package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.item.AppleItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class MixinItems {
    @Shadow @Final @Mutable public static Item APPLE;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void redirectApple(CallbackInfo ci) {
        APPLE = MCUtil.registryReplace(Registry.ITEM, new Identifier("apple"),
                new AppleItem(new FabricItemSettings().group(ItemGroup.FOOD).food(FoodComponents.APPLE)));
    }
}
