package io.github.asablock.rmc.block;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Random;

public class WhiteHoleBlock extends Block {
    public WhiteHoleBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            world.getBlockTickScheduler().schedule(pos, state.getBlock(), 1, TickPriority.EXTREMELY_HIGH);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient) {
            expand(world, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        switch (type) {
            case LAND:
            case AIR:
                return true;
            default:
                return false;
        }
    }

    private void expand(ServerWorld world, BlockPos pos) {
        for (Direction direction : DIRECTIONS) {
            BlockPos pos2 = pos.offset(direction);
            if (!world.getBlockState(pos2).isOf(ReasonableMCMod.BLOCK_BLACK_HOLE)) {
                world.setBlockState(pos2, getDefaultState());
            }
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}