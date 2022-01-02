package io.github.cleanroommc.millennium.common.blocks;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class MillenniumBlock extends Block {
  Settings settings;

  public MillenniumBlock(Settings settings) {
    super(settings.material, settings.material.getMaterialMapColor());
    this.settings = settings;

    setResistance(settings.resistance);
    setHardness(settings.hardness);
    setSoundType(settings.soundType);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return settings.opaque;
  }

  @Override
  public boolean isCollidable() {
    return settings.collidable;
  }

  @Override
  public float getSlipperiness(
      IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
    return settings.slipperiness.apply(state, world, pos);
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    return settings.lightValue.apply(state, world, pos);
  }

  @Override
  public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return settings.mapColor != null
        ? settings.mapColor.apply(state, worldIn, pos)
        : settings.material.getMaterialMapColor();
  }

  @FunctionalInterface
  public interface ContextFunction<R> {
    R apply(IBlockState t1, IBlockAccess t2, BlockPos t3);
  }

  public static class Settings {
    final Material material;
    final ContextFunction<MapColor> mapColor;
    boolean collidable = true;
    boolean opaque = true;
    float resistance;
    float hardness;
    SoundType soundType = SoundType.STONE;
    ContextFunction<Integer> lightValue = (state, access, pos) -> 0;
    ContextFunction<Float> slipperiness = (state, access, pos) -> 0.6F;

    public Settings(Material material) {
      this(material, material.getMaterialMapColor());
    }

    public Settings(Material material, MapColor mapColor) {
      this(material, (state, access, pos) -> mapColor);
    }

    public Settings(Material material, ContextFunction<MapColor> mapColor) {
      this.material = material;
      this.mapColor = mapColor;
    }

    public Settings noCollision() {
      collidable = false;
      opaque = false;
      return this;
    }

    public Settings nonOpaque() {
      opaque = false;
      return this;
    }

    public Settings resistance(float resistance) {
      this.resistance = resistance;
      return this;
    }

    public Settings hardness(float hardness) {
      this.hardness = hardness;
      return this;
    }

    public Settings soundType(SoundType soundType) {
      this.soundType = soundType;
      return this;
    }

    public Settings lightValue(ContextFunction<Integer> lightValue) {
      this.lightValue = lightValue;
      return this;
    }

    public Settings lightValue(Function<IBlockState, Integer> lightValue) {
      this.lightValue = (state, access, pos) -> lightValue.apply(state);
      return this;
    }

    public Settings lightValue(int lightValue) {
      this.lightValue = (state, access, pos) -> lightValue;
      return this;
    }

    public Settings slipperiness(ContextFunction<Float> slipperiness) {
      this.slipperiness = slipperiness;
      return this;
    }

    public Settings slipperiness(Function<IBlockState, Float> slipperiness) {
      this.slipperiness = (state, access, pos) -> slipperiness.apply(state);
      return this;
    }

    public Settings slipperiness(float slipperiness) {
      this.slipperiness = (state, access, pos) -> slipperiness;
      return this;
    }

    public static Settings copy(MillenniumBlock block) {
      Settings settings = new Settings(block.settings.material, block.blockMapColor);
      settings.collidable = block.settings.collidable;
      settings.opaque = block.settings.opaque;
      settings.soundType = block.settings.soundType;
      settings.lightValue = block.settings.lightValue;
      settings.slipperiness = block.settings.slipperiness;
      return settings;
    }
  }
}
