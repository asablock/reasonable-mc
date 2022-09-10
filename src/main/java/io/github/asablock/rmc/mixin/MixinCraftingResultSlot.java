package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingResultSlot.class)
public class MixinCraftingResultSlot {
    @Redirect(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingInventory;removeStack(II)Lnet/minecraft/item/ItemStack;"))
    private ItemStack remainLockItem(CraftingInventory instance, int slot, int amount) {
        if (instance.getStack(slot).getItem() == ReasonableMCMod.ITEM_LOCK) {
            return instance.getStack(slot); // Make reasonable-mc:lock item will not be removed when pairing a key
        } else {
            return instance.removeStack(slot, amount);
        }
    }
}
