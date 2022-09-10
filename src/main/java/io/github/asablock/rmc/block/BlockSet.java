package io.github.asablock.rmc.block;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface BlockSet extends Iterable<Block> {
    int size();

    void register();

    default void register(Block block, Identifier id) {
        Registry.register(Registry.BLOCK, id, block);
    }

    default Identifier append(Identifier base, String append) {
        return new Identifier(base.getNamespace(), base.getPath() + append);
    }

    default Identifier prepend(Identifier base, String prepend) {
        return new Identifier(base.getNamespace(), prepend + base.getPath());
    }
}