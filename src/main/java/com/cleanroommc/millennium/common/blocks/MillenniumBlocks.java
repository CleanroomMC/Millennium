package com.cleanroommc.millennium.common.blocks;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.items.MillenniumItems;
import com.cleanroommc.millennium.common.tileentity.BarrelBlockTileEntity;
import com.cleanroommc.millennium.common.tileentity.MillenniumSignTileEntity;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MillenniumBlocks {
  private static final List<MillenniumBlock> BLOCKS = new ArrayList<>();
  private static final Map<Class<? extends TileEntity>, String> TILE_ENTITIES =
      new Object2ObjectOpenHashMap<>();

  static {
    register("sign", MillenniumSignTileEntity.class);
  }

  public static final BarrelBlock BARREL_BLOCK =
      register("barrel", new BarrelBlock(), BarrelBlockTileEntity.class);

  public static final ScaffoldingBlock SCAFFOLDING_BLOCK =
          register("scaffolding", new ScaffoldingBlock());

  private static <T extends MillenniumBlock> T register(String id, T block) {
    block.setRegistryName(Millennium.MODID, id);
    BLOCKS.add(block);
    MillenniumItems.ITEM_BLOCKS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    return block;
  }

  private static <T extends MillenniumBlock, TE extends TileEntity> T register(
      String id, T block, Class<TE> tileEntity) {
    T registeredBlock = register(id, block);
    TILE_ENTITIES.put(tileEntity, id);
    return registeredBlock;
  }

  private static void register(String id, Class<? extends TileEntity> tileEntity) {
    TILE_ENTITIES.put(tileEntity, id);
  }

  public static void registerBlock(IForgeRegistry<Block> registry) {
    for (Block block : BLOCKS) {
      registry.register(block);
    }

    for (Map.Entry<Class<? extends TileEntity>, String> entry : TILE_ENTITIES.entrySet()) {
      GameRegistry.registerTileEntity(
          entry.getKey(), new ResourceLocation(Millennium.MODID, entry.getValue()));
    }
  }

  public static void registerModel() {
    for (MillenniumBlock block : BLOCKS) block.registerModel();
  }
}
