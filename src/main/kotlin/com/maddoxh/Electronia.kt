package com.maddoxh

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Electronia : ModInitializer {
    private val logger = LoggerFactory.getLogger("electronia")

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}