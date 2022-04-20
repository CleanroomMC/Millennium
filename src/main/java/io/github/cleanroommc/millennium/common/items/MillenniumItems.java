package io.github.cleanroommc.millennium.common.items;

import io.github.cleanroommc.millennium.Millennium;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public final class MillenniumItems {
  public static final List<MillenniumItem> ITEMS = new ArrayList<>();
  public static final List<Item> ITEM_BLOCKS = new ArrayList<>();

  public static BundleItem ITEM_BUNDLE = register("bundle", new BundleItem());

  public static MillenniumItem GLOW_INK_SAC =
      register(
          "glow_ink_sac",
          new MillenniumItem(
              new MillenniumItem.Settings()
                  .creativeTab(CreativeTabs.MATERIALS)
                  .translationKey("glow_ink_sac")));

  private static <T extends MillenniumItem> T register(String id, T item) {
    item.setRegistryName(Millennium.MODID, id);
    ITEMS.add(item);
    return item;
  }

  public static void registerItem(IForgeRegistry<Item> registry) {
    for (Item item : ITEMS) registry.register(item);
    for (Item item : ITEM_BLOCKS) registry.register(item);
  }

  public static void registerModel() {
    for (MillenniumItem item : ITEMS) item.registerModel();
    for (Item item : ITEM_BLOCKS) Millennium.proxy.registerItemRenderer(item, 0, "inventory");
  }
}
