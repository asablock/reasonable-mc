package io.github.asablock.rmc;

import io.github.asablock.rmc.item.LockItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class KeyRecipe extends SpecialCraftingRecipe {
    public KeyRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {

        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < craftingInventory.size(); j++) {
            ItemStack itemStack2 = craftingInventory.getStack(j);
            if (!itemStack2.isEmpty()) {
                if (itemStack2.getItem() == ReasonableMCMod.ITEM_LOCK) {
                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    itemStack = itemStack2;
                } else {
                    if (itemStack2.getItem() != ReasonableMCMod.ITEM_KEY) {
                        return false;
                    }

                    i++;
                }
            }
        }

        return !itemStack.isEmpty() && i > 0;
    }

    @Override
    public ItemStack craft(CraftingInventory craftingInventory) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < craftingInventory.size(); j++) {
            ItemStack itemStack2 = craftingInventory.getStack(j);
            if (!itemStack2.isEmpty()) {
                if (itemStack2.getItem() == ReasonableMCMod.ITEM_LOCK) {
                    if (!itemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemStack = itemStack2;
                } else {
                    if (itemStack2.getItem() != ReasonableMCMod.ITEM_KEY) {
                        return ItemStack.EMPTY;
                    }

                    i++;
                }
            }
        }

        if (!itemStack.isEmpty() && i >= 1) {
            ItemStack itemStack3 = new ItemStack(ReasonableMCMod.ITEM_KEY);
            NbtCompound tag = itemStack3.getOrCreateTag();
            tag.putBoolean("Paired", true);
            tag.putUuid("PairedLockId", LockItem.getLockId(itemStack));
            itemStack3.setCount(i + 1);
            return itemStack3;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
