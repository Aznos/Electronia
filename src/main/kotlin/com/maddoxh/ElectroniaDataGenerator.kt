package com.maddoxh

import com.maddoxh.datagen.ModBlockTagProvider
import com.maddoxh.datagen.ModItemTagProvider
import com.maddoxh.datagen.ModLootTableGenerator
import com.maddoxh.datagen.ModModelProvider
import com.maddoxh.datagen.ModRecipeGenerator
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object ElectroniaDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack = fabricDataGenerator.createPack()

		pack.addProvider(::ModBlockTagProvider)
		pack.addProvider(::ModItemTagProvider)
		pack.addProvider(::ModLootTableGenerator)
		pack.addProvider(::ModModelProvider)
		pack.addProvider(::ModRecipeGenerator)
	}
}