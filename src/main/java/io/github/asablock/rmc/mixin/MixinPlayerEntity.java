package io.github.asablock.rmc.mixin;

import com.mojang.authlib.GameProfile;
import io.github.asablock.rmc.DrinkingManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity implements DrinkingManager.Getter {
    @Unique
    private DrinkingManager rmc$drinkingManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initDrinkingManager(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        rmc$drinkingManager = new DrinkingManager();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V",
            shift = At.Shift.AFTER))
    private void tickInServer(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        rmc$drinkingManager.update(self);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/HungerManager;writeNbt(Lnet/minecraft/nbt/NbtCompound;)V",
            shift = At.Shift.AFTER))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        rmc$drinkingManager.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/HungerManager;readNbt(Lnet/minecraft/nbt/NbtCompound;)V",
            shift = At.Shift.AFTER))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        rmc$drinkingManager.readNbt(nbt);

    }

    @Override
    public DrinkingManager rmc_getDrinkingManager() {
        return rmc$drinkingManager;
    }
}