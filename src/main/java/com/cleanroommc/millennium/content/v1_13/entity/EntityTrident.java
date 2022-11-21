package com.cleanroommc.millennium.content.v1_13.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityTrident extends Entity {

    private static final DataParameter<Byte> LOYALTY = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> ENCHANTED = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BOOLEAN);

    // private ItemStack tridentStack = new ItemStack(Items.TRIDENT);
    private boolean dealtDamage;
    public int returnTimer;

    public EntityTrident(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

}
