package com.cleanroommc.millennium.common.village;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.poi.PointOfInterestType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Represents a village-and-pillage profession.
 */
@Mod.EventBusSubscriber
public class VillagerProfession extends IForgeRegistryEntry.Impl<VillagerProfession> {
    public static ForgeRegistry<VillagerProfession> REGISTRY = null;
    private static final ResourceLocation REGISTRY_LOC = new ResourceLocation(Millennium.MODID, "villager_professions");

    private final PointOfInterestType workstation;

    @GameRegistry.ObjectHolder(Millennium.MODID + ":none")
    public static final VillagerProfession NONE = null;

    public VillagerProfession(PointOfInterestType workstation) {
        this.workstation = workstation;
    }

    @SubscribeEvent
    public static void setupRegistry(RegistryEvent.NewRegistry event) {
        REGISTRY = (ForgeRegistry<VillagerProfession>)new RegistryBuilder<VillagerProfession>()
                .setType(VillagerProfession.class)
                .setName(REGISTRY_LOC)
                .create();
    }

    @SubscribeEvent
    public static void registerDefaults(RegistryEvent.Register<VillagerProfession> event) {
        REGISTRY.register(new VillagerProfession(null).setRegistryName("none"));
        REGISTRY.register(new VillagerProfession(PointOfInterestType.BLAST_FURNACE).setRegistryName("armorer"));
    }
}
