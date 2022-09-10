package io.github.asablock.rmc.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Blocks.class)
public interface BlocksInvoker {
    @Invoker
    static PillarBlock invokeCreateLogBlock(MapColor topMapColor, MapColor sideMapColor) {
        throw new AssertionError();
    }

    @Invoker
    static LeavesBlock invokeCreateLeavesBlock() {
        throw new AssertionError();
    }
}