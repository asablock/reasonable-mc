package io.github.asablock.rmc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class RMCStatusEffects {
    public static final StatusEffect DRYNESS;

    static void register() {
        register(DRYNESS, "dryness");
    }

    private static void register(StatusEffect statusEffect, String path) {
        Registry.register(Registry.STATUS_EFFECT, ReasonableMCMod.identifier(path), statusEffect);
    }

    static {
        DRYNESS = new StatusEffect(StatusEffectType.HARMFUL, 0xD38B24) {
            @Override
            public boolean canApplyUpdateEffect(int duration, int amplifier) {
                int k = 2560 >> amplifier;
                if (k > 0) {
                    return duration % k == 0;
                } else {
                    return true;
                }
            }

            @Override
            public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                entity.damage(ReasonableMCMod.TOO_DRY, 4.0F);
            }
        };
    }
}
