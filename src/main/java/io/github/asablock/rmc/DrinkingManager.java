package io.github.asablock.rmc;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;

import java.util.Random;

public class DrinkingManager {
    private int waterLevel = 20;
    private int ctick;
    private int gtick;

    public interface Getter {
        DrinkingManager rmc_getDrinkingManager();
    }

    public static final Identifier PACKET_ID = new Identifier("reasonable-mc", "drinking_update_s2c");

    public void add(int water) {
        this.waterLevel = Math.min(waterLevel + water, 20);
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void drink(Item item, ItemStack stack) {
        RMCItem item$ = (RMCItem) item;
        if (item$.rmc_isDrink()) {
            this.add(item$.rmc_getMoisture());
        }
    }

    private static final int[] DIFFICULTY_TO_GTMIN = {-1, 1400, 800, 600};
    private static final int[] DIFFICULTY_TO_GTMAX = {-1, 2400, 1500, 1000};
    private static final int[] DIFFICULTY_TO_SKIP_TICK = {100, 60, 30, 8};
    private static final int[] DIFFICULTY_TO_DOUBLE_TICK = {-1, 6, 15, 29};
    private static final int[] DIFFICULTY_TO_NEGATIVE_TICK = {-1, 30, 17, 6};

    public void update(PlayerEntity player) {
        Difficulty difficulty = player.world.getDifficulty();
        if (difficulty != Difficulty.PEACEFUL) {
            Random random = player.getRandom();
            int d = difficulty.ordinal();
            if (waterLevel > 0) {
                if (ctick < gtick) {
                    if (!MCUtil.chance(DIFFICULTY_TO_SKIP_TICK[d], random)) {
                        int dryness = MCUtil.orDefault(player.getStatusEffect(RMCStatusEffects.DRYNESS),
                                StatusEffectInstance::getAmplifier, 0);
                        int tickCount = 1;
                        if (MCUtil.chance(DIFFICULTY_TO_DOUBLE_TICK[d], random)) {
                            tickCount *= 2;
                        }
                        int negativeTickChance = DIFFICULTY_TO_NEGATIVE_TICK[d];
                        if (dryness > 0) {
                            ctick += Math.round(dryness * 1.5);
                            negativeTickChance *= 0.75 - dryness * 0.25;
                        }
                        if (MCUtil.chance(negativeTickChance, random)) {
                            tickCount *= -1;
                        }
                        ctick += tickCount;
                    }
                } else {
                    int gtMin = DIFFICULTY_TO_GTMIN[difficulty.ordinal()];
                    int gtMax = DIFFICULTY_TO_GTMAX[difficulty.ordinal()];
                    gtick = MathHelper.nextInt(player.getRandom(), gtMin, gtMax);
                    ctick = 0;
                    waterLevel--;
                }
            } else {
                player.damage(ReasonableMCMod.TOO_DRY, Float.MAX_VALUE);
            }
        }
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("waterLevel", 99)) {
            this.waterLevel = nbt.getInt("waterLevel");
            this.ctick = nbt.getInt("drinkingCTick");
            this.gtick = nbt.getInt("drinkingTickGoal");
        }
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("waterLevel", this.waterLevel);
        nbt.putInt("drinkingCTick", this.ctick);
        nbt.putInt("drinkingTickGoal", this.gtick);
    }
}
