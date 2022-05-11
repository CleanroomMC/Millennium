package com.cleanroommc.millennium.common.entity.villager;

import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * 1.14 villager profession.
 */
public class NewVillagerProfession extends VillagerRegistry.VillagerProfession {
    public NewVillagerProfession(String name, String texture, String zombie) {
        super(name, texture, zombie);
    }

    @Override
    public VillagerRegistry.VillagerCareer getCareer(int id) {
        return super.getCareer(id);
    }
}
