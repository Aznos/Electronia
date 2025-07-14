package com.maddoxh.content.world

import net.minecraft.world.gen.placementmodifier.*

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