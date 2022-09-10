package io.github.asablock.rmc.item;

import com.google.common.collect.ImmutableList;
import io.github.asablock.rmc.DrinkingManager;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.List;

public class KettleItem extends Item {
    public KettleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            NbtCompound nbt = stack.getOrCreateTag();
            if (user instanceof PlayerEntity) {
                Fluid fluid = Fluid.values()[nbt.getInt("Fluid")];
                ((PlayerEntity) user).getHungerManager().add(fluid.getFood(), 0);
                ((DrinkingManager.Getter) user).rmc_getDrinkingManager().add(fluid.getMoisture());
                for (StatusEffectInstance statusEffect : fluid.getStatusEffects()) {
                    user.addStatusEffect(statusEffect);
                }
            }
        }
        return stack;
    }

    public static void setFluid(ItemStack stack, Fluid fluid) {
        NbtCompound nbt = stack.getOrCreateTag();
        nbt.putInt("Fluid", fluid.ordinal());
    }

    public static Fluid getFluid(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateTag();
        return Fluid.values()[nbt.getInt("Fluid")];
    }

    public enum Fluid {
        PURIFIED_WATER(6, 2, ImmutableList.of()),
        RAW_WATER(2, 1, ImmutableList.of(new StatusEffectInstance(StatusEffects.POISON, 600))),
        HONEY_WATER(5, 4, ImmutableList.of());
        private final int moisture;
        private final int food;
        private final List<StatusEffectInstance> statusEffects;

        Fluid(int moisture, int food, List<StatusEffectInstance> statusEffects) {
            this.moisture = moisture;
            this.food = food;
            this.statusEffects = statusEffects;
        }

        public int getFood() {
            return food;
        }

        public int getMoisture() {
            return moisture;
        }

        public List<StatusEffectInstance> getStatusEffects() {
            return statusEffects;
        }
    }
}
