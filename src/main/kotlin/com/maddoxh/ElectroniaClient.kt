package com.maddoxh

import com.maddoxh.content.block.entity.renderer.CrankPressBlockEntityRenderer
import com.maddoxh.content.screen.CrankPressScreen
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModScreenHandlers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreens

object ElectroniaClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CRANK_PRESS_SCREEN_HANDLER, ::CrankPressScreen)
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntities.CRANK_PRESS, ::CrankPressBlockEntityRenderer)
    }
}