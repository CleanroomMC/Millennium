package com.cleanroommc.millennium.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class HorizontalBlock extends MillenniumBlock {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public HorizontalBlock(Settings settings) {
    super(settings);
    setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
  }

  @Override
  public IBlockState getStateForPlacement(
          World world,
          BlockPos pos,
          EnumFacing facing,
          float hitX,
          float hitY,
          float hitZ,
          int meta,
          EntityLivingBase placer,
          EnumHand hand) {
    return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing = EnumFacing.byIndex(meta);

    if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
      enumfacing = EnumFacing.NORTH;
    }

    return this.getDefaultState().withProperty(FACING, enumfacing);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
}
