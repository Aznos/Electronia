package com.maddoxh.datagen

import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Blocks
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModRecipeGenerator(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture)
{
    @Suppress("LongMethod")
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.CRANK_PRESS)
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.HAND_CRANK_GENERATOR)
            .pattern("PPP")
            .pattern("CWP")
            .pattern("PPP")
            .input('P', Blocks.OAK_PLANKS)
            .input('C', ModItems.CRANK)
            .input('W', ModItems.COPPER_WIRE)
            .criterion("has_oak_planks", conditionsFromItem(Blocks.OAK_PLANKS))
            .criterion("has_crank", conditionsFromItem(ModItems.CRANK))
            .criterion("has_copper_wire", conditionsFromItem(ModItems.COPPER_WIRE))
            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.HAND_CRANK_GENERATOR)
            .pattern("III")
            .pattern("S S")
            .pattern("SSS")
            .input('I', Items.IRON_INGOT)
            .input('S', Blocks.STONE)
            .criterion("has_iron_ingot", conditionsFromItem(Items.IRON_INGOT))
            .criterion("has_stone", conditionsFromItem(Blocks.STONE))
            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CHEMISTRY_TABLE)
            .pattern("G G")
            .pattern("WWW")
            .pattern("SIS")
            .input('G', Blocks.GLASS)
            .input('W', Blocks.OAK_PLANKS)
            .input('S', Blocks.STONE)
            .input('I', Items.IRON_INGOT)
            .criterion("has_glass", conditionsFromItem(Blocks.GLASS))
            .criterion("has_oak_planks", conditionsFromItem(Blocks.OAK_PLANKS))
            .criterion("has_stone", conditionsFromItem(Blocks.STONE))
            .criterion("has_iron_ingot", conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter)

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.LEAD_ACID_BATTERY)
            .pattern("LCL")
            .pattern("LDL")
            .pattern("LCL")
            .input('L', ModItems.LEAD_PLATE)
            .input('C', ModItems.COPPER_WIRE)
            .input('D', ModItems.DILUTED_ACID)
            .criterion("has_lead_plate", conditionsFromItem(ModItems.LEAD_PLATE))
            .criterion("has_copper_wire", conditionsFromItem(ModItems.COPPER_WIRE))
            .criterion("has_diluted_acid", conditionsFromItem(ModItems.DILUTED_ACID))
            .offerTo(exporter)

        offerSmelting(
            exporter,
            listOf(ModBlocks.LEAD_ORE, ModBlocks.DEEPSLATE_LEAD_ORE),
            RecipeCategory.MISC,
            ModItems.LEAD_INGOT,
            0.7f, 200,
            "smelt_lead_ore"
        )

        offerBlasting(
            exporter,
            listOf(ModBlocks.LEAD_ORE, ModBlocks.DEEPSLATE_LEAD_ORE),
            RecipeCategory.MISC,
            ModItems.LEAD_INGOT,
            0.4f, 100,
            "blast_lead_ore"
        )

        offerSmelting(
            exporter,
            listOf(ModItems.SULFUR_DUST),
            RecipeCategory.MISC,
            ModItems.SULFUR_DIOXIDE_BOTTLE,
            0.1f, 200,
            "smelt_sulfur_dust"
        )
    }
}