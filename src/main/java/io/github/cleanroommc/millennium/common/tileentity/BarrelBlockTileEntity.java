package io.github.cleanroommc.millennium.common.tileentity;

import io.github.cleanroommc.millennium.client.sounds.BarrelSoundEvents;
import io.github.cleanroommc.millennium.common.blocks.BarrelBlock;
import io.github.cleanroommc.millennium.common.blocks.MillenniumBlocks;
import io.github.cleanroommc.millennium.common.container.BarrelContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BarrelBlockTileEntity extends TileEntityLockableLoot {
  public int playerUsingCount = 0;
  public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);

  @Override
  protected NonNullList<ItemStack> getItems() {
    return inventory;
  }

  @Override
  public int getSizeInventory() {
    return inventory.size();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemstack : this.inventory) if (!itemstack.isEmpty()) return false;

    return true;
  }

  @Override
  public boolean receiveClientEvent(int id, int type) {
    IBlockState state = world.getBlockState(pos);

    if (type > 0 && !state.getValue(BarrelBlock.OPEN)) {
      world.setBlockState(pos, state.withProperty(BarrelBlock.OPEN, true), 3);
      world.markBlockRangeForRenderUpdate(pos, pos);
      world.playSound(null, pos, BarrelSoundEvents.OPEN, SoundCategory.BLOCKS, 1F, 1F);
    } else {
      world.setBlockState(pos, state.withProperty(BarrelBlock.OPEN, false), 3);
      world.markBlockRangeForRenderUpdate(pos, pos);
      world.playSound(null, pos, BarrelSoundEvents.CLOSE, SoundCategory.BLOCKS, 1F, 1F);
    }

    return true;
  }

  @Override
  public void openInventory(EntityPlayer player) {
    playerUsingCount++;
    world.addBlockEvent(this.pos, MillenniumBlocks.BARREL_BLOCK, 1, this.playerUsingCount);
  }

  @Override
  public void closeInventory(EntityPlayer player) {
    playerUsingCount--;
    world.addBlockEvent(this.pos, MillenniumBlocks.BARREL_BLOCK, 1, this.playerUsingCount);
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    return new BarrelContainer(playerInventory, this, playerIn);
  }

  @Override
  public String getGuiID() {
    return "millennium:barrel";
  }

  @Override
  public String getName() {
    return "container.barrel";
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (!checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, inventory);
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
    if (!checkLootAndRead(compound)) ItemStackHelper.loadAllItems(compound, inventory);
  }
}
