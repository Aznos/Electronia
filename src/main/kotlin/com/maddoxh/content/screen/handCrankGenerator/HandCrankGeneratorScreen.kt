package com.maddoxh.content.screen.handCrankGenerator

import com.maddoxh.Electronia
import com.maddoxh.content.screen.renderer.EnergyBarRenderer
import com.mojang.blaze3d.systems.RenderSystem
import kotlinx.coroutines.runBlocking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class HandCrankGeneratorScreen(handler: HandCrankGeneratorScreenHandler, inventory: PlayerInventory, text: Text)
    : HandledScreen<HandCrankGeneratorScreenHandler>(handler, inventory, text)
{
    private val texture: Identifier = Identifier.of(Electronia.MOD_ID, "textures/gui/hand_crank_generator/screen.png")
    private lateinit var energyArea: EnergyBarRenderer

    override fun init() {
        super.init()

        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        energyArea = EnergyBarRenderer(x + 81, y + 16, handler.blockEntity)
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
        energyArea.draw(context)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

        if(energyArea.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(textRenderer, energyArea.tooltip(), mouseX, mouseY)
        }

        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}