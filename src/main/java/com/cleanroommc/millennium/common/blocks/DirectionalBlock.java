package com.cleanroommc.millennium.common.blocks;

import net.minecraft.block.BlockDirectional;
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

/**
 * Adapted version of {@link BlockDirectional} for Millennium
 */
public abstract class DirectionalBlock extends MillenniumBlock {
  public static final PropertyDirection FACING = BlockDirectional.FACING;

  public DirectionalBlock(Settings settings) {
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
    return getDefaultState()
        .withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
}
