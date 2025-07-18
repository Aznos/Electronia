package com.maddoxh.integration

import com.maddoxh.Electronia
import com.maddoxh.content.recipe.ChemistryTableRecipe
import com.maddoxh.content.recipe.PressRecipe
import com.maddoxh.registry.ModRecipes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IModIngredientRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

@JeiPlugin
class PressingJeiPlugin : IModPlugin {
    override fun getPluginUid(): Identifier = Identifier.of(Electronia.MOD_ID, "pressing")

    companion object {
        val PRESSING_TYPE: RecipeType<PressRecipe> = RecipeType.create(
            Electronia.MOD_ID,
            "pressing",
            PressRecipe::class.java
        )

        val CHEMISTRY_TABLE_TYPE: RecipeType<ChemistryTableRecipe> = RecipeType.create(
            Electronia.MOD_ID,
            "chemistry_table",
            ChemistryTableRecipe::class.java
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper = registration.jeiHelpers.guiHelper
        registration.addRecipeCategories(CrankPressCategory(guiHelper), ChemistryTableCategory(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val world = MinecraftClient.getInstance().world ?: return
        val pressRecipes: List<PressRecipe> = world.recipeManager.listAllOfType(ModRecipes.pressType).map { it.value() }
        val chemTableRecipes: List<ChemistryTableRecipe> = world.recipeManager.listAllOfType(ModRecipes.chemistryTableType).map { it.value() }

        registration.addRecipes(PRESSING_TYPE, pressRecipes)
        registration.addRecipes(CHEMISTRY_TABLE_TYPE, chemTableRecipes)
    }
}