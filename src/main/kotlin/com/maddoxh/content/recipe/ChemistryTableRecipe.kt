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

data class ChemistryTableRecipe(
    val ingredient: Ingredient, val result: ItemStack, val fluid: FluidVariant? = null, val fluidReq: Long = 0L, val tempReq: Double = 0.0
) : Recipe<ChemistryTableRecipeInput> {
    override fun matches(
        input: ChemistryTableRecipeInput,
        world: World
    ): Boolean {
        if(world.isClient) return false
        if(!ingredient.test(input.input)) return false

        if (fluid != null &&
            (input.fluid.isBlank || input.fluid.fluid != fluid.fluid || input.amount < fluidReq)
        ) return false

        if(tempReq > 0 && input.temperature < tempReq) return false
        return true
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
        private val fluid = FluidVariant.CODEC.optionalFieldOf("fluid")
        private val amount = Codec.LONG.optionalFieldOf("fluid_amount", 0L)
        private val temp = Codec.DOUBLE.optionalFieldOf("temperature", 0.0)
        @Suppress("UNCHECKED_CAST")
        private val optionalFluid : PacketCodec<RegistryByteBuf, Optional<FluidVariant>> =
            PacketCodecs.optional(FluidVariant.PACKET_CODEC) as PacketCodec<RegistryByteBuf, Optional<FluidVariant>>

        @Suppress("UNCHECKED_CAST")
        private val varLong : PacketCodec<RegistryByteBuf, Long> =
            PacketCodecs.VAR_LONG as PacketCodec<RegistryByteBuf, Long>

        @Suppress("UNCHECKED_CAST")
        private val varDouble : PacketCodec<RegistryByteBuf, Double> =
            PacketCodecs.DOUBLE as PacketCodec<RegistryByteBuf, Double>

        val codec: MapCodec<ChemistryTableRecipe> =
            RecordCodecBuilder.mapCodec { inst ->
                inst.group(
                    Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient")
                        .forGetter(ChemistryTableRecipe::ingredient),
                    ItemStack.CODEC.fieldOf("result")
                        .forGetter(ChemistryTableRecipe::result),
                    fluid
                        .forGetter { r -> Optional.ofNullable(r.fluid) },
                    amount.forGetter(ChemistryTableRecipe::fluidReq),
                    temp.forGetter(ChemistryTableRecipe::tempReq)
                ).apply(inst) { ing, res, fluOpt, amt, tmp ->
                    ChemistryTableRecipe(ing, res, fluOpt.orElse(null), amt, tmp)
                }
            }

        val streamCodec: PacketCodec<RegistryByteBuf, ChemistryTableRecipe> =
            PacketCodec.tuple(
                Ingredient.PACKET_CODEC as PacketCodec<RegistryByteBuf, Ingredient>, { it.ingredient },
                ItemStack.PACKET_CODEC  as PacketCodec<RegistryByteBuf, ItemStack>, { it.result },
                optionalFluid, { Optional.ofNullable(it.fluid) },
                varLong, { it.fluidReq },
                varDouble, { it.tempReq }
            ) { ing, res, fluOpt, amt, tmp ->
                ChemistryTableRecipe(ing, res, fluOpt.orElse(null), amt, tmp)
            }

        override fun codec(): MapCodec<ChemistryTableRecipe> {
            return codec
        }

        override fun packetCodec(): PacketCodec<RegistryByteBuf, ChemistryTableRecipe> {
            return streamCodec
        }
    }
}