package io.github.asablock.rmc;

import net.minecraft.item.Item;

public interface RMCItem {
    int rmc_getMoisture();

    boolean rmc_isDrink();

    interface Settings {
        Item.Settings rmc_drink(int moisture);

        int rmc_getMoisture();
    }
}
