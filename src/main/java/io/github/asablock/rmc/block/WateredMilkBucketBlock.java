package io.github.asablock.rmc.block;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class WateredMilkBucketBlock extends Block {
    public WateredMilkBucketBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STAGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int stage = state.get(STAGE);
        if (stage < 3) {
            world.setBlockState(pos, state.with(STAGE, stage + 1));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            if (state.get(STAGE) < 3) return ActionResult.PASS;
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.getItem() == ReasonableMCMod.ITEM_BAG) {
                itemStack.decrement(1);
                player.giveItemStack(new ItemStack(ReasonableMCMod.ITEM_YEAST_BAG));
                player.giveItemStack(new ItemStack(Items.BUCKET));
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        } else {
            return ActionResult.CONSUME;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    public static final VoxelShape VOXEL_SHAPE = createCuboidShape(3, 0, 3, 13, 10, 13);
    public static final IntProperty STAGE = IntProperty.of("stage", 0, 3);
}
