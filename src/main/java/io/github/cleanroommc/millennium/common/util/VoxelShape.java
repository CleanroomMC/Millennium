package io.github.cleanroommc.millennium.common.util;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * Works like {@link net.minecraft.util.shape.VoxelShape}, construct the class with pixel-scaled
 * parameters.
 */
public class VoxelShape extends AxisAlignedBB {
  public static final VoxelShape EMPTY = new VoxelShape(0, 0, 0, 0, 0, 0);

  /**
   * This constructor will automatically divide all parameters with 16 to satisfy AxisAlignedBB's
   * actual scale.
   *
   * @param x1 x start pos
   * @param y1 y start pos
   * @param z1 z start pos
   * @param x2 x end pos
   * @param y2 y end pos
   * @param z2 z end pos
   */
  public VoxelShape(double x1, double y1, double z1, double x2, double y2, double z2) {
    super(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16);
  }
}
