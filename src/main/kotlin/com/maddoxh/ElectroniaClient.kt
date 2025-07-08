package com.maddoxh

import com.maddoxh.content.screen.CrankPressScreen
import com.maddoxh.registry.ModScreenHandlers
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens

object ElectroniaClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CRANK_PRESS_SCREEN_HANDLER, ::CrankPressScreen)
    }
}