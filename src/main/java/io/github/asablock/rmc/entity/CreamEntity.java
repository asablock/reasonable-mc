package io.github.asablock.rmc.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.gen.ChunkRandom;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class CreamEntity extends SlimeEntity {
    private static final float BASE_EXPLOSION = 1.8F;

    public CreamEntity(EntityType<? extends CreamEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean canSpawnCream(EntityType<CreamEntity> type,
                                        WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (Objects.equals(world.getBiomeKey(pos), Optional.of(BiomeKeys.SWAMP)) &&
                    pos.getY() > 50 && pos.getY() < 70 && random.nextFloat() < 0.5F &&
                    random.nextFloat() < world.getMoonSize() && world.getLightLevel(pos) <= random.nextInt(8)) {
                return canMobSpawn(type, world, spawnReason, pos, random);
            }

            if (!(world instanceof StructureWorldAccess)) {
                return false;
            }

            ChunkPos chunkPos = new ChunkPos(pos);
            boolean bl = ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z,
                    ((StructureWorldAccess) world).getSeed(), 987234911L).nextInt(10) == 0;
            if (random.nextInt(30) == 0 && bl && pos.getY() < 40) {
                return canMobSpawn(type, world, spawnReason, pos, random);
            }
        }

        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!world.isClient) {
                if (!this.removed) {
                    if (!source.isExplosive()) {
                        this.world.createExplosion(this, getX(), getY(), getZ(),
                                BASE_EXPLOSION * getSize(), Explosion.DestructionType.DESTROY);
                    }
                    this.remove();
                    return true;
                } else {
                    return super.damage(source, amount);
                }
            } else {
                return false;
            }
        }
    }
}
