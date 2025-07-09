package com.maddoxh

import com.maddoxh.content.block.entity.renderer.CrankPressBlockEntityRenderer
import com.maddoxh.content.screen.CrankPressScreen
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModScreenHandlers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object ElectroniaClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CRANK_PRESS_SCREEN_HANDLER, ::CrankPressScreen)
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntities.CRANK_PRESS, ::CrankPressBlockEntityRenderer)

        ModelLoadingPlugin.register { ctx ->
            ctx.addModels(
                Identifier.of(Electronia.MOD_ID, "block/crank_press_piston")
            )
        }
    }
}