package com.maddoxh.content.block.entity

import com.maddoxh.content.recipe.PressRecipeInput
import com.maddoxh.content.screen.crankpress.CrankPressScreenHandler
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModRecipes
import com.maddoxh.registry.ModSounds
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
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class CrankPressBlockEntity(pos: BlockPos, state: BlockState)
    : BlockEntity(ModBlockEntities.CRANK_PRESS, pos, state), Inventory, ExtendedScreenHandlerFactory<BlockPos>
{
    var crankProgress = 0
    private var tickCounter = 0

    val inv: DefaultedList<ItemStack> = DefaultedList.ofSize(1, ItemStack.EMPTY)

    fun crank() {
        crankProgress = (crankProgress + 15).coerceAtMost(100)
        world?.playSound(null, pos, ModSounds.CRANK, SoundCategory.BLOCKS, 1f, 1f)
        if(crankProgress >= 100) {
            world?.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f)
            tryCraft()
        }

        markDirty()
        sync()
    }

    fun serverTick() {
        tickCounter++
        if(tickCounter >= 20) {
            tickCounter = 0
            if(crankProgress > 0) {
                crankProgress = (crankProgress - 5).coerceAtLeast(0)
                markDirty()
                sync()
            }
        }
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

    private fun tryCraft() {
        val w = world ?: return
        if(w.isClient) return
        val s = w.server ?: return

        val inStack = inv[0]
        if(inStack.isEmpty) return

        val recipeEntry = s.recipeManager.getFirstMatch(ModRecipes.pressType, PressRecipeInput(inStack), w)
        if(recipeEntry.isPresent) {
            val recipe = recipeEntry.get().value()
            inv[0] = recipe.output.copy()
            markDirty()
            sync()
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
    override fun getDisplayName(): Text = Text.literal("Crank Press")

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
        nbt?.putInt("CrankProgress", crankProgress)
    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)
        if(nbt != null) {
            Inventories.readNbt(nbt, inv, registryLookup)
        }

        crankProgress = nbt?.getInt("CrankProgress") ?: 0
    }

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound =
        createNbt(registryLookup)
}