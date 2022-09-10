package io.github.asablock.rmc.block.entity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class ChoppingResultBuilder {
    private Map<Item, ChoppingBlockEntity.ResultAndContainerData> map;

    public ChoppingResultBuilder() {
        map = new HashMap<>();
    }

    public ChoppingResultBuilder addContained(Item ingredient, Item container, ItemOrStack... itemStacks) {
        map.put(ingredient, new ChoppingBlockEntity.ResultAndContainerData(Arrays.stream(itemStacks)
                .map(ItemOrStack::toStack).collect(Collectors.toList()), container));
        return this;
    }

    public ChoppingResultBuilder addContained(Item ingredient, Item container, ItemOrStack itemStack) {
        map.put(ingredient, new ChoppingBlockEntity.ResultAndContainerData(Collections.singletonList(itemStack.toStack()), container));
        return this;
    }

    public ChoppingResultBuilder add(Item ingredient, ItemOrStack itemStack) {
        map.put(ingredient, new ChoppingBlockEntity.ResultAndContainerData(Collections.singletonList(itemStack.toStack()), null));
        return this;
    }

    public ChoppingResultBuilder add(Item ingredient, ItemOrStack... itemStacks) {
        map.put(ingredient, new ChoppingBlockEntity.ResultAndContainerData(Arrays.stream(itemStacks)
                .map(ItemOrStack::toStack).collect(Collectors.toList()), null));
        return this;
    }

    public Map<Item, ChoppingBlockEntity.ResultAndContainerData> build() {
        return map = Collections.unmodifiableMap(map);
    }

    @FunctionalInterface
    public interface ItemOrStack {
        ItemStack toStack();

        static ItemOrStack of(Item item) {
            ItemStack stack = new ItemStack(item, 1);
            return () -> stack;
        }

        static ItemOrStack of(Item item, int count) {
            ItemStack stack = new ItemStack(item, count);
            return () -> stack;
        }
    }
}
