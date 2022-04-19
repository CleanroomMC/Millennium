package com.cleanroommc.millennium.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;

public class MillenniumSignTileEntity extends TileEntitySign {
  public boolean isGlowingText;
  public EnumDyeColor textColor;

  public boolean setColor(EnumDyeColor color) {
    if (color == textColor) return false;
    else {
      IBlockState state = world.getBlockState(pos);
      textColor = color;
      markDirty();
      world.notifyBlockUpdate(pos, state, state, 3);
      return true;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setBoolean("GlowingText", isGlowingText);
    if (textColor != null) compound.setString("Color", textColor.getName());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    isGlowingText = compound.getBoolean("GlowingText");
    String textColorName = compound.getString("Color");

    textColor = textColorName.isEmpty() ? null : EnumDyeColor.valueOf(textColorName.toUpperCase());
  }
}
