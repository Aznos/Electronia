package com.maddoxh.content.screen.renderer

import com.google.common.base.Preconditions
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item.TooltipContext
import net.minecraft.registry.Registries
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.min

class FluidStackRenderer private constructor(capacityMb: Long, tooltipMode: TooltipMode, width: Int, height: Int) {
    val capacityMb: Long
    private val tooltipMode: TooltipMode
    val width: Int
    val height: Int

    internal enum class TooltipMode {
        SHOW_AMOUNT,
        SHOW_AMOUNT_AND_CAPACITY,
        ITEM_LIST
    }

    constructor() : this((FluidConstants.BUCKET / 81), TooltipMode.SHOW_AMOUNT_AND_CAPACITY, 16, 16)

    constructor(capacityMb: Long, showCapacity: Boolean, width: Int, height: Int) : this(
        capacityMb,
        if (showCapacity) TooltipMode.SHOW_AMOUNT_AND_CAPACITY else TooltipMode.SHOW_AMOUNT,
        width,
        height
    )

    @Deprecated("")
    constructor(capacityMb: Int, showCapacity: Boolean, width: Int, height: Int) : this(
        capacityMb.toLong(),
        if (showCapacity) TooltipMode.SHOW_AMOUNT_AND_CAPACITY else TooltipMode.SHOW_AMOUNT,
        width,
        height
    )

    init {
        Preconditions.checkArgument(capacityMb > 0, "capacity must be > 0")
        Preconditions.checkArgument(width > 0, "width must be > 0")
        Preconditions.checkArgument(height > 0, "height must be > 0")
        this.capacityMb = capacityMb
        this.tooltipMode = tooltipMode
        this.width = width
        this.height = height
    }

    @Suppress("LongParameterList")
    fun drawFluid(
        context: DrawContext,
        fluidStorage: SingleVariantStorage<FluidVariant>,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        maxCapacity: Long
    ) {
        var y = y
        if(fluidStorage.variant!!.fluid === Fluids.EMPTY) return

        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        y += height
        val sprite = FluidVariantRendering.getSprite(fluidStorage.variant)
        val color = FluidVariantRendering.getColor(fluidStorage.variant)

        val drawHeight = ceil((fluidStorage.amount / (maxCapacity * 1f) * height).toDouble()).toInt()
        val iconHeight = sprite!!.getY()
        var offsetHeight = drawHeight

        RenderSystem.setShaderColor(
            (color shr 16 and 255) / 255.0f,
            (color shr 8 and 255).toFloat() / 255.0f,
            (color and 255).toFloat() / 255.0f,
            1f
        )

        var iteration = 0
        while(offsetHeight != 0) {
            val curHeight = min(offsetHeight, iconHeight)

            context.drawSprite(x, y - offsetHeight, 0, width, curHeight, sprite)
            offsetHeight -= curHeight
            iteration++
            if(iteration > 50) break
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(
            0, FluidRenderHandlerRegistry.INSTANCE.get(fluidStorage.variant!!.fluid)!!
                .getFluidSprites(
                    MinecraftClient.getInstance().world,
                    null,
                    fluidStorage.variant!!.fluid.defaultState
                )[0].atlasId
        )
    }

    fun getTooltip(
        fluidStorage: SingleVariantStorage<FluidVariant>,
    ): MutableList<Text> {
        val droplets = fluidStorage.amount
        val milliBuckets = droplets / 81
        val capacityMb = capacityMb

        val list = mutableListOf<Text>()
        list += Text.translatable(
            "block." + Registries.FLUID.getId(fluidStorage.variant.fluid).toTranslationKey()
        )

        list += if(tooltipMode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
            Text.translatable(
                "electronia.tooltip.liquid.amount.with.capacity",
                nf.format(milliBuckets),
                nf.format(capacityMb)
            ).fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
        } else {
            Text.translatable(
                "electronia.tooltip.liquid.amount",
                nf.format(milliBuckets)
            ).fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
        }

        return list
    }

    fun isMouseOver(mouseX: Int, mouseY: Int, originX: Int, originY: Int): Boolean =
        mouseX >= originX && mouseX < originX + width &&
        mouseY >= originY && mouseY < originY + height

    companion object {
        private val nf: NumberFormat = NumberFormat.getIntegerInstance()
    }
}