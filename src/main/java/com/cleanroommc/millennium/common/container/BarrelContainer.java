package com.cleanroommc.millennium.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BarrelContainer extends Container {
  private final IInventory inventory;

  public BarrelContainer(IInventory playerInv, IInventory inventory, EntityPlayer player) {
    this.inventory = inventory;
    inventory.openInventory(player);

    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        addSlotToContainer(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));

    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

    for (int i = 0; i < 9; ++i) addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return inventory.isUsableByPlayer(playerIn);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < inventory.getSizeInventory()) {
        if (!mergeItemStack(
            itemstack1, inventory.getSizeInventory(), inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!mergeItemStack(itemstack1, 0, inventory.getSizeInventory(), false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
      else slot.onSlotChanged();
    }

    return itemstack;
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);
    inventory.closeInventory(playerIn);
  }
}
