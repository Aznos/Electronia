package com.maddoxh.content.recipe

import com.maddoxh.registry.ModRecipes
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

data class PressRecipe(val inputItem: Ingredient, val output: ItemStack) : Recipe<PressRecipeInput> {
    override fun getIngredients(): DefaultedList<Ingredient> {
        val list: DefaultedList<Ingredient> = DefaultedList.of()
        list.add(inputItem)

        return list
    }

    override fun matches(
        input: PressRecipeInput,
        world: World
    ): Boolean {
        if(world.isClient) return false
        return inputItem.test(input.getStackInSlot(0))
    }

    override fun craft(
        input: PressRecipeInput,
        lookup: RegistryWrapper.WrapperLookup
    ): ItemStack {
        return output.copy()
    }

    override fun fits(width: Int, height: Int): Boolean {
        return true
    }

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack {
        return output
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipes.pressSerializer
    }

    override fun getType(): RecipeType<*> {
        return ModRecipes.pressType
    }

    class Serializer : RecipeSerializer<PressRecipe> {
        val codec: MapCodec<PressRecipe> =
            RecordCodecBuilder.mapCodec { inst ->
                inst.group(
                    Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(PressRecipe::inputItem),
                    ItemStack.CODEC.fieldOf("result").forGetter(PressRecipe::output)
                ).apply(inst, ::PressRecipe)
            }

        val streamCodec: PacketCodec<RegistryByteBuf, PressRecipe> =
            PacketCodec.tuple(
                Ingredient.PACKET_CODEC, { it.inputItem },
                ItemStack.PACKET_CODEC, { it.output },
                { ingredient, result -> PressRecipe(ingredient, result) }
            )

        override fun codec(): MapCodec<PressRecipe> {
            return codec
        }

        override fun packetCodec(): PacketCodec<RegistryByteBuf, PressRecipe> {
            return streamCodec
        }
    }
}