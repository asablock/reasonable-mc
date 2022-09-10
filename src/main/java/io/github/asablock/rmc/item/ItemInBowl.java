package io.github.asablock.rmc.item;

import io.github.asablock.rmc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class ItemInBowl extends Item {
    public ItemInBowl(Settings settings) {
        super(settings.recipeRemainder(Items.BOWL));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity)
            MCUtil.giveItem((PlayerEntity) user, Items.BOWL);
        return super.finishUsing(stack, world, user);
    }
}
