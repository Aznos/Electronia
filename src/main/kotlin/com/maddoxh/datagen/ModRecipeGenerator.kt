package com.maddoxh.datagen

import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Blocks
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModRecipeGenerator(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricRecipeProvider(output, registriesFuture) {
    override fun generate(exporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CRANK)
            .pattern("   ")
            .pattern("PPS")
            .pattern("   ")
            .input('P', Blocks.OAK_PLANKS)
            .input('S', Blocks.STONE)
            .criterion("has_oak_planks", conditionsFromItem(Blocks.OAK_PLANKS))
            .criterion("has_stone", conditionsFromItem(Blocks.STONE))
            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CRANK_PRESS)
            .pattern(" #S")
            .pattern(" @P")
            .pattern("CPS")
            .input('#', Blocks.PISTON)
            .input('@', Blocks.STONE_PRESSURE_PLATE)
            .input('S', Blocks.OAK_STAIRS)
            .input('P', Blocks.OAK_PLANKS)
            .input('C', ModItems.CRANK)
            .criterion("has_piston", conditionsFromItem(Blocks.PISTON))
            .criterion("has_stone_pressure_plate", conditionsFromItem(Blocks.STONE_PRESSURE_PLATE))
            .criterion("has_oak_stairs", conditionsFromItem(Blocks.OAK_STAIRS))
            .criterion("has_oak_planks", conditionsFromItem(Blocks.OAK_PLANKS))
            .criterion("has_crank", conditionsFromItem(ModItems.CRANK))
            .offerTo(exporter)
    }
}