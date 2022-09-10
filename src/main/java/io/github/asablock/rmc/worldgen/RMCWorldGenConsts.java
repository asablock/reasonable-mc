package io.github.asablock.rmc.worldgen;

import com.google.common.collect.ImmutableList;
import io.github.asablock.rmc.ReasonableMCMod;
import io.github.asablock.rmc.mixin.DefaultBiomeCreatorInvoker;
import io.github.asablock.rmc.mixin.TreeDecoratorTypeInvoker;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.awt.*;
import java.util.function.Predicate;

import static io.github.asablock.rmc.ReasonableMCMod.identifier;

public class RMCWorldGenConsts {
    public static final TreeDecoratorType<AppleTreeDecorator> APPLE;
    public static final RegistryKey<Biome> BIOME_APPLE_FOREST = RegistryKey.of(Registry.BIOME_KEY, identifier("apple_forest"));
    public static final ConfiguredFeature<?, ?> TREE_APPLE =
            Feature.TREE
                    .configure(new TreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
                            new SimpleBlockStateProvider(ReasonableMCMod.BLOCK_APPLE_LEAVES.getDefaultState()),
                            new BlobFoliagePlacer(UniformIntDistribution.of(2, 1), UniformIntDistribution.of(0), 3),
                            new StraightTrunkPlacer(6, 2, 0),
                            new TwoLayersFeatureSize(1, 0, 1)
                    ).decorators(ImmutableList.of(AppleTreeDecorator.INSTANCE)).build())
                    .applyChance(6);
    public static final ConfiguredFeature<?, ?> TREE_FOREST_APPLE =
            Feature.TREE
                    .configure(new TreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
                            new SimpleBlockStateProvider(ReasonableMCMod.BLOCK_APPLE_LEAVES.getDefaultState()),
                            new BlobFoliagePlacer(UniformIntDistribution.of(2, 1), UniformIntDistribution.of(0), 3),
                            new StraightTrunkPlacer(6, 2, 0),
                            new TwoLayersFeatureSize(1, 0, 1)
                    ).decorators(ImmutableList.of(AppleTreeDecorator.INSTANCE)).build())
                    .repeat(20)
                    .applyChance(2);

    @SuppressWarnings("deprecation")
    public static void register() {
        Registry.register(BuiltinRegistries.BIOME, BIOME_APPLE_FOREST.getValue(), createAppleForest());

        @SuppressWarnings("deprecation")
        Predicate<BiomeSelectionContext> overworld = BiomeSelectors.foundInOverworld();

        addConfiguredFeature(TREE_APPLE, "apple_tree", GenerationStep.Feature.VEGETAL_DECORATION,
                BiomeSelectors.includeByKey(BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.WOODED_HILLS,
                        BiomeKeys.BIRCH_FOREST));
        addConfiguredFeature(TREE_FOREST_APPLE, "apple_tree_forest", GenerationStep.Feature.VEGETAL_DECORATION,
                BiomeSelectors.includeByKey(BIOME_APPLE_FOREST));

        OverworldBiomes.addContinentalBiome(BIOME_APPLE_FOREST, OverworldClimate.TEMPERATE, 3D);
    }

    @SuppressWarnings("deprecation")
    private static void addConfiguredFeature(ConfiguredFeature<?, ?> feature, String id, GenerationStep.Feature step,
                                             Predicate<BiomeSelectionContext> biomeSelector) {
        RegistryKey<ConfiguredFeature<?, ?>> featureKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                ReasonableMCMod.identifier(id));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, featureKey.getValue(), feature);
        BiomeModifications.addFeature(biomeSelector, step, featureKey);
    }

    // Biomes
    private static Biome createAppleForest() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addDefaultLakes(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);
        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        DefaultBiomeFeatures.addDefaultDisks(generationSettings);
        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .depth(0.1F)
                .scale(0.2F)
                .temperature(0.7F)
                .downfall(0.7F)
                .effects(new BiomeEffects.Builder()
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .grassColor(0x6fb728)
                        .fogColor(0xc0d8ff)
                        .skyColor(DefaultBiomeCreatorInvoker.invokeGetSkyColor(0.7F))
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    static {
        APPLE = TreeDecoratorTypeInvoker.invokeRegister("reasonable-mc:apple", AppleTreeDecorator.CODEC);
    }
}