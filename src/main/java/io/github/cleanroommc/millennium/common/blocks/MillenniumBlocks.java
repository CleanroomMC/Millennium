package io.github.cleanroommc.millennium.common.blocks;

import io.github.cleanroommc.millennium.Millennium;
import io.github.cleanroommc.millennium.common.items.MillenniumItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class MillenniumBlocks {
  private static final List<MillenniumBlock> BLOCKS = new ArrayList<>();

  private static <T extends MillenniumBlock> T register(String id, T block) {
    block.setRegistryName(Millennium.MODID, id);
    BLOCKS.add(block);
    MillenniumItems.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    return block;
  }

  public static void registerBlock(IForgeRegistry<Block> registry) {
    for (Block block : BLOCKS) {
      registry.register(block);
    }
  }
}
