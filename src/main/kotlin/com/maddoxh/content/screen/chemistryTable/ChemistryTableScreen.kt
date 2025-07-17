package com.maddoxh.content.screen.chemistryTable

import com.maddoxh.Electronia
import com.maddoxh.content.screen.renderer.TemperatureRenderer
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ChemistryTableScreen(handler: ChemistryTableScreenHandler, inventory: PlayerInventory, text: Text)
    : HandledScreen<ChemistryTableScreenHandler>(handler, inventory, text)
{
    private val tempX = 106
    private val tempY = 67
    private val texture: Identifier = Identifier.of(Electronia.MOD_ID, "textures/gui/chemistry_table/screen.png")

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
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

        val renderer = TemperatureRenderer(
            (width - backgroundWidth) /2  + tempX,
            (height - backgroundHeight) / 2 + tempY,
            handler.blockEntity.temperature,
            handler.blockEntity.maxTemp
        )

        if(renderer.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(textRenderer, renderer.tooltip(), mouseX, mouseY)
        }

        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}