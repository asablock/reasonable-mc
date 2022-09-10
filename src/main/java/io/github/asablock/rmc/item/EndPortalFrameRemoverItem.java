package io.github.asablock.rmc.item;

import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.block.RMCPortalFrameBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;

public class EndPortalFrameRemoverItem extends Item {
    public EndPortalFrameRemoverItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
            if (blockState.getBlock() == Blocks.END_PORTAL_FRAME) {
                if (blockState.get(RMCPortalFrameBlock.REMOVABLE)) {
                    if (context.getWorld().canPlayerModifyAt(context.getPlayer(), context.getBlockPos())) {
                        context.getWorld().setBlockState(context.getBlockPos(), Blocks.AIR.getDefaultState());
                        MCUtil.giveItem(context.getPlayer(), Items.END_PORTAL_FRAME);
                        MCUtil.damage(context.getPlayer(), context.getHand());
                        return ActionResult.SUCCESS;
                    }
                } else {
                    context.getPlayer().sendMessage(new TranslatableText("text.reasonable-mc.cannot_remove_portal_frame"),
                            true);
                }
            }
            return ActionResult.PASS;
        }
        return ActionResult.SUCCESS;
    }
}