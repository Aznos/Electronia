package com.maddoxh.content.block.entity

import com.maddoxh.content.energy.EnergyStorage
import com.maddoxh.content.screen.handCrankGenerator.HandCrankGeneratorScreenHandler
import com.maddoxh.registry.ModBlockEntities
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
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HandCrankGeneratorBlockEntity(pos: BlockPos, state: BlockState)
    : BlockEntity(ModBlockEntities.HAND_CRANK_GENERATOR, pos, state),
    EnergyStorage, Inventory, ExtendedScreenHandlerFactory<BlockPos>
{
    private var storedEu: Long = 0
    private val capacity: Long = 250
    private var lastCrankTime: Long = 0
    val inv: DefaultedList<ItemStack> = DefaultedList.ofSize(1, ItemStack.EMPTY)

    fun crank(world: World): String {
        val now = System.currentTimeMillis()
        if(now - lastCrankTime < 2000) {
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1.0f, 1.0f)
            return "Generator is still cranking! Please wait."
        }

        if(storedEu >= capacity) {
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1.0f, 1.0f)
            return "Generator is full! Current stored EU: $storedEu"
        }

        storedEu += 10
        lastCrankTime = now
        markDirty()

        world.playSound(null, pos, ModSounds.CRANK, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return "Cranked! Current stored EU: $storedEu"
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        nbt.putLong("StoredEU", storedEu)
        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        storedEu = nbt.getLong("StoredEU")
    }

    override fun insert(amount: Long, simulate: Boolean): Long {
        val insertable = (capacity - storedEu).coerceAtMost(amount)
        if(!simulate) storedEu += insertable
        return insertable
    }

    override fun extract(amount: Long, simulate: Boolean): Long {
        val extractable = storedEu.coerceAtMost(amount)
        if(!simulate) storedEu -= extractable
        return extractable
    }

    override fun getStored(): Long = storedEu
    override fun getCapacity(): Long = capacity
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
    override fun getDisplayName(): Text = Text.literal("Hand Crank Generator")

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity
    ) = HandCrankGeneratorScreenHandler(syncId, playerInventory, this)

    override fun getScreenOpeningData(p0: ServerPlayerEntity?): BlockPos? {
        return this.pos
    }
}