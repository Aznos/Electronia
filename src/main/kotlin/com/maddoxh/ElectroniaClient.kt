package com.maddoxh

import com.maddoxh.content.block.entity.renderer.CrankPressBlockEntityRenderer
import com.maddoxh.content.screen.chemistryTable.ChemistryTableScreen
import com.maddoxh.content.screen.crankpress.CrankPressScreen
import com.maddoxh.content.screen.handCrankGenerator.HandCrankGeneratorScreen
import com.maddoxh.content.screen.heater.HeaterScreen
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModBlocks
import com.maddoxh.registry.ModScreenHandlers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier

object ElectroniaClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CRANK_PRESS_SCREEN_HANDLER, ::CrankPressScreen)
        HandledScreens.register(ModScreenHandlers.HAND_CRANK_GENERATOR_SCREEN_HANDLER, ::HandCrankGeneratorScreen)
        HandledScreens.register(ModScreenHandlers.HEATER_SCREEN_HANDLER, ::HeaterScreen)
        HandledScreens.register(ModScreenHandlers.CHEMISTRY_TABLE_SCREEN_HANDLER, ::ChemistryTableScreen)

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntities.CRANK_PRESS, ::CrankPressBlockEntityRenderer)
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHEMISTRY_TABLE, RenderLayer.getTranslucent())

        ModelLoadingPlugin.register { ctx ->
            ctx.addModels(
                Identifier.of(Electronia.MOD_ID, "block/crank_press_piston")
            )
        }
    }
}