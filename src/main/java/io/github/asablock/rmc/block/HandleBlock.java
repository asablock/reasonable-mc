package io.github.asablock.rmc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class HandleBlock extends Block {
    public HandleBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private static boolean canConnect(BlockPos pos, BlockView world) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof HandleConnectable && !((HandleConnectable) block).isHandleConnected(state, pos, world);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        for (Direction value : Direction.values()) {
            if (value.getHorizontal() != -1 && canConnect(blockPos.offset(value), world)) {
                return getDefaultState().with(FACING, value);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        for (Direction value : Direction.values()) {
            if (value.getHorizontal() != -1 && canConnect(pos.offset(value), world)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof HandleConnectable) {
            HandleConnectable hc = (Block & HandleConnectable) block;
            hc.onConnectHandle(state, pos.offset(direction), world, direction.getOpposite());
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        Direction direction = state.get(FACING);
        BlockPos connectedPos = pos.offset(direction);
        BlockState connectedState = world.getBlockState(connectedPos);
        Block connectedBlock = connectedState.getBlock();
        if (connectedBlock instanceof HandleConnectable) {
            HandleConnectable hc = (Block & HandleConnectable) connectedBlock;
            hc.onDisconnectHandle(state, pos, world, direction);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            BlockPos pos2 = pos.offset(state.get(FACING));
            BlockState blockState = world.getBlockState(pos2);
            Block block = blockState.getBlock();
            if (block instanceof HandleConnectable) {
                return ((HandleConnectable) block).onUseHandle(blockState, pos2, state.get(FACING).getOpposite(), world, player, hand, hit);
            }
            return ActionResult.PASS;
        } else {
            return ActionResult.SUCCESS;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockPos pos2 = pos.offset(state.get(FACING));
        return pos2 instanceof HandleConnectable ?
                ((HandleConnectable) pos2).getHandleOutlineShape(world.getBlockState(pos2), pos2, world) :
                VoxelShapes.empty();
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
}
