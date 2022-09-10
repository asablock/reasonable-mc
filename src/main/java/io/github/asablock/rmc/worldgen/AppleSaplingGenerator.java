package io.github.asablock.rmc.worldgen;

import com.google.common.collect.ImmutableList;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.Random;

public class AppleSaplingGenerator extends SaplingGenerator {
    private static final ConfiguredFeature<TreeFeatureConfig, ?> SAMPLE =
            Feature.TREE
                    .configure(new TreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
                            new SimpleBlockStateProvider(ReasonableMCMod.BLOCK_APPLE_LEAVES.getDefaultState()),
                            new BlobFoliagePlacer(UniformIntDistribution.of(2, 1), UniformIntDistribution.of(0), 3),
                            new StraightTrunkPlacer(6, 2, 0),
                            new TwoLayersFeatureSize(1, 0, 1)
                    ).decorators(ImmutableList.of(AppleTreeDecorator.INSTANCE)).build());

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bees) {
        return SAMPLE;
    }
}
