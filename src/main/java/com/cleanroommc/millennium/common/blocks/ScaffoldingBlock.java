package com.cleanroommc.millennium.common.blocks;

import com.cleanroommc.millennium.common.util.VoxelShape;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ScaffoldingBlock extends MillenniumBlock {
  // NORMAL SHAPES
  public static final VoxelShape NORMAL_SHAPE$1 = createShape(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
  public static final VoxelShape NORMAL_SHAPE$2 = createShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D);
  public static final VoxelShape NORMAL_SHAPE$3 = createShape(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
  public static final VoxelShape NORMAL_SHAPE$4 = createShape(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D);
  public static final VoxelShape NORMAL_SHAPE$5 = createShape(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
  // BOTTOM OUTLINE SHAPES TODO: Implement multiple outline shape rendering functionality first
  public static final VoxelShape OUTLINE_SHAPE$1 = createShape(0.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D);
  public static final VoxelShape OUTLINE_SHAPE$2 = createShape(14.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
  public static final VoxelShape OUTLINE_SHAPE$3 = createShape(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 16.0D);
  public static final VoxelShape OUTLINE_SHAPE$4 = createShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 2.0D);

  public ScaffoldingBlock() {
    super(
        new MillenniumBlock.Settings(Material.WOOD, MapColor.SAND)
            .creativeTab(CreativeTabs.DECORATIONS)
            .translationKey("scaffolding"));
  }

  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(
      IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
    if (entityIn == null)
      super.addCollisionBoxToList(
          state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    else {
      if (entityIn.isSneaking()) {
      }
    }
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
}
