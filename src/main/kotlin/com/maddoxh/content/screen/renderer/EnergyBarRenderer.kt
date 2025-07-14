package com.maddoxh.content.screen.renderer

import com.maddoxh.content.energy.EnergyStorage
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.Rect2i
import net.minecraft.text.Text

class EnergyBarRenderer(
    private val x: Int,
    private val y: Int,
    private val storage: EnergyStorage,
    private val width: Int = 13,
    private val height: Int = 56
) {
    private val area: Rect2i = Rect2i(x, y, width, height)

    fun draw(context: DrawContext) {
        val ratio = storage.getStored().toFloat() / storage.getCapacity().toFloat()
        val filled = (height * ratio).toInt()

        context.fillGradient(
            x, y + (height - filled),
            x + width, y + height,
            0xFF00FF00.toInt(), 0xFF007F00.toInt()
        )
    }

    fun tooltip(): List<Text> = listOf(Text.literal("${storage.getStored()} / ${storage.getCapacity()} EU"))

    fun isMouseOver(mouseX: Int, mouseY: Int): Boolean = area.contains(mouseX, mouseY)
}