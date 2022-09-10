package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.RMCItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Settings.class)
public class MixinItemSettings implements RMCItem.Settings {
    @Shadow private FoodComponent foodComponent;

    @Unique
    private int rmc$moisture;

    @Override
    public Item.Settings rmc_drink(int moisture) {
        if (foodComponent != null) {
            throw new RuntimeException("Unable to have food component AND moisture.");
        } else {
            this.rmc$moisture = moisture;
            return (Item.Settings) (Object) this;
        }
    }

    @Override
    public int rmc_getMoisture() {
        return rmc$moisture;
    }

    @Inject(method = "food", at = @At("HEAD"))
    private void moistureCheckInFood(FoodComponent foodComponent, CallbackInfoReturnable<Item.Settings> cir) {
        if (rmc$moisture > 0) {
            throw new RuntimeException("Unable to have food component AND moisture.");
        }
    }
}
