package io.github.asablock.rmc.client;

import io.github.asablock.rmc.DrinkingManager;
import io.github.asablock.rmc.ReasonableMCMod;
import io.github.asablock.rmc.client.render.block.ChoppingBlockEntityRenderer;
import io.github.asablock.rmc.client.screen.FletchingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class ClientReasonableMCMod implements ClientModInitializer {
    public static final Identifier GUI_EXTRA_ICONS_TEXTURE = ReasonableMCMod.identifier("textures/gui/icons.png");

    @Override
    public void onInitializeClient() {
        for (Field field : ReasonableMCMod.class.getDeclaredFields()) {
            ClientRenderLayer annotation = field.getAnnotation(ClientRenderLayer.class);
            if (annotation != null) {
                try {
                    Block block = (Block) field.get(null);
                    BlockRenderLayerMap.INSTANCE.putBlock(block, annotation.value().getRenderLayer());
                } catch (IllegalAccessException e) {
                    throw new AssertionError(); // All public
                }
            }
        }

        BlockEntityRendererRegistry.INSTANCE.register(ReasonableMCMod.CHOPPING_BLOCK_ENTITY_TYPE, ChoppingBlockEntityRenderer::new);

        /*EntityRendererRegistry.INSTANCE.register(ReasonableMCMod.CREAM_ENTITY_TYPE,
                (manager, context) -> new SlimeEntityRenderer(manager));*/

        ClientPlayNetworking.registerGlobalReceiver(DrinkingManager.PACKET_ID,
                (client, handler, buf, responseSender) -> client.execute(
                        () -> ((DrinkingManager.Getter) client.player).rmc_getDrinkingManager().setWaterLevel(buf.readInt())));

        ScreenRegistry.register(ReasonableMCMod.SCH_FLETCHING_TABLE, FletchingTableScreen::new);
    }
}
