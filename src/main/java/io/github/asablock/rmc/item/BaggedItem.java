package io.github.asablock.rmc.item;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.item.Item;

public class BaggedItem extends Item {
    public BaggedItem(Settings settings) {
        super(settings.recipeRemainder(ReasonableMCMod.ITEM_BAG));
    }
}
