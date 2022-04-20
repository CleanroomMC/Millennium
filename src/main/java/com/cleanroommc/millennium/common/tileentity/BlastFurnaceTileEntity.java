package com.cleanroommc.millennium.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import javax.annotation.Nonnull;

public class BlastFurnaceTileEntity extends TileEntityFurnace {
    @Override
    public int getCookTime(@Nonnull ItemStack stack) {
        return 100;
    }
}
