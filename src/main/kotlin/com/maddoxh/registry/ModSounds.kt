package com.maddoxh.registry

import com.maddoxh.Electronia
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object ModSounds {
    val CRANK = registerSound("block.crank")

    fun register() {}

    private fun registerSound(name: String): SoundEvent {
        val id = Identifier.of(Electronia.MOD_ID, name)
        val event = SoundEvent.of(id)

        Registry.register(Registries.SOUND_EVENT, id, event)
        return event
    }
}