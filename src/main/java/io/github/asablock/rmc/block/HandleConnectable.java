package io.github.asablock.rmc.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Deprecated
public interface HandleConnectable {
    /**
     * @apiNote 前3个参数均为连接到手柄的方块的状态，而非手柄的状态。{@code hitResult}参数是玩家点击手柄的结果。
     */
    ActionResult onUseHandle(BlockState state, BlockPos pos, Direction direction, World world,
                             PlayerEntity player, Hand hand, BlockHitResult hitResult);

    void onConnectHandle(BlockState state, BlockPos pos, World world, Direction direction);

    void onDisconnectHandle(BlockState state, BlockPos pos, World world, Direction direction);

    void setDirection(BlockState state, BlockPos pos, World world, Direction direction);

    boolean isHandleConnected(BlockState state, BlockPos pos, BlockView world);

    VoxelShape getHandleOutlineShape(BlockState state, BlockPos pos, BlockView world);

    default void rotateHandle(BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        Direction direction = state.get(HandleBlock.FACING);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        Direction direction2 = direction.rotateYClockwise();
        BlockPos thisPos = pos.offset(direction);
        world.setBlockState(thisPos.offset(direction2), state.with(HandleBlock.FACING, direction2));
    }
}
