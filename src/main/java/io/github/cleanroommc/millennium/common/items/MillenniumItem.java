package io.github.cleanroommc.millennium.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;

public abstract class MillenniumItem extends Item {
  EnumRarity rarity;

  public MillenniumItem(Settings settings) {
    setMaxStackSize(settings.maxCount);
    setMaxDamage(settings.maxDamage);
    setCreativeTab(settings.tab);

    rarity = settings.rarity;
  }

  @Override
  public IRarity getForgeRarity(ItemStack stack) {
    return rarity;
  }

  public static class Settings {
    int maxCount = 64;
    int maxDamage;
    CreativeTabs tab;
    EnumRarity rarity;

    public Settings() {
      rarity = EnumRarity.COMMON;
    }

    public Settings maxCount(int count) {
      maxCount = count;
      return this;
    }

    public Settings maxDamage(int damage) {
      maxCount = damage;
      return this;
    }

    public Settings creativeTab(CreativeTabs tab) {
      this.tab = tab;
      return this;
    }
  }
}
