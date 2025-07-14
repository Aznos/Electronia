package com.maddoxh.content.world

import com.maddoxh.Electronia
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.PlacedFeatures.register
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier
import net.minecraft.world.gen.placementmodifier.PlacementModifier
import java.util.List

object ModPlacedFeatures {
    val LEAD_ORE_UPPER_KEY = registerKey("lead_ore_upper")
    val LEAD_ORE_LOWER_KEY = registerKey("lead_ore_lower")

    fun bootstrap(context: Registerable<PlacedFeature>) {
        val cfg = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)

        register(
            context, registerKey("lead_ore_upper"),
            cfg.getOrThrow(ModConfiguredFeatures.LEAD_ORE_KEY),
            ModOrePlacement.modifiersWithCount(3, uniform(0, 48))
        )

        register(
            context, registerKey("lead_ore_lower"),
            cfg.getOrThrow(ModConfiguredFeatures.LEAD_ORE_KEY),
            ModOrePlacement.modifiersWithCount(2, uniform(-32, 0))
        )
    }

    fun registerKey(name: String): RegistryKey<PlacedFeature> {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Electronia.MOD_ID, name))
    }

    private fun register(
        context: Registerable<PlacedFeature>,
        key: RegistryKey<PlacedFeature>,
        configuration: RegistryEntry<ConfiguredFeature<*, *>>,
        modifiers: MutableList<PlacementModifier>
    ) {
        context.register(key, PlacedFeature(configuration, List.copyOf<PlacementModifier>(modifiers)))
    }

    private fun <FC : FeatureConfig, F : Feature<FC>> register(
        context: Registerable<PlacedFeature>, key: RegistryKey<PlacedFeature>,
        configuration: RegistryEntry<ConfiguredFeature<*, *>>,
        vararg modifiers: PlacementModifier
    ) {
        register(context, key, configuration, listOf(*modifiers))
    }

    private fun uniform(minY: Int, maxY: Int) = HeightRangePlacementModifier.uniform(YOffset.fixed(minY), YOffset.fixed(maxY))
}