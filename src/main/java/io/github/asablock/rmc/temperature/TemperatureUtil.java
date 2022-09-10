package io.github.asablock.rmc.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;

public class TemperatureUtil {
    public static float getBiomeTemperature(PlayerEntity player) {
        Biome biome = player.world.getBiome(player.getBlockPos());
        float temperature = biome.getTemperature();
        if (player.world.getDimension().isUltrawarm()) {
            temperature *= 1.78f;
        }
        return temperature;
    }

    public static float lightLevelToTemperature(int lightLevel, float modifier) {
        return lightLevel / 16.5f * modifier - 0.1f;
    }

    public static float getAmbientTemperature(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        float block = lightLevelToTemperature(player.world.getLightLevel(LightType.BLOCK, pos), 0.973f);
        float sky = lightLevelToTemperature(player.world.getLightLevel(LightType.SKY, pos), 0.1873f);
        return (block + sky) * 0.76f;
    }

    public static float getPlayerTemperature(PlayerEntity player) {
        return getBiomeTemperature(player) + getAmbientTemperature(player);
    }
}
