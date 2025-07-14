package com.maddoxh.content.world

import com.maddoxh.Electronia
import com.maddoxh.registry.ModBlocks
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.structure.rule.TagMatchRuleTest
import net.minecraft.util.Identifier
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig

object ModConfiguredFeatures {
    val LEAD_ORE_KEY = registerKey("lead_ore")

    fun bootstrap(context: Registerable<ConfiguredFeature<*, *>>) {
        val stoneReplaceable = TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES)
        val deepslateReplaceable = TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)

        val leadOres: List<OreFeatureConfig.Target> = listOf(
            OreFeatureConfig.createTarget(stoneReplaceable, ModBlocks.LEAD_ORE.defaultState),
            OreFeatureConfig.createTarget(deepslateReplaceable, ModBlocks.DEEPSLATE_LEAD_ORE.defaultState)
        )

        register(context, LEAD_ORE_KEY, Feature.ORE, OreFeatureConfig(leadOres, 6))
    }

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