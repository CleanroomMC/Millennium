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
    AssetMoverAPI.fromMinecraft("1.18.1", textures);
  }
}
