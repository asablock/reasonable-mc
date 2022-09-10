package io.github.asablock.rmc.client.render.block;

import io.github.asablock.rmc.block.entity.ChoppingBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public class ChoppingBlockEntityRenderer extends BlockEntityRenderer<ChoppingBlockEntity> {
    public ChoppingBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ChoppingBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        assert blockEntity.getWorld() != null;
        matrices.push();
        matrices.translate(0.5, 0.0625, 0.5);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getStack(),
                ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        matrices.pop();
    }
}
