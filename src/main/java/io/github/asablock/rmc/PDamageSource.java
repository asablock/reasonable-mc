package io.github.asablock.rmc;

import io.github.asablock.rmc.annotations.Publicifier;
import net.minecraft.entity.damage.DamageSource;

@Publicifier
public class PDamageSource extends DamageSource {
    public PDamageSource(String name) {
        super(name);
    }

    @Override
    public PDamageSource setBypassesArmor() {
        super.setBypassesArmor();
        return this;
    }

    @Override
    public PDamageSource setOutOfWorld() {
        super.setOutOfWorld();
        return this;
    }

    @Override
    public PDamageSource setUnblockable() {
        super.setUnblockable();
        return this;
    }

    @Override
    public PDamageSource setFire() {super.setFire();
        return this;
    }
}
