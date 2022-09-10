package io.github.asablock.rmc.block;

import io.github.asablock.rmc.mixin.BlocksInvoker;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Iterator;

public final class TreeBlockSet implements BlockSet {
    private final Block log;
    private final Block strippedLog;
    private final Block wood;
    private final Block strippedWood;
    private final Block leaves;
    private final Block planks;

    private final Identifier baseIdentifier;

    private final Block[] blocks;

    private TreeBlockSet(MapColor topMapColor, MapColor sideMapColor, Block planksBlock, Identifier baseIdentifier) {
        this.baseIdentifier = baseIdentifier;
        this.log = BlocksInvoker.invokeCreateLogBlock(topMapColor, sideMapColor);
        this.strippedLog = BlocksInvoker.invokeCreateLogBlock(topMapColor, topMapColor);
        this.wood = new PillarBlock(FabricBlockSettings.of(Material.WOOD, topMapColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
        this.strippedWood = new PillarBlock(FabricBlockSettings.of(Material.WOOD, topMapColor)
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD));
        this.leaves = BlocksInvoker.invokeCreateLeavesBlock();
        this.planks = planksBlock;
        this.blocks = new Block[]{log, strippedLog, wood, strippedWood, leaves};
    }

    public Block log() {
        return log;
    }

    public Block strippedLog() {
        return strippedLog;
    }

    public Block wood() {
        return wood;
    }

    public Block strippedWood() {
        return strippedWood;
    }

    public Block leaves() {
        return leaves;
    }

    public Block planks() {
        return planks;
    }

    public static TreeBlockSet create(MapColor topMapColor, MapColor sideMapColor, Block planksBlock, Identifier baseIdentifier) {
        return new TreeBlockSet(topMapColor, sideMapColor, planksBlock, baseIdentifier);
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public void register() {
        register(log, append(baseIdentifier, "_log"));
        Identifier stripped = prepend(baseIdentifier, "stripped_");
        register(strippedLog, append(stripped, "_log"));
        register(wood, append(baseIdentifier, "_wood"));
        register(strippedWood, append(stripped, "_wood"));
        register(leaves, append(baseIdentifier, "_leaves"));
    }

    @Override
    public Iterator<Block> iterator() {
        class Itr implements Iterator<Block> {
            private int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < 6;
            }

            @Override
            public Block next() {
                return blocks[cur++];
            }
        }

        return new Itr();
    }
}