package com.cleanroommc.millennium.client.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

// Internal usage only
class AssetHashMap extends Object2ObjectOpenHashMap<String, String> {
  private final String resourceType;

  AssetHashMap(String resourceType) {
    this.resourceType = resourceType;
  }

  void put(String subPath) {
    put(
        "assets/minecraft/" + resourceType + "/" + subPath,
        "assets/millennium/" + resourceType + "/" + subPath);
  }
}
