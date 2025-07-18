package com.maddoxh.content.recipe

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

data class ChemistryTableRecipeInput(val input: ItemStack, val fluid: FluidVariant, val amount: Long) : RecipeInput {
    override fun getStackInSlot(slot: Int): ItemStack? {
        return input
    }

    override fun getSize(): Int {
        return 1
    }
}