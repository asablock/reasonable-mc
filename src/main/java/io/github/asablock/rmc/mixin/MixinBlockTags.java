package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.RMCTags;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockTags.class)
public class MixinBlockTags {
    @Shadow
    private static Tag.Identified<Block> register(String id) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void rmc_clinit(CallbackInfo ci) {
        RMCTags.VOID_EXTRACTOR_IMMUNE = register("reasonable-mc:void_extractor_immune");
    }
}
