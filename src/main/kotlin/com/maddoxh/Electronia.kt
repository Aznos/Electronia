package com.maddoxh

import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModItemGroups
import com.maddoxh.registry.ModSounds
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Electronia : ModInitializer {
	const val MOD_ID = "electronia"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ModItemGroups.register()
		ModBlocks.register()
		ModBlockEntities.register()
		ModSounds.register()
	}
}