package com.maddoxh.content.block.entity.renderer

import com.maddoxh.content.block.CrankPress
import com.maddoxh.content.block.entity.CrankPressBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis

class CrankPressBlockEntityRenderer(dispatcher: BlockEntityRendererFactory.Context) : BlockEntityRenderer<CrankPressBlockEntity> {
    override fun render(
        entity: CrankPressBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val stack = entity.getStack(0)
        if(stack.isEmpty) return

        val world = entity.world ?: return
        val state = world.getBlockState(entity.pos)
        if(state.block !is CrankPress) return

        val facing = state.get(Properties.HORIZONTAL_FACING)

        val yaw = when(facing) {
            Direction.NORTH -> 180f
            Direction.SOUTH ->   0f
            Direction.WEST  ->  90f
            Direction.EAST  -> -90f
            else            ->   0f
        }

        matrices.push()
        matrices.translate(0.5, 0.75, 0.5)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw))

        if(facing == Direction.NORTH || facing == Direction.SOUTH) {
            matrices.translate(0.0, 0.0, 0.2)
        } else {
            matrices.translate(0.0, 0.0, -0.2)
        }

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f))
        matrices.scale(0.5f, 0.5f, 0.5f)

        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, entity.world, 0
        )

        matrices.pop()
    }
}