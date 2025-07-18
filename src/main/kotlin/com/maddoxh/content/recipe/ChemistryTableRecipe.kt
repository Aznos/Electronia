package com.maddoxh.content.recipe

import com.maddoxh.registry.ModRecipes
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import java.util.Optional

data class ChemistryTableRecipe(val ingredient: Ingredient, val result: ItemStack, val fluid: FluidVariant? = null, val fluidReq: Long = 0L) : Recipe<ChemistryTableRecipeInput> {
    override fun matches(
        input: ChemistryTableRecipeInput,
        world: World
    ): Boolean {
        if(world.isClient) return false
        if(!ingredient.test(input.input)) return false

        return fluid == null || (input.fluid.fluid == fluid.fluid && input.amount >= fluidReq)
    }

    override fun craft(
        input: ChemistryTableRecipeInput,
        lookup: RegistryWrapper.WrapperLookup
    ): ItemStack {
        return result.copy()
    }

    override fun fits(width: Int, height: Int): Boolean {
        return true
    }

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack {
        return result
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipes.chemistryTableSerializer
    }

    override fun getType(): RecipeType<*> {
        return ModRecipes.chemistryTableType
    }

    class Serializer : RecipeSerializer<ChemistryTableRecipe> {
        private val FLUID = FluidVariant.CODEC.optionalFieldOf("fluid")
        private val AMOUNT = Codec.LONG.optionalFieldOf("fluid_amount", 0L)
        @Suppress("UNCHECKED_CAST")
        private val OPTIONAL_FLUID : PacketCodec<RegistryByteBuf, Optional<FluidVariant>> =
            PacketCodecs.optional(FluidVariant.PACKET_CODEC) as PacketCodec<RegistryByteBuf, Optional<FluidVariant>>

        @Suppress("UNCHECKED_CAST")
        private val VAR_LONG : PacketCodec<RegistryByteBuf, Long> =
            PacketCodecs.VAR_LONG as PacketCodec<RegistryByteBuf, Long>

        val codec: MapCodec<ChemistryTableRecipe> =
            RecordCodecBuilder.mapCodec { inst ->
                inst.group(
                    Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient")
                        .forGetter(ChemistryTableRecipe::ingredient),
                    ItemStack.CODEC.fieldOf("result")
                        .forGetter(ChemistryTableRecipe::result),
                    FLUID
                        .forGetter { r -> Optional.ofNullable(r.fluid) },
                    AMOUNT.forGetter(ChemistryTableRecipe::fluidReq)
                ).apply(inst) { ing, res, fluOpt, amt ->
                    ChemistryTableRecipe(ing, res, fluOpt.orElse(null), amt)
                }
            }

        val streamCodec: PacketCodec<RegistryByteBuf, ChemistryTableRecipe> =
            PacketCodec.tuple(
                Ingredient.PACKET_CODEC as PacketCodec<RegistryByteBuf, Ingredient>, { it.ingredient },
                ItemStack.PACKET_CODEC  as PacketCodec<RegistryByteBuf, ItemStack>, { it.result },
                OPTIONAL_FLUID, { Optional.ofNullable(it.fluid) },
                VAR_LONG, { it.fluidReq }
            ) { ing, res, fluOpt, amt ->
                ChemistryTableRecipe(ing, res, fluOpt.orElse(null), amt)
            }

        override fun codec(): MapCodec<ChemistryTableRecipe> {
            return codec
        }

        override fun packetCodec(): PacketCodec<RegistryByteBuf, ChemistryTableRecipe> {
            return streamCodec
        }
    }
}