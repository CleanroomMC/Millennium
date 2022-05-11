package com.cleanroommc.millennium.common.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Village-and-Pillage-style villager.
 */
public class EntityNewVillager extends EntityVillager {
    public enum BiomeType {
        PLAINS,
        DESERT,
        SAVANNA,
        SNOW,

    }
    public EntityNewVillager(World worldIn) {
        super(worldIn);
    }
}
