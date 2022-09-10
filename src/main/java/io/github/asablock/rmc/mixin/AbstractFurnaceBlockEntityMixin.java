package io.github.asablock.rmc.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Shadow protected DefaultedList<ItemStack> inventory;

    @Inject(method = "craftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V",
            shift = At.Shift.BEFORE))
    private void craftRecipe(Recipe<?> recipe, CallbackInfo ci) {
        ItemStack itemStack = this.inventory.get(0);
        if (itemStack.getItem() == Blocks.CRYING_OBSIDIAN.asItem() &&
                !this.inventory.get(1).isEmpty() && this.inventory.get(1).getItem() == Items.BUCKET) {
            this.inventory.set(1, new ItemStack(Items.WATER_BUCKET));
        }
    }
}
