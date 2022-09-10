package io.github.asablock.rmc.block;

import io.github.asablock.rmc.RMCTags;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class VoidExtractorBlock extends Block {
    public VoidExtractorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STAGE, Stage.EXTRACTING));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        scheduledTick(state, world, pos, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(STAGE) == Stage.EXTRACTING) {
            BlockPos downPos = pos.down();
            if (World.isOutOfBuildLimitVertically(downPos)) {
                world.setBlockState(pos, state.with(STAGE, Stage.FINISHED));
            } else if (RMCTags.VOID_EXTRACTOR_IMMUNE.contains(world.getBlockState(downPos).getBlock())) {
                world.setBlockState(pos, state.with(STAGE, Stage.FAILED));
            } else {
                world.setBlockState(downPos, ReasonableMCMod.BLOCK_VOID_EXTRACTION_ARM.getDefaultState());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        return dropItem(state, world, pos);
    }

    static ActionResult dropItem(BlockState state, World world, BlockPos pos) {
        if (!world.isClient) {
            if (state.get(STAGE) == Stage.FINISHED) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                Block.dropStack(world, pos, new ItemStack(ReasonableMCMod.ITEM_VOID_ESSENCE));
            } else if (state.get(STAGE) == Stage.FAILED) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                Block.dropStack(world, pos, new ItemStack(ReasonableMCMod.ITEM_VOID_EXTRACTOR));
            } else {
                return ActionResult.PASS;
            }
            notifyFixed(world, pos);
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    private static void notifyFixed(World world, BlockPos pos) {
        BlockPos downPos = pos.down();
        if (!World.isOutOfBuildLimitVertically(downPos)) {
            BlockState state = world.getBlockState(downPos);
            if (state.isOf(ReasonableMCMod.BLOCK_VOID_EXTRACTION_ARM)) {
                VoidExtractionArmBlock.notifyFixed(world, downPos);
            }
        }
    }

    public static final EnumProperty<Stage> STAGE = EnumProperty.of("stage", Stage.class);

    public enum Stage implements StringIdentifiable {
        EXTRACTING("extracting"),
        FINISHED("finished"),
        FAILED("failed");
        private final String stringValue;

        Stage(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public String asString() {
            return stringValue;
        }


        @Override
        public String toString() {
            return stringValue;
        }
    }
}
