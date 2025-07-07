package com.maddoxh.content.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.math.Direction

/**
 * Base class for all Electronia machine blocks
 * Adds horizontal facing support and other common features
 *
 * @param settings The settings for the block
 */
abstract class MachineBlock(settings: Settings) : BlockWithEntity(settings) {
    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
    }

    init {
        defaultState = defaultState.with(FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)
    }
}