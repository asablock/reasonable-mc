package io.github.asablock.rmc.temperature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public enum TemperatureFeel {
    VERY_COLD(Float.MIN_VALUE, 0.22f, 63, 12),
    COLD(0.22f, 0.4f, 51, 12),
    COOL(0.4f, 0.55f, 39, 12),
    TEMPERATE(0.55f, 0.7f, 27, 12),
    WARM(0.7f, 0.95f, 27, 0),
    HOT(0.95f, 1.5f, 39, 0),
    VERY_HOT(1.5f, Float.MAX_VALUE, 51, 0);

    public final float start;
    public final float stop;
    @Environment(EnvType.CLIENT)
    public final int u;
    @Environment(EnvType.CLIENT)
    public final int v;

    TemperatureFeel(float start, float stop, int u, int v) {
        this.start = start;
        this.stop = stop;
        this.u = u;
        this.v = v;
    }

    public static TemperatureFeel getTemperatureFeel(float temperature) {
        for (TemperatureFeel value : values()) {
            if (value.start >= temperature && value.stop < temperature) {
                return value;
            }
        }
        throw new AssertionError();
    }
}
