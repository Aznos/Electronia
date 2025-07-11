package com.maddoxh.integration

import com.maddoxh.content.recipe.PressRecipe
import com.maddoxh.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class CrankPressCategory(private val gui: IGuiHelper) : IRecipeCategory<PressRecipe> {
    override fun getRecipeType(): RecipeType<PressRecipe> = PressingJeiPlugin.PRESSING_TYPE
    override fun getTitle(): Text = Text.translatable("jei.electronia.pressing")
    override fun getBackground(): IDrawable = gui.createBlankDrawable(96, 32)
    override fun getIcon(): IDrawable = gui.createDrawableItemStack(ItemStack(ModBlocks.CRANK_PRESS))

    override fun setRecipe(layout: IRecipeLayoutBuilder, recipe: PressRecipe, focuses: IFocusGroup) {
        layout.addSlot(RecipeIngredientRole.INPUT, 12, 8).addIngredients(recipe.inputItem)
        layout.addSlot(RecipeIngredientRole.OUTPUT, 64, 8).addItemStack(recipe.output)
    }
}