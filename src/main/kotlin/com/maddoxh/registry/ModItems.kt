package com.maddoxh.registry

import com.maddoxh.Electronia
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {
    val CRANK = Item(Item.Settings())
    val COPPER_WIRE = Item(Item.Settings())
    val LEAD_INGOT = Item(Item.Settings())
    val LEAD_PLATE = Item(Item.Settings())
    val SULFUR_DUST = Item(Item.Settings())
    val SULFUR_DIOXIDE_BOTTLE = Item(Item.Settings())

    fun register() {
        registerItem("crank", CRANK)
        registerItem("copper_wire", COPPER_WIRE)
        registerItem("lead_ingot", LEAD_INGOT)
        registerItem("lead_plate", LEAD_PLATE)
        registerItem("sulfur_dust", SULFUR_DUST)
        registerItem("sulfur_dioxide_bottle", SULFUR_DIOXIDE_BOTTLE)
    }

    private fun registerItem(name: String, item: Item) {
        val id = Identifier.of(Electronia.MOD_ID, name)
        Registry.register(Registries.ITEM, id, item)
    }
}