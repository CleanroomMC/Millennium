package io.github.cleanroommc.millennium.common.blocks;

import io.github.cleanroommc.millennium.Millennium;
import io.github.cleanroommc.millennium.client.gui.GuiHandler;
import io.github.cleanroommc.millennium.common.tileentity.BarrelBlockTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BarrelBlock extends DirectionalBlock implements ITileEntityProvider {
  public static final PropertyBool OPEN = PropertyBool.create("open");

  public BarrelBlock() {
    super(
        new Settings(Material.WOOD)
            .strength(2.5F)
            .soundType(SoundType.WOOD)
            .creativeTab(CreativeTabs.DECORATIONS)
            .translationKey("barrel"));
    setDefaultState(getBlockState().getBaseState().withProperty(OPEN, false));
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    if (!worldIn.isRemote)
      playerIn.openGui(
          Millennium.INSTANCE, GuiHandler.GUI_BARREL, worldIn, pos.getX(), pos.getY(), pos.getZ());

    return true;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof BarrelBlockTileEntity) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (BarrelBlockTileEntity) tileEntity);
    }

    super.breakBlock(worldIn, pos, state);
  }

  @Override
  public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    super.eventReceived(state, worldIn, pos, id, param);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return tileentity != null && tileentity.receiveClientEvent(id, param);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int meta = super.getMetaFromState(state);

    if (state.getValue(OPEN)) meta |= 8;

    return meta;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, OPEN);
  }

  @Nullable
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new BarrelBlockTileEntity();
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }
}
