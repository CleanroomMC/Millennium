package io.github.cleanroommc.millennium.common.items;

import io.github.cleanroommc.millennium.Millennium;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public final class MillenniumItems {
  public static final List<Item> ITEMS = new ArrayList<>();

  public static BundleItem ITEM_BUNDLE = register("bundle", new BundleItem());

  private static <T extends MillenniumItem> T register(String id, T item) {
    item.setRegistryName(Millennium.MODID, id);
    ITEMS.add(item);
    return item;
  }

  public static void registerItem(IForgeRegistry<Item> registry) {
    for (Item item : ITEMS) {
      registry.register(item);
    }
  }

  public static void registerModel() {
    for (Item item : ITEMS) {
      if (item instanceof MillenniumItem)
        ((MillenniumItem) item).registerModel();
    }
  }
}
