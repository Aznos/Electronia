package com.maddoxh.datagen

import com.maddoxh.registry.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {

    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(ModItems.CRANK, Models.GENERATED)
        generator.register(ModItems.COPPER_WIRES, Models.GENERATED)
    }
}