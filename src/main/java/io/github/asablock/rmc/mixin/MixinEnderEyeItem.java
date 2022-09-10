package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.block.RMCPortalFrameBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderEyeItem.class)
public class MixinEnderEyeItem {
    @Redirect(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"))
    private <T extends Comparable<T>, V extends T> Object with(BlockState instance, Property<T> property, V value) {
        return instance.with(property, value).with(RMCPortalFrameBlock.REMOVABLE, false);
    }
}