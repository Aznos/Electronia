package com.maddoxh

import com.maddoxh.content.world.gen.ModWorldGeneration
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModItemGroups
import com.maddoxh.registry.ModItems
import com.maddoxh.registry.ModRecipes
import com.maddoxh.registry.ModScreenHandlers
import com.maddoxh.registry.ModSounds
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Electronia : ModInitializer {
	const val MOD_ID = "electronia"
	@Suppress("Unused")
	val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ModItemGroups.register()
		ModItems.register()
		ModBlocks.register()
		ModBlockEntities.register()
		ModSounds.register()
		ModScreenHandlers.register()
		ModRecipes.register()
		ModWorldGeneration.register()
	}
}