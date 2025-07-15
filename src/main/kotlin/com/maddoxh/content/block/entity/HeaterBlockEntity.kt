package com.maddoxh.content.block.entity

import com.maddoxh.content.screen.crankpress.CrankPressScreenHandler
import com.maddoxh.registry.ModBlockEntities
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

const val AMBIENT_TEMP = 25
const val MAX_HEAT = 300
const val WARMUP_HEAT = 4
const val COOLDOWN_RATE = 1

class HeaterBlockEntity(pos: BlockPos, state: BlockState)
    : BlockEntity(ModBlockEntities.HEATER, pos, state), Inventory, ExtendedScreenHandlerFactory<BlockPos>
{
    var temperature = AMBIENT_TEMP
    private var tickCounter = 0
    val inv: DefaultedList<ItemStack> = DefaultedList.ofSize(1, ItemStack.EMPTY)

    fun serverTick() {
        tickCounter++
    }

    private fun sync() {
        world?.let { w ->
            val pkt = BlockEntityUpdateS2CPacket.create(this)
            w.server?.playerManager?.playerList
                ?.filterIsInstance<ServerPlayerEntity>()
                ?.filter { it.world == w }
                ?.forEach { it.networkHandler.sendPacket(pkt) }
        }
    }

    override fun size() = inv.size
    override fun isEmpty() = inv.all { it.isEmpty }
    override fun getStack(slot: Int) = inv[slot]
    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(inv, slot, amount)
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(inv, slot)
    override fun getMaxCountPerStack(): Int = 1

    override fun setStack(slot: Int, stack: ItemStack) {
        inv[slot] = stack
    }

    override fun canPlayerUse(player: PlayerEntity) = world?.getBlockEntity(pos) === this &&
            player.squaredDistanceTo(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64.0

    override fun clear() = inv.clear()
    override fun getDisplayName(): Text = Text.literal("Heater")

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity
    ) = CrankPressScreenHandler(syncId, playerInventory, this)

    override fun getScreenOpeningData(p0: ServerPlayerEntity?): BlockPos? {
        return this.pos
    }

    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, inv, registryLookup)
        nbt?.putInt("Temp", temperature)
    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)
        if(nbt != null) {
            Inventories.readNbt(nbt, inv, registryLookup)
        }

        temperature = nbt?.getInt("Temp") ?: AMBIENT_TEMP
    }

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound =
        createNbt(registryLookup)
}