package io.github.asablock.rmc.worldgen;

import com.mojang.serialization.Codec;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class AppleTreeDecorator extends TreeDecorator {
    public static final AppleTreeDecorator INSTANCE = new AppleTreeDecorator();
    public static final Codec<AppleTreeDecorator> CODEC = Codec.unit(INSTANCE);
    private static final Logger LOGGER = LogManager.getLogger();

    private AppleTreeDecorator() {} // Singleton mode

    @Override
    protected TreeDecoratorType<?> getType() {
        return RMCWorldGenConsts.APPLE;
    }

    @Override
    public void generate(StructureWorldAccess world, Random random, List<BlockPos> logPositions,
                         List<BlockPos> leavesPositions, Set<BlockPos> placedStates, BlockBox box) {
        int appleCount = random.nextInt(7) + 1;
        List<BlockPos> validPositions = getValidApplePositions(leavesPositions);
        for (int i = 0; i < Math.min(appleCount, validPositions.size()); i++) {
            int index = random.nextInt(validPositions.size());
            try {
                this.setBlockStateAndEncompassPosition(world, validPositions.get(index),
                        ReasonableMCMod.BLOCK_APPLE.getDefaultState(), placedStates, box);
            } catch (RuntimeException e) {
                LOGGER.error("Cannot decorate apple. The apple position is below.", e);
            }
        }
    }

    private static List<BlockPos> getValidApplePositions(List<BlockPos> leavesPositions) {
        int min = -1;
        for (BlockPos leavesPosition : leavesPositions) {
            if (min == -1 || leavesPosition.getY() < min) {
                min = leavesPosition.getY();
            }
        }
        final int finalMin = min;
        return leavesPositions.stream()
                .filter(pos -> pos.getY() == finalMin)
                .map(BlockPos::down)
                .collect(Collectors.toList());
    }

}
