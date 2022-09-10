package io.github.asablock.rmc.block;

import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StoneMortarBlock extends Block {
    public StoneMortarBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ITEM, ItemType.EMPTY).with(STAGE, 0));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == ReasonableMCMod.ITEM_PESTLE) {
            if (state.get(ITEM) == ItemType.EMPTY) return ActionResult.PASS;
            if (state.get(STAGE) == 2) return ActionResult.PASS;
            world.setBlockState(pos, state.with(STAGE, state.get(STAGE) + 1));
            itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            return ActionResult.success(world.isClient());
        } else if (state.get(ITEM) == ItemType.EMPTY) {
            ItemType itemType = ItemType.of(itemStack.getItem());
            if (itemType != null) {
                world.setBlockState(pos, state.with(ITEM, itemType));
                itemStack.decrement(1);
                return ActionResult.success(world.isClient());
            } else {
                return ActionResult.PASS;
            }
        } else if (itemStack.getItem() == ReasonableMCMod.ITEM_BAG) {
            if (state.get(STAGE) < 2) return ActionResult.PASS;
            itemStack.decrement(1);
            System.out.println(state.get(ITEM));
            player.giveItemStack(new ItemStack(state.get(ITEM).getResult()));
            world.setBlockState(pos, getDefaultState());
            return ActionResult.success(world.isClient());
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ITEM, STAGE);
    }

    public static final EnumProperty<ItemType> ITEM = EnumProperty.of("item", ItemType.class, ItemType.values());
    public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);

    public enum ItemType implements StringIdentifiable {
        EMPTY("empty", null, null),
        RICE("rice", ReasonableMCMod.ITEM_FLOUR_BAG, ReasonableMCMod.ITEM_RICE);

        private final String strval;
        private final Item result;
        private final Item ingredient;

        ItemType(String strval, Item result, Item ingredient) {
            this.strval = strval;
            this.result = result;
            this.ingredient = ingredient;
        }

        @Override
        public String asString() {
            return strval;
        }

        @Override
        public String toString() {
            return strval;
        }

        public Item getResult() {
            return result;
        }

        public static ItemType of(Item item) {
            for (ItemType value : values()) {
                if (value.ingredient == item) {
                    return value;
                }
            }
            return null;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public static final VoxelShape SHAPE = createCuboidShape(3, 0, 3, 13, 6, 13);
}
