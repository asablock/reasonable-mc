package io.github.asablock.rmc.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface CropBlockInvoker {
    @Invoker
    static float invokeGetAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        throw new AssertionError();
    }
}
