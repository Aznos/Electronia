package com.maddoxh.datagen

import com.maddoxh.registry.ModBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModLootTableGenerator(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
)
    : FabricBlockLootTableProvider(output, registriesFuture)
{
    override fun generate() {
        addDrop(ModBlocks.HAND_CRANK_GENERATOR)
        addDrop(ModBlocks.CRANK_PRESS)
        addDrop(ModBlocks.LEAD_ORE)
        addDrop(ModBlocks.DEEPSLATE_LEAD_ORE)
    }
}