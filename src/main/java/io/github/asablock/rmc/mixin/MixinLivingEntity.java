package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements RMCLivingE {
    @Shadow public abstract Hand getActiveHand();

    @Inject(method = "tick", at = @At("HEAD"))
    private void rmc_tick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!RMCTags.DRY_ENDURING.contains(self.getType())) {
            Biome biome = self.world.getBiome(self.getBlockPos());
            if (biome.getTemperature() >= 1.5) {
                self.applyStatusEffect(new StatusEffectInstance(RMCStatusEffects.DRYNESS, 120,
                        MCUtil.getDrynessAmplifier(self)));
            }
        }
    }

    @Override
    public ItemStack drink(World world, ItemStack stack) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (((RMCItem) stack.getItem()).rmc_isDrink()) {
            world.playSound(null, self.getX(), self.getY(), self.getZ(),
                    SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F,
                    1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            if (!(self instanceof PlayerEntity) || !((PlayerEntity) self).abilities.creativeMode) {
                if (stack.isDamageable()) {
                    MCUtil.damage(self, getActiveHand());
                } else {
                    stack.decrement(1);
                }
            }
        }
        return stack;
    }
}