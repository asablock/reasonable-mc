package io.github.asablock.rmc;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public final class RMCFoodComponents {
    public static final FoodComponent NOODLES;
    public static final FoodComponent COOKED_NOODLES;
    public static final FoodComponent BUN;
    public static final FoodComponent COOKED_BUN;
    public static final FoodComponent CORN;

    private static FoodComponent.Builder builder() {
        return new FoodComponent.Builder();
    }

    static {
        NOODLES = builder().hunger(4).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER,
                30, 2), 1.0F).build();
        COOKED_NOODLES = builder().hunger(8).saturationModifier(0.6F).build();
        BUN = builder().hunger(4).saturationModifier(0.3F).build();
        COOKED_BUN = builder().hunger(10).saturationModifier(0.9F).build();
    }
}
