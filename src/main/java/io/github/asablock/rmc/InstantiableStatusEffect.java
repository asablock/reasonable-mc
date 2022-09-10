package io.github.asablock.rmc;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class InstantiableStatusEffect extends StatusEffect {
    public InstantiableStatusEffect(StatusEffectType type, int color) {
        super(type, color);
    }
}
