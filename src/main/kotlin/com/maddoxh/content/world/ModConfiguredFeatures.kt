package com.maddoxh.content.world

import com.maddoxh.Electronia
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig

object ModConfiguredFeatures {
    @Suppress("UnusedParameter")
    fun bootstrap(context: Registerable<ConfiguredFeature<*, *>>) {}

    fun registerKey(name: String): RegistryKey<ConfiguredFeature<*, *>> {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Electronia.MOD_ID, name))
    }

    private fun <FC : FeatureConfig, F : Feature<FC>> register(
        context: Registerable<ConfiguredFeature<*, *>>,
        key: RegistryKey<ConfiguredFeature<*, *>>, feature: F, configuration: FC
    ) {
        context.register(key, ConfiguredFeature<FC, F>(feature, configuration))
    }
}