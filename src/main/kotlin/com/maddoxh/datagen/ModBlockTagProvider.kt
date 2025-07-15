package com.maddoxh.datagen

import com.maddoxh.registry.ModBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class ModBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture)
{
    override fun configure(wrapper: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(
                ModBlocks.CRANK_PRESS, ModBlocks.HAND_CRANK_GENERATOR,
                ModBlocks.LEAD_ORE, ModBlocks.DEEPSLATE_LEAD_ORE,
                ModBlocks.SULFUR_ORE, ModBlocks.DEEPSLATE_SULFUR_ORE,
            )
    }
}