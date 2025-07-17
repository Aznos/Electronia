package com.maddoxh.registry

import com.maddoxh.Electronia
import com.maddoxh.content.screen.chemistryTable.ChemistryTableScreenHandler
import com.maddoxh.content.screen.crankpress.CrankPressScreenHandler
import com.maddoxh.content.screen.handCrankGenerator.HandCrankGeneratorScreenHandler
import com.maddoxh.content.screen.heater.HeaterScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object ModScreenHandlers {
    val CRANK_PRESS_SCREEN_HANDLER: ScreenHandlerType<CrankPressScreenHandler?> =
        Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Electronia.MOD_ID, "crank_press_screen_handler"),
            ExtendedScreenHandlerType<CrankPressScreenHandler?, BlockPos?>(
                { syncID: Int, playerInventory: PlayerInventory, pos: BlockPos? ->
                CrankPressScreenHandler(
                    syncID,
                    playerInventory,
                    pos
                )
            }, BlockPos.PACKET_CODEC)
        )

    val HAND_CRANK_GENERATOR_SCREEN_HANDLER: ScreenHandlerType<HandCrankGeneratorScreenHandler?> =
        Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Electronia.MOD_ID, "hand_crank_generator_screen_handler"),
            ExtendedScreenHandlerType<HandCrankGeneratorScreenHandler?, BlockPos?>(
                { syncID: Int, playerInventory: PlayerInventory, pos: BlockPos? ->
                HandCrankGeneratorScreenHandler(
                    syncID,
                    playerInventory,
                    pos
                )
            }, BlockPos.PACKET_CODEC)
        )

    val HEATER_SCREEN_HANDLER: ScreenHandlerType<HeaterScreenHandler?> =
        Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Electronia.MOD_ID, "heater_screen_handler"),
            ExtendedScreenHandlerType<HeaterScreenHandler?, BlockPos?>(
                { syncID: Int, playerInventory: PlayerInventory, pos: BlockPos? ->
                    HeaterScreenHandler(
                        syncID,
                        playerInventory,
                        pos
                    )
                }, BlockPos.PACKET_CODEC)
        )

    val CHEMISTRY_TABLE_SCREEN_HANDLER: ScreenHandlerType<ChemistryTableScreenHandler?> =
        Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(Electronia.MOD_ID, "chemistry_table_screen_handler"),
            ExtendedScreenHandlerType<ChemistryTableScreenHandler?, BlockPos?>(
                { syncID: Int, playerInventory: PlayerInventory, pos: BlockPos? ->
                    ChemistryTableScreenHandler(
                        syncID,
                        playerInventory,
                        pos
                    )
                }, BlockPos.PACKET_CODEC)
        )

    fun register() {}
}
