package io.github.asablock.rmc.mixin;

import io.github.asablock.rmc.DrinkingManager;
import io.github.asablock.rmc.client.ClientReasonableMCMod;
import io.github.asablock.rmc.temperature.TemperatureFeel;
import io.github.asablock.rmc.temperature.TemperatureUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.FluidTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract int getHeartRows(int heartCount);

    @Shadow protected abstract int getHeartCount(LivingEntity entity);

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE))
    private void renderDrinkingStatus(MatrixStack matrices, CallbackInfo ci) {
        InGameHud self = (InGameHud) (Object) this;

        ClientPlayerEntity playerEntity = client.player;

        int t = this.scaledHeight - 49;
        int n = this.scaledWidth / 2 + 91;
        int k = n - 30;

        this.client.getProfiler().swap("water");
        this.client.getTextureManager().bindTexture(ClientReasonableMCMod.GUI_EXTRA_ICONS_TEXTURE);
        int ah = playerEntity.getMaxAir();
        int ai = Math.min(playerEntity.getAir(), ah);
        boolean isAirDrawn = playerEntity.isSubmergedIn(FluidTags.WATER) || ai < ah;

        int ad = this.getHeartRows(this.getHeartCount(playerEntity)) - 1;
        t -= ad * 10;

        if (isAirDrawn) {
            t -= 10;
        }

        int wl = ((DrinkingManager.Getter) playerEntity).rmc_getDrinkingManager().getWaterLevel();

        for (int i = 0; i < 10; i++) {
            int x = n - i * 8 - 9;
            if (i * 2 + 1 < wl) {
                self.drawTexture(matrices, x, t, 18, 20, 9, 9);
            } else if (i * 2 + 1 == wl) {
                self.drawTexture(matrices, x, t, 9, 0, 9, 9);
            } else {
                self.drawTexture(matrices, x, t, 0, 0, 9, 9);
            }
        }

        this.client.getProfiler().swap("temperature");
        TemperatureFeel temperatureFeel = TemperatureFeel.getTemperatureFeel(TemperatureUtil.getPlayerTemperature(playerEntity));
        self.drawTexture(matrices, n - 18, scaledHeight, temperatureFeel.u, temperatureFeel.v, 12, 12);

        this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
        this.client.getProfiler().pop();
    }
}
