package com.maddoxh.content.block.entity

import com.maddoxh.content.energy.EnergyStorage
import com.maddoxh.registry.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.BlockPos

class HandCrankGeneratorBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.HAND_CRANK_GENERATOR, pos, state), EnergyStorage {
    private var storedEu: Long = 0
    private val capacity: Long = 250
    private var lastCrankTime: Long = 0

    fun crank(): Boolean {
        val now = System.currentTimeMillis()
        if(now - lastCrankTime < 2000) return false

        if(storedEu + 25 <= capacity) {
            storedEu += 25
            lastCrankTime = now
            markDirty()
            return true
        }

        return false
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        nbt.putLong("StoredEu", storedEu)
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