package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.FletchingTableScreenHandler;
import io.github.asablock.rmc.RMCStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public class MixinFletchingTable {
    private static final Text TITLE = new TranslatableText("container.fletching_table");

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                       Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient()) {
            player.openHandledScreen(createScreenHandlerFactory(state, world, pos));
            player.incrementStat(RMCStats.INTERACT_WITH_FLETCHINGTABLE);
            cir.setReturnValue(ActionResult.CONSUME);
        } else {
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    private NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
            new FletchingTableScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }
}
