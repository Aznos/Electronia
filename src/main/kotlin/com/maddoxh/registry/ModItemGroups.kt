package com.maddoxh.registry

import com.maddoxh.Electronia
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ModItemGroups {
    val ELECTRONIA_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(Electronia.MOD_ID, "electronia"))
    val ELECTRONIA_GROUP: ItemGroup? = FabricItemGroup.builder()
        .icon { ItemStack(ModBlocks.HAND_CRANK_GENERATOR) }
        .displayName(Text.translatable("itemGroup.electronia"))
        .build()

    fun register() {
        Registry.register(Registries.ITEM_GROUP, ELECTRONIA_GROUP_KEY, ELECTRONIA_GROUP)
        ItemGroupEvents.modifyEntriesEvent(ELECTRONIA_GROUP_KEY).register { itemGroup ->
            itemGroup.add(ModBlocks.HAND_CRANK_GENERATOR)
        }
    }
}