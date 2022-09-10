package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.ReasonableMCMod;
import io.github.asablock.rmc.block.RMCPortalFrameBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndPortalFrameBlock.class)
public class MixinEndPortalFrameBlock {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(AbstractBlock.Settings settings, CallbackInfo ci) {
        EndPortalFrameBlock self = (EndPortalFrameBlock) (Object) this;
        ((BlockInvoker) self).invokeSetDefaultState(self.getDefaultState().with(RMCPortalFrameBlock.REMOVABLE, false));
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ctx.getStack().getItem() == ReasonableMCMod.ITEM_REMOVABLE_END_PORTAL_FRAME) {
            cir.setReturnValue(cir.getReturnValue().with(RMCPortalFrameBlock.REMOVABLE, true));
        }
    }

    @Inject(method = "appendProperties", at = @At("HEAD"))
    private void appendCustomProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(RMCPortalFrameBlock.REMOVABLE);
    }
}
