package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.RMCTags;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTypeTags.class)
public abstract class MixinEntityTypeTags {
    @Shadow
    private static Tag.Identified<EntityType<?>> register(String id) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void rmc_clinit(CallbackInfo ci) {
        RMCTags.DRY_ENDURING = register("reasonable-mc:dry_enduring");
    }
}