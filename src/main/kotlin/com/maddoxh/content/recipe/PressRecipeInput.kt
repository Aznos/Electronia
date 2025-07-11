package com.maddoxh.content.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

data class PressRecipeInput(val input: ItemStack) : RecipeInput {
    override fun getStackInSlot(slot: Int): ItemStack? {
        return input
    }

    override fun getSize(): Int {
        return 1
    }
}