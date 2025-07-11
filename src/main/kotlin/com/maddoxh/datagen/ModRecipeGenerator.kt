package com.maddoxh.datagen

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
    }
}