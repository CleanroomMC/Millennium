package com.cleanroommc.millennium.client.resource;

import com.cleanroommc.assetmover.AssetMoverAPI;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MillenniumTextures {
  @SideOnly(Side.CLIENT)
  public static void initTextures() {
    AssetHashMap textures = new AssetHashMap("textures");
    // Bundle
    textures.put("item/bundle.png");
    textures.put("item/bundle_filled.png");
    // Glow ink sac
    textures.put("item/glow_ink_sac.png");
    // Barrel, Shulker Box
    textures.put("gui/container/shulker_box.png");
    textures.put("block/barrel_bottom.png");
    textures.put("block/barrel_side.png");
    textures.put("block/barrel_top.png");
    textures.put("block/barrel_top_open.png");
    // Villagers
    textures.put("entity/villager/profession/armorer.png");
    textures.put("entity/villager/profession/butcher.png");
    textures.put("entity/villager/profession/cartographer.png");
    textures.put("entity/villager/profession/cleric.png");
    textures.put("entity/villager/profession/farmer.png");
    textures.put("entity/villager/profession/fisherman.png");
    textures.put("entity/villager/profession/fletcher.png");
    textures.put("entity/villager/profession/leatherworker.png");
    textures.put("entity/villager/profession/librarian.png");
    textures.put("entity/villager/profession/mason.png");
    textures.put("entity/villager/profession/nitwit.png");
    textures.put("entity/villager/profession/shepherd.png");
    textures.put("entity/villager/profession/toolsmith.png");
    textures.put("entity/villager/profession/weaponsmith.png");
    textures.put("entity/villager/profession_level/diamond.png");
    textures.put("entity/villager/profession_level/emerald.png");
    textures.put("entity/villager/profession_level/gold.png");
    textures.put("entity/villager/profession_level/iron.png");
    textures.put("entity/villager/profession_level/stone.png");
    textures.put("entity/villager/type/desert.png");
    textures.put("entity/villager/type/jungle.png");
    textures.put("entity/villager/type/plains.png");
    textures.put("entity/villager/type/savanna.png");
    textures.put("entity/villager/type/snow.png");
    textures.put("entity/villager/type/swamp.png");
    textures.put("entity/villager/type/taiga.png");
    textures.put("entity/villager/villager.png");

    AssetMoverAPI.fromMinecraft("1.18.1", textures);
  }
}
