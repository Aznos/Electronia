package com.maddoxh.integration

import com.maddoxh.Electronia
import com.maddoxh.content.recipe.ChemistryTableRecipe
import com.maddoxh.content.recipe.PressRecipe
import com.maddoxh.registry.ModBlocks
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

class ChemistryTableCategory(private val gui: IGuiHelper) : IRecipeCategory<ChemistryTableRecipe> {
    companion object {
        private val tex = Identifier.of(Electronia.MOD_ID, "textures/gui/chemistry_table/screen.png")
        private val bgW = 132
        private val bgH = 54

        private val itemX = 76
        private val itemY = 6

        private val fluidX = 42
        private val fluidY = 10
        private val fluidW = 16
        private val fluidH = 48

        private val outX = 76
        private val outY = 41

        private val thermX = 103
        private val thermY = 50
    }

    private val background: IDrawable = gui.drawableBuilder(tex, 0, 0, bgW, bgH).setTextureSize(256, 256).build()
    private val icon: IDrawable = gui.createDrawableItemStack(ItemStack(ModBlocks.CHEMISTRY_TABLE))

    override fun getRecipeType(): RecipeType<ChemistryTableRecipe> = PressingJeiPlugin.CHEMISTRY_TABLE_TYPE
    override fun getTitle(): Text = Text.translatable("jei.electronia.chemistry_table")
    override fun getBackground(): IDrawable = background
    override fun getIcon(): IDrawable = icon

    override fun setRecipe(layout: IRecipeLayoutBuilder, recipe: ChemistryTableRecipe, group: IFocusGroup) {
        layout.addSlot(RecipeIngredientRole.INPUT, itemX, itemY)
            .addIngredients(recipe.ingredient)

        if(recipe.fluid != null && recipe.fluidReq > 0) {
            val mb = dropletsToMB(recipe.fluidReq)

            layout.addSlot(RecipeIngredientRole.INPUT, fluidX, fluidY)
                .setFluidRenderer(mb.toLong(), false, fluidW, fluidH)
                .addIngredients(VanillaTypes.ITEM_STACK, listOf(ItemStack(recipe.fluid.fluid.bucketItem)))
                .addTooltipCallback(fluidTooltip(recipe))

            layout.addSlot(RecipeIngredientRole.OUTPUT, outX, outY)
                .addItemStack(recipe.result)
                .addTooltipCallback(outputTooltip(recipe))
        }
    }

    override fun draw(
        recipe: ChemistryTableRecipe,
        recipeSlotsView: IRecipeSlotsView,
        guiGraphics: DrawContext,
        mouseX: Double,
        mouseY: Double
    ) {

    }

    private fun fluidTooltip(recipe: ChemistryTableRecipe) = IRecipeSlotTooltipCallback { _, tooltip ->
        val req = recipe.fluidReq
        val mb = dropletsToMB(req)
        tooltip.add(Text.translatable("electronia.tooltip.liquid.amount.with.capacity", mb, "mB"))
        tooltip.add(Text.literal("$req droplets"))
        if(recipe.tempReq > 0) {
            tooltip.add(Text.literal("Needs ≥${recipe.tempReq}°C").formatted(Formatting.RED))
        }
    }

    private fun outputTooltip(recipe: ChemistryTableRecipe) = IRecipeSlotTooltipCallback { _, tooltip ->
        if(recipe.tempReq > 0) {
            tooltip.add(Text.literal("Heated process (≥${recipe.tempReq}°C)."))
        } else {
            tooltip.add(Text.literal("Ambient process."))
        }

        if(recipe.fluid != null && recipe.fluidReq > 0) {
            val mb = dropletsToMB(recipe.fluidReq)
            tooltip.add(Text.literal("Consumes $mb mB ${recipe.fluid.fluid.defaultState.blockState?.block?.name?.string ?: "fluid"}"))
        }
    }

    private fun dropletsToMB(droplets: Long): Int {
        return (droplets / 81.0).roundToInt()
    }
}