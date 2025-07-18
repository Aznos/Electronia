package com.maddoxh.registry

import com.maddoxh.Electronia
import com.maddoxh.content.recipe.ChemistryTableRecipe
import com.maddoxh.content.recipe.PressRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModRecipes {
    val pressSerializer: RecipeSerializer<PressRecipe> = Registry.register(
        Registries.RECIPE_SERIALIZER,
        Identifier.of(Electronia.MOD_ID, "pressing"),
        PressRecipe.Serializer()
    )

    val pressType: RecipeType<PressRecipe> = Registry.register(
        Registries.RECIPE_TYPE,
        Identifier.of(Electronia.MOD_ID, "pressing"),
        object : RecipeType<PressRecipe> {
            override fun toString(): String = "pressing"
        }
    )

    val chemistryTableSerializer: RecipeSerializer<ChemistryTableRecipe> = Registry.register(
        Registries.RECIPE_SERIALIZER,
        Identifier.of(Electronia.MOD_ID, "chemistry_table"),
        ChemistryTableRecipe.Serializer()
    )

    val chemistryTableType: RecipeType<ChemistryTableRecipe> = Registry.register(
        Registries.RECIPE_TYPE,
        Identifier.of(Electronia.MOD_ID, "chemistry_table"),
        object : RecipeType<ChemistryTableRecipe> {
            override fun toString(): String = "chemistry_table"
        }
    )

    fun register() {}
}