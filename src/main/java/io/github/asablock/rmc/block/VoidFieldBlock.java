package io.github.asablock.rmc.block;

import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VoidFieldBlock extends Block {
    public VoidFieldBlock(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            MCUtil.kill(player, ReasonableMCMod.INTO_VOID);
        }
        return ActionResult.SUCCESS;
    }
}
