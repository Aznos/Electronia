package com.maddoxh.content.screen.heater

import com.maddoxh.Electronia
import com.maddoxh.content.screen.renderer.EnergyBarRenderer
import com.maddoxh.content.screen.renderer.TemperatureRenderer
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class HeaterScreen(handler: HeaterScreenHandler, inventory: PlayerInventory, text: Text)
    : HandledScreen<HeaterScreenHandler>(handler, inventory, text)
{
    private val texture: Identifier = Identifier.of(Electronia.MOD_ID, "textures/gui/heater/screen.png")
    private lateinit var temperatureArea: TemperatureRenderer

    override fun init() {
        super.init()

        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        temperatureArea = TemperatureRenderer(
            x + 58, y + 61,
            handler.blockEntity.temperature, handler.blockEntity.maxTemp
        )
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
        temperatureArea.draw(context)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

        if(temperatureArea.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(textRenderer, temperatureArea.tooltip(), mouseX, mouseY)
        }

        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}