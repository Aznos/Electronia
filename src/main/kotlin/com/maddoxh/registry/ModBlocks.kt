package com.maddoxh.registry

import com.maddoxh.Electronia
import com.maddoxh.content.block.CrankPress
import com.maddoxh.content.block.HandCrankGenerator
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {
    val HAND_CRANK_GENERATOR = HandCrankGenerator(
        AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
    )

    val CRANK_PRESS = CrankPress(
        AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)
    )

    val LEAD_ORE = Block(
        AbstractBlock.Settings.copy(Blocks.IRON_ORE)
    )
    val DEEPSLATE_LEAD_ORE = Block(
        AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE)
    )

    val SULFUR_ORE = Block(
        AbstractBlock.Settings.copy(Blocks.COAL_ORE)
    )

    val DEEPSLATE_SULFUR_ORE = Block(
        AbstractBlock.Settings.copy(Blocks.DEEPSLATE_COAL_ORE)
    )

    fun register() {
        registerBlock("hand_crank_generator", HAND_CRANK_GENERATOR)
        registerBlock("crank_press", CRANK_PRESS)
        registerBlock("lead_ore", LEAD_ORE)
        registerBlock("deepslate_lead_ore", DEEPSLATE_LEAD_ORE)
        registerBlock("sulfur_ore", SULFUR_ORE)
        registerBlock("deepslate_sulfur_ore", DEEPSLATE_SULFUR_ORE)
    }

    private fun registerBlock(name: String, block: Block) {
        val id = Identifier.of(Electronia.MOD_ID, name)

        Registry.register(Registries.BLOCK, id, block)
        Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
    }
}