package com.maddoxh.content.block.entity

import com.maddoxh.content.energy.EnergyStorage
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModSounds
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HandCrankGeneratorBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.HAND_CRANK_GENERATOR, pos, state), EnergyStorage {
    private var storedEu: Long = 0
    private val capacity: Long = 250
    private var lastCrankTime: Long = 0

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
}