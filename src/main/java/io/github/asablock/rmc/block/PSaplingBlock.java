package io.github.asablock.rmc.block;

import io.github.asablock.rmc.annotations.Publicifier;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;

@Publicifier
public class PSaplingBlock extends SaplingBlock {
    public PSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }
}
