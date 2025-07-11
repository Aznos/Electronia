package com.maddoxh.integration

import com.maddoxh.Electronia
import com.maddoxh.content.recipe.PressRecipe
import com.maddoxh.registry.ModRecipes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
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
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper = registration.jeiHelpers.guiHelper
        registration.addRecipeCategories(CrankPressCategory(guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val world = MinecraftClient.getInstance().world ?: return
        val recipes: List<PressRecipe> = world.recipeManager.listAllOfType(ModRecipes.pressType).map { it.value() }
        registration.addRecipes(PRESSING_TYPE, recipes)
    }
}