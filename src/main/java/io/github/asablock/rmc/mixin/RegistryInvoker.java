package io.github.asablock.rmc.mixin;

import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(Registry.class)
public interface RegistryInvoker {
    @Invoker
    static <T> DefaultedRegistry<T> invokeCreate(RegistryKey<? extends Registry<T>> key, String defaultId,
                                                 Supplier<T> defaultEntry) {
        throw new AssertionError();
    }
}
