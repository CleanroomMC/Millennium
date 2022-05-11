package com.cleanroommc.millennium.proxy;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.entity.EntityNewVillager;
import com.cleanroommc.millennium.poi.IPOICapability;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    CapabilityManager.INSTANCE.register(IPOICapability.class, IPOICapability.Storage.INSTANCE, IPOICapability.Impl::new);
    EntityRegistry.registerModEntity(new ResourceLocation(Millennium.MODID, "villager"), EntityNewVillager.class, "villager_v2", 0, Millennium.INSTANCE, 64, 3, true);
  }

  public void init(FMLInitializationEvent event) {}

  public void postInit(FMLPostInitializationEvent event) {}

  public void registerItemRenderer(Item item, int meta, String id) {}
}
