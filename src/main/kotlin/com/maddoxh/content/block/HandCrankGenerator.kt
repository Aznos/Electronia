package com.maddoxh.content.block

import com.maddoxh.content.block.entity.HandCrankGeneratorBlockEntity
import com.maddoxh.registry.ModSounds
import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

/**
 * A hand-cranked generator that produces power when cranked in EU
 */
class HandCrankGenerator(settings: Settings) : MachineBlock(settings) {
    companion object {
        val CODEC: MapCodec<HandCrankGenerator> = createCodec(::HandCrankGenerator)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return HandCrankGeneratorBlockEntity(pos, state)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = CODEC

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return createCuboidShape(-4.0, 0.0, -4.0, 20.0, 28.0, 20.0)
    }

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

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if(!world.isClient) {
            val be = world.getBlockEntity(pos) as? HandCrankGeneratorBlockEntity ?: return ActionResult.PASS
            player.sendMessage(Text.literal(be.crank(world)), true)
        }

        return ActionResult.SUCCESS
    }
}