package com.cleanroommc.millennium.client.gui;

import com.cleanroommc.millennium.common.container.BarrelContainer;
import com.cleanroommc.millennium.common.tileentity.BarrelBlockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_BARREL = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case GUI_BARREL:
                return new BarrelContainer(player.inventory, (BarrelBlockTileEntity) tileEntity, player);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case GUI_BARREL:
                return new BarrelGui(player.inventory, (BarrelBlockTileEntity) tileEntity, player);
            default:
                return null;
        }
    }
}
