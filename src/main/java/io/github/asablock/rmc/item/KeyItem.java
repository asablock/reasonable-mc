package io.github.asablock.rmc.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class KeyItem extends Item {
    public KeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        NbtCompound tag = stack.getOrCreateTag();
        if (!tag.getBoolean("Paired")) {
            tag.putBoolean("Paired", false);
        }
    }

    public static UUID getKeyId(ItemStack stack) {
        if (stack.getItem() instanceof LockItem) {
            return stack.getTag().getUuid("PairedLockId");
        } else {
            return null;
        }
    }

    public static boolean isPaired(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Paired");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!isPaired(stack)) {
            tooltip.add(new TranslatableText("item.reasonable-mc.key.unpaired"));
        } else if (context.isAdvanced()) {
            tooltip.add(new TranslatableText("item.reasonable-mc.key.paired", getKeyId(stack)));
        }
    }
}
