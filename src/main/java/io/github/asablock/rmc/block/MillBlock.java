package io.github.asablock.rmc.block;

import io.github.asablock.rmc.item.BaggedItem;
import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MillBlock extends Block /*implements HandleConnectable*/ {
    public MillBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ITEM, ItemType.EMPTY).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ITEM);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemType itemType = state.get(ITEM);
            if (itemType != ItemType.EMPTY) {
                Direction direction = state.get(FACING);
                Direction direction2 = direction.rotateYClockwise();
                Item item = itemType.getResult();
                if (direction2 == Direction.NORTH) {
                    if (item instanceof BaggedItem) {
                        if (itemStack.getItem() != ReasonableMCMod.ITEM_BAG) {
                            return ActionResult.PASS;
                        }
                        itemStack.decrement(1);
                    }
                    MCUtil.giveItem(player, item);
                    world.setBlockState(pos, state.with(FACING, direction2).with(ITEM, ItemType.EMPTY));
                } else {
                    world.setBlockState(pos, state.with(FACING, direction2));
                }
                return ActionResult.SUCCESS;
            } else {
                ItemType newType = ItemType.of(itemStack.getItem());
                if (newType == ItemType.EMPTY) {
                    return ActionResult.PASS;
                } else {
                    itemStack.decrement(1);
                    if (newType.ingredient instanceof BaggedItem) {
                        MCUtil.giveItem(player, ReasonableMCMod.ITEM_BAG);
                    }
                    world.setBlockState(pos, state.with(ITEM, newType));
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<ItemType> ITEM = EnumProperty.of("item", ItemType.class);
    public static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 10, 16);

    public enum ItemType implements StringIdentifiable {
        EMPTY(null, null, "empty"),
        WHEAT(Items.WHEAT, ReasonableMCMod.ITEM_FLOUR_BAG, "wheat");
        private final Item ingredient;
        private final Item result;
        private final String strval;

        ItemType(Item ingredient, Item result, String strval) {
            this.ingredient = ingredient;
            this.result = result;
            this.strval = strval;
        }

        public Item getResult() {
            return result;
        }

        @Override
        public String asString() {
            return strval;
        }

        @Override
        public String toString() {
            return strval;
        }

        public static ItemType of(Item ingredient) {
            for (ItemType value : values()) {
                if (value.ingredient == ingredient) {
                    return value;
                }
            }
            return EMPTY;
        }
    }
}