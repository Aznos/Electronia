package com.maddoxh.content.block.entity.renderer

import com.maddoxh.Electronia
import com.maddoxh.content.block.CrankPress
import com.maddoxh.content.block.entity.CrankPressBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
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

        if(!stack.isEmpty) {
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

        val progress = entity.crankProgress / 100f
        val yOffset = -0.615 * progress
        val pistonModel = MinecraftClient.getInstance()
            .bakedModelManager.getModel(Identifier.of(Electronia.MOD_ID, "block/crank_press_piston"))

        val consumer: VertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE))

        val (offX, offZ) = when(facing) {
            Direction.SOUTH -> -0.375 to -0.12
            Direction.NORTH ->  0.375 to 0.12
            Direction.WEST  ->  -0.435 to 0.375
            Direction.EAST  ->  0.435 to -0.375
            else            ->  0.0   to  0.0
        }

        matrices.push()
        matrices.translate(
            0.5 + offX,
            1.35 + yOffset,
            0.5 + offZ
        )
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw))

        MinecraftClient.getInstance()
            .blockRenderManager
            .modelRenderer
            .render(
                matrices.peek(),
                consumer,
                null,
                pistonModel,
                1f, 1f, 1f,
                light,
                overlay
            )

        matrices.pop()
    }
}