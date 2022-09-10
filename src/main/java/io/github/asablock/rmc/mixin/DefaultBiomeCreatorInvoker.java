package io.github.asablock.rmc.mixin;

import net.minecraft.world.biome.DefaultBiomeCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DefaultBiomeCreator.class)
public interface DefaultBiomeCreatorInvoker {
    @Invoker
    static int invokeGetSkyColor(float temperature) {
        throw new AssertionError();
    }
}
