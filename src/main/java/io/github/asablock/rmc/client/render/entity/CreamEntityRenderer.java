package io.github.asablock.rmc.client.render.entity;

import io.github.asablock.rmc.entity.CreamEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CreamEntityRenderer extends MobEntityRenderer<CreamEntity, CreamEntityModel<CreamEntity>> {
    private static final Identifier TEXTURE = new Identifier("reasonable-mc", "textures/entity/cream.png");

    public CreamEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CreamEntityModel<>(16), 0.25F);
        this.addFeature(new CreamOverlayFeatureRenderer<>(this));
    }

    public void render(CreamEntity creamEntity, float f, float g,
                       MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (float) creamEntity.getSize();
        super.render(creamEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    protected void scale(SlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
        float g = 0.999F;
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float h = (float)slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(j * h, 1.0F / j * h, j * h);
    }

    @Override
    public Identifier getTexture(CreamEntity entity) {
        return TEXTURE;
    }
}
