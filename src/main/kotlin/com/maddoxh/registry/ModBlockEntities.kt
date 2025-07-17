package com.maddoxh.registry

import com.maddoxh.Electronia
import com.maddoxh.content.block.ChemistryTable
import com.maddoxh.content.block.entity.ChemistryTableBlockEntity
import com.maddoxh.content.block.entity.CrankPressBlockEntity
import com.maddoxh.content.block.entity.HandCrankGeneratorBlockEntity
import com.maddoxh.content.block.entity.HeaterBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.BlockEntityTickInvoker

object ModBlockEntities {
    val HAND_CRANK_GENERATOR: BlockEntityType<HandCrankGeneratorBlockEntity> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Electronia.MOD_ID, "hand_crank_generator"),
            BlockEntityType.Builder.create(::HandCrankGeneratorBlockEntity, ModBlocks.HAND_CRANK_GENERATOR).build()
        )

    val CRANK_PRESS: BlockEntityType<CrankPressBlockEntity> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Electronia.MOD_ID, "crank_press"),
            BlockEntityType.Builder.create(::CrankPressBlockEntity, ModBlocks.CRANK_PRESS).build()
        )

    val HEATER: BlockEntityType<HeaterBlockEntity> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Electronia.MOD_ID, "heater"),
            BlockEntityType.Builder.create(::HeaterBlockEntity, ModBlocks.HEATER).build()
        )

    val CHEMISTRY_TABLE: BlockEntityType<ChemistryTableBlockEntity> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Electronia.MOD_ID, "chemistry_table"),
            BlockEntityType.Builder.create(::ChemistryTableBlockEntity, ModBlocks.CHEMISTRY_TABLE).build()
        )

    fun register() {}
}