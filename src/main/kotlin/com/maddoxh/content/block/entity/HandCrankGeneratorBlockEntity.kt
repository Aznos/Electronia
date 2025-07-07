package com.maddoxh.content.block.entity

import com.maddoxh.content.energy.EnergyStorage
import com.maddoxh.registry.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HandCrankGeneratorBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.HAND_CRANK_GENERATOR, pos, state), EnergyStorage {
    override fun insert(amount: Long, simulate: Boolean): Long {
        TODO("Not yet implemented")
    }

    override fun extract(amount: Long, simulate: Boolean): Long {
        TODO("Not yet implemented")
    }

    override fun getStored(): Long {
        TODO("Not yet implemented")
    }

    override fun getCapacity(): Long {
        TODO("Not yet implemented")
    }
}