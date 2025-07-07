package com.maddoxh.registry

import com.maddoxh.Electronia
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {
    val CRANK = Item(Item.Settings())

    fun register() {
        registerItem("crank", CRANK)
    }

    private fun registerItem(name: String, item: Item) {
        val id = Identifier.of(Electronia.MOD_ID, name)
        Registry.register(Registries.ITEM, id, item)
    }
}