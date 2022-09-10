package io.github.asablock.rmc;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Supplier;

public class MCUtil {
    public static void damage(LivingEntity livingEntity, Hand hand, int amount) {
        livingEntity.getStackInHand(hand).damage(amount, livingEntity, e -> e.sendToolBreakStatus(hand));
    }

    public static void damage(LivingEntity livingEntity, Hand hand) {
        damage(livingEntity, hand, 1);
    }

    public static void giveItem(PlayerEntity player, ItemStack itemStack) {
        if (!player.inventory.insertStack(itemStack)) {
            player.dropItem(itemStack, false);
        }
    }

    public static boolean oneHasStack(ScreenHandler handler, int fromInclusive, int toExclusive) {
        for (int i = fromInclusive; i < toExclusive; i++) {
            if (handler.getSlot(i).hasStack()) return true;
        }
        return false;
    }

    public static boolean hasStacks(ScreenHandler handler, int fromInclusive, int toExclusive) {
        for (int i = fromInclusive; i < toExclusive; i++) {
            if (!handler.getSlot(i).hasStack()) return false;
        }
        return true;
    }

    public static <T, V extends T> V registryReplace(MutableRegistry<T> registry, Identifier identifier, V entry) {
        return registry.replace(OptionalInt.of(registry.getRawId(registry.get(identifier))),
                RegistryKey.of(registry.getKey(), identifier),
                entry, Lifecycle.stable());
    }

    /**
     * <p>Percent <= 0 means <b>never</b>.</p>
     * <p>Percent >= 100 means <b>always</b>.</p>
     */
    public static boolean chance(@Range(from = 0, to = 100) int percent, Random random) {
        return random.nextInt(100) < percent;
    }

    /**
     * <p>Percent <= 0 means <b>never</b>.</p>
     * <p>Percent >= 100 means <b>always</b>.</p>
     */
    public static boolean chance(@Range(from = 0, to = 100) int percent) {
        return chance(percent, new Random());
    }

    @NotNull
    public static <T, R, E extends Throwable> R orDefault(@Nullable T value,
                                     @NotNull ExceptionFunction<? super T, ? extends @NotNull R, E> function,
                                     @NotNull R defaultValue) throws E {
        Validate.notNull(function, "function and defaultValue must be non-null values", defaultValue);
        if (value == null) {
            return defaultValue;
        } else {
            R result = function.apply(value);
            Validate.notNull(result, "function result must be not null");
            return result;
        }
    }

    @FunctionalInterface
    public interface ExceptionFunction<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    public static void giveItem(PlayerEntity player, Item item, int count) {
        giveItem(player, new ItemStack(item, count));
    }

    public static void giveItem(PlayerEntity player, Item item) {
        giveItem(player, item, 1);
    }

    public static void giveItems(PlayerEntity player, @NotNull Item... items) {
        Objects.requireNonNull(items, "items");
        for (Item item : items) {
            giveItem(player, item);
        }
    }

    public static void giveItems(PlayerEntity player, Iterable<ItemStack> itemStacks) {
        Objects.requireNonNull(itemStacks);
        for (ItemStack itemStack : itemStacks) {
            giveItem(player, itemStack);
        }
    }

    public static boolean inventoryRemoveOne(@NotNull Inventory inventory, @Nullable Item item) {
        Objects.requireNonNull(inventory, "inventory");
        if (item == null || item == Items.AIR) return true;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }

    public static ItemStack newDamagedItemStack(Item item, int damage) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.setDamage(damage);
        return itemStack;
    }

    public static <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(Supplier<T> supplier, Block... blocks) {
        return BlockEntityType.Builder.create(supplier, blocks).build(null);
    }

    public static ArmorMaterial getArmorMaterial(@Nullable Item item) {
        if (item instanceof ArmorItem) {
            return ((ArmorItem) item).getMaterial();
        } else {
            return null;
        }
    }

    public static ToolMaterial getToolMaterial(@Nullable Item item) {
        if (item instanceof ToolItem) {
            return ((ToolItem) item).getMaterial();
        } else {
            return null;
        }
    }

    public static boolean isMaterial(Item item, ArmorMaterial material) {
        return getArmorMaterial(item) == material;
    }

    public static boolean isMaterial(Item item, ToolMaterial material) {
        return getToolMaterial(item) == material;
    }

    public static int getDrynessAmplifier(LivingEntity entity) {
        int amplifier = 8;
        if (isMaterial(entity.getEquippedStack(EquipmentSlot.HEAD).getItem(), RMCMaterials.ARMOR_ICE))
            amplifier -= 2;
        if (isMaterial(entity.getEquippedStack(EquipmentSlot.CHEST).getItem(), RMCMaterials.ARMOR_ICE))
            amplifier -= 2;
        if (isMaterial(entity.getEquippedStack(EquipmentSlot.LEGS).getItem(), RMCMaterials.ARMOR_ICE))
            amplifier -= 2;
        if (isMaterial(entity.getEquippedStack(EquipmentSlot.FEET).getItem(), RMCMaterials.ARMOR_ICE))
            amplifier -= 2;
        return amplifier;
    }

    public static void kill(LivingEntity entity, DamageSource damageSource) {
        entity.damage(damageSource, Float.MAX_VALUE);
    }
}
