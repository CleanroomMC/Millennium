package com.cleanroommc.millennium.client;

import net.minecraft.item.EnumDyeColor;

public final class ColorUtil {
  public static int getRed(int color) {
    return color & 255;
  }

  public static int getGreen(int color) {
    return color >> 8 & 255;
  }

  public static int getBlue(int color) {
    return color >> 16 & 255;
  }

  public static int getAbgrColor(int alpha, int blue, int green, int red) {
    return (alpha & 255) << 24 | (blue & 255) << 16 | (green & 255) << 8 | (red & 255) << 0;
  }

  public static int getColor(EnumDyeColor color, boolean isGlowing) {
    int i = color.getColorValue();
    int j = (int)(getRed(i) * 0.4D);
    int k = (int)(getGreen(i) * 0.4D);
    int l = (int)(getBlue(i) * 0.4D);
    return i == EnumDyeColor.BLACK.getColorValue() && isGlowing ? -988212 : getAbgrColor(0, l, k, j);
  }
}
