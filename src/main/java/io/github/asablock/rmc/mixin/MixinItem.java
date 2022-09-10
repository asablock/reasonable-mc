package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.RMCItem;
import io.github.asablock.rmc.RMCLivingE;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem implements RMCItem {
    @Unique
    private int rmc_moisture;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Item.Settings settings, CallbackInfo ci) {
        rmc_moisture = ((Settings) settings).rmc_getMoisture();
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishDrinking(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (rmc_isDrink()) {
            cir.setReturnValue(((RMCLivingE) user).drink(world, stack));
        }
    }

    @Override
    public int rmc_getMoisture() {
        return rmc_moisture;
    }

    @Override
    public boolean rmc_isDrink() {
        return rmc_moisture > 0;
    }
}