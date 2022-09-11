package io.github.asablock.rmc.block;

import io.github.asablock.rmc.mixin.CropBlockInvoker;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Random;

public class TallCropBlock extends TallPlantBlock implements Fertilizable {
    public static final IntProperty AGE = Properties.AGE_2;

    public TallCropBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(getAgeProperty());
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.FARMLAND);
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }
    
    public int getMaxAge() {
        return 2;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER && state.get(AGE) < getMaxAge();
    }

    public boolean isMature(BlockState state) {
        return state.get(AGE) >= getMaxAge();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = state.get(AGE);
            if (i < getMaxAge()) {
                float f = CropBlockInvoker.invokeGetAvailableMoisture(this, world, pos);
                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    growDouble(world, pos, state, i + 1);
                }
            }
        }
    }

    public static void growDouble(World world, BlockPos pos, BlockState state, int to) {
        world.setBlockState(pos, state.with(AGE, to), 2);
        world.setBlockState(pos.up(), world.getBlockState(pos.up()).with(AGE, to), 2);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !isMature(state);
    }

    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            world.breakBlock(pos, true, entity);
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            int i = Math.min(state.get(AGE) + this.getGrowthAmount(world), getMaxAge());

            growDouble(world, pos, state, i);
        } else {
            grow(world, random, pos.down(), world.getBlockState(pos));
        }
    }
}
