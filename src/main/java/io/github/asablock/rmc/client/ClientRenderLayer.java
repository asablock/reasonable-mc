package io.github.asablock.rmc.client;

import net.minecraft.client.render.RenderLayer;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClientRenderLayer {
    @NotNull Layer value();

    enum Layer {
        CUTOUT(RenderLayer.getCutout()),
        TRANSLUCENT(RenderLayer.getTranslucent());
        private final RenderLayer renderLayer;

        Layer(RenderLayer renderLayer) {
            this.renderLayer = renderLayer;
        }

        public RenderLayer getRenderLayer() {
            return renderLayer;
        }
    }
}
