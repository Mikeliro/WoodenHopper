package cech12.woodenhopper.inventory;

import cech12.woodenhopper.api.inventory.WoodenHopperContainerTypes;
import cech12.woodenhopper.tileentity.WoodenHopperTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class WoodenHopperContainer extends Container {
    private final IInventory hopperInventory;

    public WoodenHopperContainer(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(WoodenHopperContainerTypes.WOODEN_HOPPER, id);
        this.hopperInventory = inventory;
        assertInventorySize(inventory, 1);
        inventory.openInventory(playerInventory.player);
        //hopper slot
        this.addSlot(new Slot(inventory, 0, 80, 20));
        //inventory
        for(int l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
    }

    public WoodenHopperContainer(int id, PlayerInventory playerInventoryIn, BlockPos pos) {
        this(id, playerInventoryIn, (WoodenHopperTileEntity) playerInventoryIn.player.world.getTileEntity(pos));
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return this.hopperInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.hopperInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.hopperInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(@Nonnull PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.hopperInventory.closeInventory(playerIn);
    }
}
