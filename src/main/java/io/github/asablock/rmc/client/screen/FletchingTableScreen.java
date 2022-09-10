package io.github.asablock.rmc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.asablock.rmc.FletchingTableScreenHandler;
import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FletchingTableScreen extends HandledScreen<FletchingTableScreenHandler> {
    private static final Identifier TEXTURE = ReasonableMCMod.identifier("textures/gui/container/fletching_table.png");

    public FletchingTableScreen(FletchingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (MCUtil.oneHasStack(handler, 0, 5) && !this.handler.getSlot(5).hasStack()) {
            this.drawTexture(matrices, i + 92, j + 31, this.backgroundWidth, 0, 28, 21);
        }
    }
}
