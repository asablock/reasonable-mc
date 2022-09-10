package io.github.asablock.rmc.block;

import io.github.asablock.rmc.block.entity.ChoppingBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ChoppingBlock extends BlockWithEntity {
    public ChoppingBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            return ((ChoppingBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).onUse(player, hand);
        } else {
            return ActionResult.SUCCESS;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ChoppingBlockEntity.isFilledChoppingBlock(world, pos) ? FILLED_SHAPE : EMPTY_SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static final VoxelShape EMPTY_SHAPE = createCuboidShape(0, 0, 0, 16, 1, 16);
    public static final VoxelShape FILLED_SHAPE = createCuboidShape(0, 0, 0, 16, 2, 16);

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ChoppingBlockEntity();
    }
}
