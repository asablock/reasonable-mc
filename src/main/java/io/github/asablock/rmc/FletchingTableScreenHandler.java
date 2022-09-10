package io.github.asablock.rmc;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.ItemTags;

public class FletchingTableScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private long lastTakeResultTime;
    public final Inventory inventory;
    private final CraftingResultInventory resultInventory;

    public FletchingTableScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public FletchingTableScreenHandler(int syncId, PlayerInventory inventory, final ScreenHandlerContext context) {
        super(ReasonableMCMod.SCH_FLETCHING_TABLE, syncId);
        this.inventory = new SimpleInventory(5) {
            public void markDirty() {
                FletchingTableScreenHandler.this.onContentChanged(this);
                super.markDirty();
            }
        };
        this.resultInventory = new CraftingResultInventory() {
            public void markDirty() {
                FletchingTableScreenHandler.this.onContentChanged(this);
                super.markDirty();
            }
        };
        this.context = context;
        class Slot1 extends Slot {
            public Slot1(int index, int x, int y) {
                super(FletchingTableScreenHandler.this.inventory, index, x, y);
            }

            public boolean canInsert(ItemStack stack) {
                Item item = stack.getItem();
                return item == Items.FLINT || item == Items.IRON_NUGGET || item == Items.IRON_INGOT;
            }
        }
        this.addSlot(new Slot1(0, 39, 17));
        this.addSlot(new Slot1(1, 57, 17));
        this.addSlot(new Slot(this.inventory, 2, 48, 35) {
            public boolean canInsert(ItemStack stack) {
                Item item = stack.getItem();
                return item == Items.STICK || ItemTags.PLANKS.contains(item);
            }
        });
        class Slot2 extends Slot {
            public Slot2(int index, int x, int y) {
                super(FletchingTableScreenHandler.this.inventory, index, x, y);
            }

            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == Items.FEATHER;
            }
        }
        this.addSlot(new Slot2(3, 57, 53));
        this.addSlot(new Slot2(4, 39, 53));
        this.addSlot(new Slot(this.resultInventory, 2, 124, 35) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
                FletchingTableScreenHandler.this.slots.get(0).takeStack(1);
                FletchingTableScreenHandler.this.slots.get(1).takeStack(1);
                FletchingTableScreenHandler.this.slots.get(2).takeStack(1);
                FletchingTableScreenHandler.this.slots.get(3).takeStack(1);
                FletchingTableScreenHandler.this.slots.get(4).takeStack(1);
                stack.getItem().onCraft(stack, player.world, player);
                context.run((world, blockPos) -> {
                    long l = world.getTime();
                    if (lastTakeResultTime != l) {
                        world.playSound(null, blockPos, ReasonableMCMod.SE_ITEM_BLOCK_FLETCHING_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        lastTakeResultTime = l;
                    }
                });
                return super.onTakeItem(player, stack);
            }
        });


        int k;
        for (k = 0; k < 3; ++k) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.inventory) {
            this.updateResult();
        }
    }

    private void updateResult() {
        boolean bl = true;
        for (int i = 0; i < 5; i++) {
            if (inventory.getStack(i).isEmpty()) {
                bl = false;
                break;
            }
        }
        if (!bl) {
            this.resultInventory.setStack(0, ItemStack.EMPTY);
        } else {
            boolean ingot = inventory.getStack(0).getItem() == Items.IRON_INGOT;
            boolean ingot2 = inventory.getStack(1).getItem() == Items.IRON_INGOT;
            if (ingot != ingot2) this.resultInventory.setStack(0, ItemStack.EMPTY);
            boolean stk = inventory.getStack(2).getItem() == Items.STICK;
            if (!ingot && stk) this.resultInventory.setStack(0, new ItemStack(Items.ARROW, 10));
            else if (ingot && !stk) this.resultInventory.setStack(0, new ItemStack(Items.ARROW, 52));
            else this.resultInventory.setStack(0, ItemStack.EMPTY);
        }

        this.sendContentUpdates();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, Blocks.FLETCHING_TABLE);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            ItemStack itemStack3 = this.inventory.getStack(0);
            ItemStack itemStack4 = this.inventory.getStack(1);
            if (index == 5) {
                if (!this.insertItem(itemStack2, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index != 0 && index != 1) {
                if (!itemStack3.isEmpty() && !itemStack4.isEmpty()) {
                    if (index >= 3 && index < 30) {
                        if (!this.insertItem(itemStack2, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 6, 42, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run(((world, pos) -> {
            this.dropInventory(player, world, inventory);
        }));
    }
}
