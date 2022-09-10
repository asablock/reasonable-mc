package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {
    @Shadow @Final public static IntProperty LEVEL;

    @Shadow public abstract void setLevel(World world, BlockPos pos, BlockState state, int level);

    @Inject(method = "onUse", cancellable = true, at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", shift = At.Shift.AFTER))
    private void onUse(BlockState state, World world, BlockPos pos,
                       PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        int i = state.get(LEVEL);
        if (item == Items.MILK_BUCKET) {
            if (i == 3 && !world.isClient()) {
                if (!player.abilities.creativeMode) {
                    itemStack.decrement(1);
                }
                MCUtil.giveItem(player, ReasonableMCMod.ITEM_WATERED_MILK_BUCKET);
                player.incrementStat(Stats.USE_CAULDRON);
                this.setLevel(world, pos, state, 0);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        } else if (item == ReasonableMCMod.ITEM_FLOUR_BAG) {
            if (i > 0 && !world.isClient()) {
                if (!player.abilities.creativeMode) {
                    itemStack.decrement(1);
                }
                MCUtil.giveItems(player, ReasonableMCMod.ITEM_BAG, ReasonableMCMod.ITEM_DOUGH);
                this.setLevel(world, pos, state, i - 1);
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        } else if (item == Items.OBSIDIAN) {
            if (i > 1 && !world.isClient()) {
                if (!player.abilities.creativeMode) {
                    itemStack.decrement(1);
                }
                MCUtil.giveItem(player, Items.CRYING_OBSIDIAN);
                this.setLevel(world, pos, state, i - 2);
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
    }
}
