package com.maddoxh.content.screen.chemistryTable

import com.maddoxh.Electronia
import com.maddoxh.content.screen.renderer.FluidStackRenderer
import com.maddoxh.content.screen.renderer.TemperatureRenderer
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ChemistryTableScreen(handler: ChemistryTableScreenHandler, inventory: PlayerInventory, text: Text)
    : HandledScreen<ChemistryTableScreenHandler>(handler, inventory, text)
{
    private val tempX = 67
    private val tempY = 67
    private val texture: Identifier = Identifier.of(Electronia.MOD_ID, "textures/gui/chemistry_table/screen.png")
    private val fluidRenderer = FluidStackRenderer(
        capacityMb = 1_000,
        showCapacity = true,
        width = 16,
        height = 64
    )

    override fun init() {
        super.init()

        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, texture)

        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2

        context.drawTexture(texture, x, y, 0, 0, backgroundWidth, backgroundHeight)

        TemperatureRenderer(
            (width - backgroundWidth) / 2  + tempX,
            (height - backgroundHeight) / 2 + tempY,
            handler.blockEntity.temperature,
            handler.blockEntity.maxTemp
        ).draw(context)

        val tank = handler.blockEntity.tank
        fluidRenderer.drawFluid(context, tank, tankLeft(), tankTop(), 16, 64, FluidConstants.BUCKET)
    }

    override fun drawForeground(context: DrawContext?, mouseX: Int, mouseY: Int) {}

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

        val tempRenderer = TemperatureRenderer(
            (width - backgroundWidth) /2  + tempX,
            (height - backgroundHeight) / 2 + tempY,
            handler.blockEntity.temperature,
            handler.blockEntity.maxTemp
        )

        if(tempRenderer.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(textRenderer, tempRenderer.tooltip(), mouseX, mouseY)
        }

        if(fluidRenderer.isMouseOver(mouseX, mouseY, tankLeft(), tankTop())) {
            val tooltip = fluidRenderer.getTooltip(handler.blockEntity.tank)
            context.drawTooltip(textRenderer, tooltip.filterNotNull(), mouseX, mouseY)
        }

        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    private fun tankLeft()  = (width  - backgroundWidth) / 2 + 43
    private fun tankTop()   = (height - backgroundHeight) / 2 + 7
}