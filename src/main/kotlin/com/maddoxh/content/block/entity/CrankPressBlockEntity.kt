package com.maddoxh.content.block.entity

import com.maddoxh.registry.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class CrankPressBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.CRANK_PRESS, pos, state) {
    private var lastCrankTime: Long = 0

    fun crank(): Boolean {
        return false
    }
}