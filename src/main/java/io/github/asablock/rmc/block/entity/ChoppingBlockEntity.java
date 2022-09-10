package io.github.asablock.rmc.block.entity;

import io.github.asablock.rmc.item.BaggedItem;
import io.github.asablock.rmc.MCUtil;
import io.github.asablock.rmc.ReasonableMCMod;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static io.github.asablock.rmc.block.entity.ChoppingResultBuilder.ItemOrStack.of;

public class ChoppingBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int utd;
    private ItemStack item;
    private Utensil utensil;

    public static final String UTENSIL = "Utensil";
    public static final String UTENSIL_DAMAGE = "Damage";
    public static final String UTENSIL_ID = "id";
    public static final String ITEM = "item";

    public ChoppingBlockEntity() {
        super(ReasonableMCMod.CHOPPING_BLOCK_ENTITY_TYPE);
        utd = 0;
        item = ItemStack.EMPTY;
        utensil = Utensil.EMPTY;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound tag) {
        super.fromTag(state, tag);
        fromClientTag(tag);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        toClientTag(nbt);
        return nbt;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        NbtCompound utensil = tag.getCompound(UTENSIL);
        utd = utensil.getInt(UTENSIL_DAMAGE);
        this.utensil = Utensil.ofId(utensil.getString(UTENSIL_ID));
        item = new ItemStack(Registry.ITEM.get(new Identifier(tag.getString(ITEM))));
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        NbtCompound utensil = new NbtCompound();
        utensil.putInt(UTENSIL_DAMAGE, utd);
        utensil.putString(UTENSIL_ID, this.utensil.asString());
        tag.put(UTENSIL, utensil);
        tag.putString(ITEM, Registry.ITEM.getId(item.getItem()).toString());
        return tag;
    }

    public ActionResult onUse(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (item.isEmpty()) {
            Item playerItem = itemStack.getItem();

            if (!player.abilities.creativeMode) {
                itemStack.decrement(1);
                if (playerItem instanceof BaggedItem) {
                    MCUtil.giveItem(player, ReasonableMCMod.ITEM_BAG);
                }
            }
            item = new ItemStack(playerItem);
            update();
            return ActionResult.SUCCESS;
        } else if (utensil == Utensil.EMPTY) {
            Utensil newUtensil = Utensil.ofItem(itemStack.getItem());
            if (newUtensil == Utensil.EMPTY || !newUtensil.getRecipes().containsKey(item.getItem())) {
                MCUtil.giveItem(player, item);
                item = ItemStack.EMPTY;
                update();
                return ActionResult.SUCCESS;
            }
            player.setStackInHand(hand, ItemStack.EMPTY);
            utensil = newUtensil;
            utd = itemStack.getDamage();
            update();
            return ActionResult.SUCCESS;
        } else {
            ResultAndContainerData racd = utensil.getRecipes().get(item.getItem());
            System.out.println(racd);
            if (racd == null) {
                return ActionResult.FAIL;
            }
            int durability = utd + 1;
            if (!racd.needsContainer() || MCUtil.inventoryRemoveOne(player.inventory, racd.getContainer())) {
                MCUtil.giveItems(player, racd.getResult());
                System.out.println("St");
                if (durability < utensil.getItem().getMaxDamage())
                    MCUtil.giveItem(player, MCUtil.newDamagedItemStack(utensil.getItem(), durability));
                item = ItemStack.EMPTY;
                utensil = Utensil.EMPTY;
                utd = 0;
                update();
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    private void update() {
        BlockState state = Objects.requireNonNull(world).getBlockState(pos);
        world.updateListeners(pos, state, state, 2);
    }

    public enum Utensil implements StringIdentifiable {
        KNIFE("knife", ReasonableMCMod.ITEM_KNIFE, new ChoppingResultBuilder()
                .add(Items.ROTTEN_FLESH, of(Items.LEATHER, 2), of(ReasonableMCMod.ITEM_REFINED_ROTTEN_FLESH))
                .addContained(ReasonableMCMod.ITEM_WAFER, Items.BOWL, of(ReasonableMCMod.ITEM_NOODLES))
                .build()),
        ROLLING_PIN("rolling_pin", ReasonableMCMod.ITEM_ROLLING_PIN, new ChoppingResultBuilder()
                .add(ReasonableMCMod.ITEM_DOUGH, of(ReasonableMCMod.ITEM_WAFER))
                .add(ReasonableMCMod.ITEM_FERMENTED_DOUGH, of(ReasonableMCMod.ITEM_FERMENTED_WAFER))
                .build()),
        YEAST("yeast", ReasonableMCMod.ITEM_YEAST_BAG, new ChoppingResultBuilder()
                .add(ReasonableMCMod.ITEM_DOUGH, of(ReasonableMCMod.ITEM_FERMENTED_DOUGH))
                .build()),
        EMPTY("empty", null, Collections.emptyMap());

        private final String strval;
        private final Item item;
        private final Map<Item, ResultAndContainerData> recipes;

        Utensil(String strval, Item item, Map<Item, ResultAndContainerData> recipes) {
            this.strval = strval;
            this.item = item;
            this.recipes = recipes;
        }

        public Item getItem() {
            return item;
        }

        public Map<Item, ResultAndContainerData> getRecipes() {
            return recipes;
        }

        @Override
        public String toString() {
            return strval;
        }

        @Override
        public String asString() {
            return strval;
        }

        public static Utensil ofItem(Item item) {
            for (Utensil value : values()) {
                if (value.item == item) {
                    return value;
                }
            }
            return EMPTY;
        }

        public static Utensil ofId(String id) {
            for (Utensil value : values()) {
                if (value.strval.equals(id)) {
                    return value;
                }
            }
            return EMPTY;
        }
    }

    static class ResultAndContainerData {
        private final Collection<ItemStack> result;
        private final Item container;

        ResultAndContainerData(Collection<ItemStack> result, Item container) {
            this.result = result;
            this.container = container;
        }

        public Collection<ItemStack> getResult() {
            return result;
        }

        public Item getContainer() {
            return container;
        }

        public boolean needsContainer() {
            return container != null;
        }

        @Override
        public String toString() {
            return String.format("ResultAndContainderData{container=%s,result=%s}", container, result);
        }
    }

    public static boolean isFilledChoppingBlock(@NotNull BlockView world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ChoppingBlockEntity) {
            return !((ChoppingBlockEntity) be).item.isEmpty();
        } else {
            return false;
        }
    }

    public ItemStack getStack() {
        return item;
    }
}
