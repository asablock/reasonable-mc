package io.github.asablock.rmc.client.render.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.Entity;

public class CreamEntityModel<T extends Entity> extends CompositeEntityModel<T> {
    private final ModelPart innerCube;

    public CreamEntityModel(int size) {
        this.innerCube = new ModelPart(this, 0, 32);
        if (size > 0) {
            this.innerCube.addCuboid(-3.25F, 18.0F, -3.5F, 5.0F, 5.0F, 5.0F);
        } else {
            this.innerCube.addCuboid(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        }
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.innerCube);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }
}
