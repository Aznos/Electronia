package com.maddoxh.content.screen.renderer

import com.maddoxh.content.energy.EnergyStorage
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.Rect2i
import net.minecraft.text.Text

class TemperatureRenderer(
    private val x: Int,
    private val y: Int,
    private val temperature: Int,
    private val maxTemperature: Int,
    private val width: Int = 60,
    private val height: Int = 6
) {
    private val area: Rect2i = Rect2i(x, y, width, height)

    fun draw(context: DrawContext) {
        val ratio = (temperature / maxTemperature).toFloat()
        val filled = (width * ratio).toInt()

        context.fillGradient(
            area.x, area.y, area.x + filled, area.y + height,
            0xFFFF0000.toInt(), 0xFF800000.toInt()
        )
    }

    fun tooltip(): List<Text> = listOf(Text.literal("$temperature / $maxTemperature °C"))
    fun isMouseOver(mouseX: Int, mouseY: Int): Boolean = area.contains(mouseX, mouseY)
}