package com.maddoxh

import com.maddoxh.registry.ModBlocks
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Electronia : ModInitializer {
	const val MOD_ID = "electronia"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ModBlocks.register()
	}
}