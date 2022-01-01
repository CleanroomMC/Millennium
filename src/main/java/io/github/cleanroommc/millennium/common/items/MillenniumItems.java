package io.github.cleanroommc.millennium.common.items;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public final class MillenniumItems {
  static final List<Item> ITEMS = new ArrayList<>();

  public static BundleItem ITEM_BUNDLE = addItem(new BundleItem());

  private static <T extends Item> T addItem(T item) {
    ITEMS.add(item);
    return item;
  }

  public static void init(IForgeRegistry<Item> registry) {
    for (Item item : ITEMS) {
      registry.register(item);
    }
  }
}
