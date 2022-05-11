package com.cleanroommc.millennium.common.village;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.poi.PointOfInterestType;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber
public class VillagerType extends IForgeRegistryEntry.Impl<VillagerType> {
    public static ForgeRegistry<VillagerType> REGISTRY = null;
    private static final ResourceLocation REGISTRY_LOC = new ResourceLocation(Millennium.MODID, "villager_types");
    public VillagerType() {

    }
    @GameRegistry.ObjectHolder(Millennium.MODID + ":plains")
    public static final VillagerType PLAINS = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":desert")
    public static final VillagerType DESERT = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":jungle")
    public static final VillagerType JUNGLE = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":savanna")
    public static final VillagerType SAVANNA = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":snow")
    public static final VillagerType SNOW = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":swamp")
    public static final VillagerType SWAMP = null;
    @GameRegistry.ObjectHolder(Millennium.MODID + ":taiga")
    public static final VillagerType TAIGA = null;

    @SubscribeEvent
    public static void setupRegistry(RegistryEvent.NewRegistry event) {
        REGISTRY = (ForgeRegistry<VillagerType>)new RegistryBuilder<VillagerType>()
                .setType(VillagerType.class)
                .setName(REGISTRY_LOC)
                .create();
    }

    @SubscribeEvent
    public static void registerDefaults(RegistryEvent.Register<VillagerType> event) {
        ImmutableList.of("plains", "desert", "jungle", "savanna", "snow", "swamp", "taiga").forEach(id -> {
            REGISTRY.register(new VillagerType().setRegistryName(id));
        });
    }
}
