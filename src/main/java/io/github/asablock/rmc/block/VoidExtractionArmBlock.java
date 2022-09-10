package io.github.asablock.rmc.block;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class VoidExtractionArmBlock extends Block {
    public VoidExtractionArmBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(VoidExtractorBlock.STAGE, VoidExtractorBlock.Stage.EXTRACTING));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VoidExtractorBlock.STAGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        scheduledTick(state, world, pos, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(VoidExtractorBlock.STAGE) == VoidExtractorBlock.Stage.EXTRACTING) {
            BlockPos downPos = pos.down();
            if (World.isOutOfBuildLimitVertically(downPos)) {
                notifyFinished(state, world, pos);
            } else if (world.getBlockState(downPos).isOf(ReasonableMCMod.BLOCK_VOID_FIELD)) {
                notifyFailed(state, world, pos);
            } else {
                world.setBlockState(downPos, getDefaultState());
            }
        }
    }

    static void notifyFixed(World world, BlockPos pos) {
        world.setBlockState(pos, ReasonableMCMod.BLOCK_VOID_FIELD.getDefaultState());
        BlockPos downPos = pos.down();
        if (!World.isOutOfBuildLimitVertically(downPos)) {
            BlockState state = world.getBlockState(downPos);
            if (state.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTION_ARM)) {
                notifyFixed(world, downPos);
            }
        }
    }

    private static void notifyFinished(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(VoidExtractorBlock.STAGE, VoidExtractorBlock.Stage.FINISHED));
        BlockPos upPos = pos.up();
        BlockState state2 = world.getBlockState(upPos);
        if (state2.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTOR)) {
            world.setBlockState(upPos, world.getBlockState(upPos).with(VoidExtractorBlock.STAGE, VoidExtractorBlock.Stage.FINISHED));
        } else if (state2.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTION_ARM)) {
            notifyFinished(world.getBlockState(upPos), world, upPos);
        }
    }

    private static void notifyFailed(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(VoidExtractorBlock.STAGE, VoidExtractorBlock.Stage.FAILED));
        BlockPos upPos = pos.up();
        BlockState state2 = world.getBlockState(upPos);
        if (state2.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTOR)) {
            world.setBlockState(upPos, world.getBlockState(upPos).with(VoidExtractorBlock.STAGE, VoidExtractorBlock.Stage.FAILED));
        } else if (state2.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTION_ARM)) {
            notifyFailed(world.getBlockState(upPos), world, upPos);
        }
    }
}