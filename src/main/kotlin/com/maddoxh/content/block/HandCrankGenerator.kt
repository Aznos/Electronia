package com.maddoxh.content.block

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

/**
 * A hand-cranked generator that produces power when cranked in EU
 */
class HandCrankGenerator(settings: Settings) : MachineBlock(settings) {
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState? {
        //TODO: Update for connectivity
        return state
    }
}