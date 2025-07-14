package com.maddoxh.content.world

import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier
import net.minecraft.world.gen.placementmodifier.PlacementModifier
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier

object ModOrePlacement {
    fun modifiers(
        countModifier: PlacementModifier,
        heightModifier: PlacementModifier
    ): MutableList<PlacementModifier> {
        return listOf<PlacementModifier>(
            countModifier,
            SquarePlacementModifier.of(),
            heightModifier,
            BiomePlacementModifier.of()
        ) as MutableList<PlacementModifier>
    }

    fun modifiersWithCount(count: Int, heightModifier: PlacementModifier): MutableList<PlacementModifier> {
        return modifiers(CountPlacementModifier.of(count), heightModifier)
    }

    fun modifiersWithRarity(chance: Int, heightModifier: PlacementModifier): MutableList<PlacementModifier> {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier)
    }
}