package com.maddoxh.datagen

import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerSimpleCubeAll(ModBlocks.LEAD_ORE)
        generator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_LEAD_ORE)
        generator.registerSimpleCubeAll(ModBlocks.SULFUR_ORE)
        generator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_SULFUR_ORE)
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(ModItems.CRANK, Models.GENERATED)
        generator.register(ModItems.COPPER_WIRE, Models.GENERATED)
        generator.register(ModItems.LEAD_INGOT, Models.GENERATED)
        generator.register(ModItems.SULFUR_DUST, Models.GENERATED)
        generator.register(ModItems.LEAD_PLATE, Models.GENERATED)
        generator.register(ModItems.SULFUR_DIOXIDE_BOTTLE, Models.GENERATED)
        generator.register(ModItems.DILUTED_ACID, Models.GENERATED)
    }
}