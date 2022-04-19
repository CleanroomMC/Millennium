package com.cleanroommc.millennium;

import com.cleanroommc.millennium.client.sounds.BarrelSoundEvents;
import com.cleanroommc.millennium.client.sounds.BundleSoundEvents;
import com.cleanroommc.millennium.common.blocks.MillenniumBlocks;
import com.cleanroommc.millennium.common.items.MillenniumItems;
import com.cleanroommc.millennium.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Millennium.MODID, name = Millennium.NAME, version = Millennium.VERSION)
public class Millennium {
  public static final String MODID = "millennium";
  public static final String NAME = "Millennium";
  public static final String VERSION = "@VERSION@";

  @Mod.Instance public static Millennium INSTANCE;

  @SidedProxy(
      clientSide = "com.cleanroommc.millennium.proxy.ClientProxy",
      serverSide = "com.cleanroommc.millennium.proxy.CommonProxy")
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void construct(FMLConstructionEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }

  @SubscribeEvent
  public void onBlockRegister(RegistryEvent.Register<Block> event) {
    MillenniumBlocks.registerBlock(event.getRegistry());
  }

  @SubscribeEvent
  public void onItemRegister(RegistryEvent.Register<Item> event) {
    MillenniumItems.registerItem(event.getRegistry());
  }

  @SubscribeEvent
  public void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
    BundleSoundEvents.init(event.getRegistry());
    BarrelSoundEvents.init(event.getRegistry());
  }

  @SubscribeEvent
  public void onItemModelRegister(ModelRegistryEvent event) {
    MillenniumItems.registerModel();
    MillenniumBlocks.registerModel();
  }
}
