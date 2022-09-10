package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.DrinkingManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Unique
    private int rmc$syncedWaterLevel;

    @Inject(method = "playerTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;getHealth()F",
            shift = At.Shift.BEFORE, ordinal = 0))
    private void syncDrinking(CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        DrinkingManager drinkingManager = ((DrinkingManager.Getter) self).rmc_getDrinkingManager();
        if (drinkingManager.getWaterLevel() != rmc$syncedWaterLevel) {
            ServerPlayNetworking.send(self, DrinkingManager.PACKET_ID, new PacketByteBuf(Unpooled.buffer()
                    .writeInt(drinkingManager.getWaterLevel())));
            rmc$syncedWaterLevel = drinkingManager.getWaterLevel();
        }
    }
}
