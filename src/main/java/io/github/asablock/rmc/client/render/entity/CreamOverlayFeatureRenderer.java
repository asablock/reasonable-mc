package io.github.asablock.rmc.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class CreamOverlayFeatureRenderer<T extends LivingEntity> extends FeatureRenderer<T, CreamEntityModel<T>> {
    private final EntityModel<T> model = new CreamEntityModel<>(0);

    public CreamOverlayFeatureRenderer(FeatureRendererContext<T, CreamEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!livingEntity.isInvisible()) {
            this.getContextModel().copyStateTo(this.model);
            this.model.animateModel(livingEntity, f, g, h);
            this.model.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = vertexConsumerProvider
                    .getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
            this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
