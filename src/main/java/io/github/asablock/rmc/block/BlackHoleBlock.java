package io.github.asablock.rmc.block;

import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class BlackHoleBlock extends Block {
    public BlackHoleBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            world.getBlockTickScheduler().schedule(pos, state.getBlock(),
                    600 - state.get(AGE) * 29, TickPriority.VERY_HIGH);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient) {
            expand(state, world, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return true;
    }

    private void expand(BlockState state, ServerWorld world, BlockPos pos) {
        for (Direction direction : DIRECTIONS) {
            BlockPos pos2 = pos.offset(direction);
            if (!world.getBlockState(pos2).isOf(ReasonableMCMod.BLOCK_BLACK_HOLE)) {
                world.setBlockState(pos2, getDefaultState().with(AGE, Math.min(state.get(AGE) + 1, 20)));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("block.reasonable-mc.black_hole.warning.t1"));
        tooltip.add(new TranslatableText("block.reasonable-mc.black_hole.warning.t2"));
        tooltip.add(new TranslatableText("block.reasonable-mc.black_hole.warning.t3"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            MCUtil.kill((LivingEntity) entity, ReasonableMCMod.INTO_VOID);
        } else {
            entity.remove();
        }
    }

    public static final IntProperty AGE = IntProperty.of("age", 0, 20);
}
