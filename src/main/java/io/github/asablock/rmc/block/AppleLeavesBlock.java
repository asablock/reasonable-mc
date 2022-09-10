package io.github.asablock.rmc.block;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AppleLeavesBlock extends LeavesBlock {
    public static final IntProperty GROW_COOLDOWN = IntProperty.of("grow_cooldown", 0, 2);

    public AppleLeavesBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(GROW_COOLDOWN, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GROW_COOLDOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(GROW_COOLDOWN, 0);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return super.hasRandomTicks(state) || state.get(GROW_COOLDOWN) > 0;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (state.get(GROW_COOLDOWN) > 0) {
            int cooldown = state.get(GROW_COOLDOWN);
            if (cooldown == 1 && world.getBlockState(pos.down()).isAir()) {
                world.setBlockState(pos.down(), ReasonableMCMod.BLOCK_APPLE.getDefaultState());
            } else {
                world.setBlockState(pos, state.with(GROW_COOLDOWN, 1));
            }
        }
    }
}
