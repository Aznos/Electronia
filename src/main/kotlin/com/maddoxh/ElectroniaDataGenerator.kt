package com.maddoxh

import com.maddoxh.content.world.ModConfiguredFeatures
import com.maddoxh.content.world.ModPlacedFeatures
import com.maddoxh.datagen.ModBlockTagProvider
import com.maddoxh.datagen.ModItemTagProvider
import com.maddoxh.datagen.ModLootTableGenerator
import com.maddoxh.datagen.ModModelProvider
import com.maddoxh.datagen.ModRecipeGenerator
import com.maddoxh.datagen.ModWorldGenerator
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys

object ElectroniaDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack = fabricDataGenerator.createPack()

		pack.addProvider(::ModBlockTagProvider)
		pack.addProvider(::ModItemTagProvider)
		pack.addProvider(::ModLootTableGenerator)
		pack.addProvider(::ModModelProvider)
		pack.addProvider(::ModRecipeGenerator)
		pack.addProvider(::ModWorldGenerator)
	}

	override fun buildRegistry(registryBuilder: RegistryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
	}
}