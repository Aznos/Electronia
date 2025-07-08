package com.maddoxh.registry

import com.maddoxh.Electronia
import com.maddoxh.content.screen.CrankPressScreenHandler
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
            ExtendedScreenHandlerType<CrankPressScreenHandler?, BlockPos?>({ syncID: Int, playerInventory: PlayerInventory, pos: BlockPos? ->
                CrankPressScreenHandler(
                    syncID,
                    playerInventory,
                    pos
                )
            }, BlockPos.PACKET_CODEC)
        )

    fun register() {}
}
