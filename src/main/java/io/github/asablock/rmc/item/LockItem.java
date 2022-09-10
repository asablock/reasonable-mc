package io.github.asablock.rmc.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class LockItem extends Item {
    public LockItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        NbtCompound tag = stack.getOrCreateTag();
        tag.putUuid("LockId", UUID.randomUUID());
    }

    public static UUID getLockId(ItemStack stack) {
        if (stack.getItem() instanceof LockItem) {
            return stack.getTag().getUuid("LockId");
        } else {
            return null;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (context.isAdvanced()) {
            tooltip.add(new LiteralText("ID: " + getLockId(stack)));
        }
    }
}
