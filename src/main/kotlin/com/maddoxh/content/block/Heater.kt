package com.maddoxh.content.block

import com.maddoxh.content.block.entity.HeaterBlockEntity
import com.maddoxh.registry.ModBlockEntities
import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class Heater(settings: Settings): MachineBlock(settings) {
    private val shapeN  = createCuboidShape(
        0.0, 0.0, 0.0,
        16.0, 6.0, 32.0)

    private val shapeS  = createCuboidShape(
        0.0, 0.0, -16.0,
        16.0, 6.0, 16.0)

    private val shapeE  = createCuboidShape(
        0.0, 0.0, 0.0,
        32.0, 6.0, 16.0)

    private val shapeW  = createCuboidShape(
        0.0, 0.0, 0.0,
        32.0, 6.0, 16.0)

    private val shapeByFacing: Map<Direction, VoxelShape> = mapOf(
        Direction.NORTH to shapeN,
        Direction.SOUTH to shapeS,
        Direction.EAST to shapeE,
        Direction.WEST to shapeW
    )

    companion object {
        val CODEC: MapCodec<Heater> = createCodec(::Heater)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return HeaterBlockEntity(pos, state)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = CODEC

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {
        return shape(state)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {
        return shape(state)
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
        if(!updateBE(world, pos, state, player)) return ActionResult.PASS
        return ActionResult.SUCCESS
    }

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ItemActionResult {
        if(!updateBE(world, pos, state, player)) return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        return ItemActionResult.SUCCESS
    }

    private fun updateBE(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): Boolean {
        if(!world.isClient) {
            val be = world.getBlockEntity(pos) as? HeaterBlockEntity ?: return false
            player.openHandledScreen(be)

            be.markDirty()
            world.updateListeners(pos, state, state, 0)
        }

        return true
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if(state.block != newState.block) {
            (world.getBlockEntity(pos) as? HeaterBlockEntity)?.let { be ->
                ItemScatterer.spawn(world, pos, be.inv)
            }

            world.removeBlockEntity(pos)
        }

        super.onStateReplaced(state, world, pos, newState, moved)
    }


    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if(world.isClient || type != ModBlockEntities.HEATER) return null

        return BlockEntityTicker<T> {
                tickWorld: World,
                tickPos: BlockPos,
                tickState: BlockState,
                blockEntity: T ->
            (blockEntity as HeaterBlockEntity).serverTick()
        }
    }

    private fun shape(state: BlockState) = shapeByFacing[state.get(FACING)]
}