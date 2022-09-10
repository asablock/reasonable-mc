package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.ReasonableMCMod;
import io.github.asablock.rmc.item.KettleItem;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public class MixinBeehiveBlock {
    @Shadow @Final public static IntProperty HONEY_LEVEL;

    @Inject(method = "onUse", at = @At("HEAD"))
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                       Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (state.get(HONEY_LEVEL) >= 5) {
            if (itemStack.getItem() == ReasonableMCMod.ITEM_KETTLE) {
                if (KettleItem.getFluid(itemStack) == KettleItem.Fluid.PURIFIED_WATER)
                    KettleItem.setFluid(itemStack, KettleItem.Fluid.HONEY_WATER);
            }
        }
    }
}