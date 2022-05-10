package com.cleanroommc.millennium.proxy;

import com.cleanroommc.millennium.poi.IPOICapability;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    CapabilityManager.INSTANCE.register(IPOICapability.class, IPOICapability.Storage.INSTANCE, IPOICapability.Impl::new);
  }

  public void init(FMLInitializationEvent event) {}

  public void postInit(FMLPostInitializationEvent event) {}

  public void registerItemRenderer(Item item, int meta, String id) {}
}
