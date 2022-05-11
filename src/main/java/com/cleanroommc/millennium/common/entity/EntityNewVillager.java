package com.cleanroommc.millennium.common.entity;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.village.VillagerProfession;
import com.cleanroommc.millennium.common.village.VillagerType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nullable;

/**
 * Village-and-Pillage-style villager.
 */
public class EntityNewVillager extends EntityAgeable {
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityNewVillager.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityNewVillager.class, DataSerializers.VARINT);
    public enum BiomeType {
        PLAINS,
        DESERT,
        SAVANNA,
        SNOW,
    }
    public EntityNewVillager(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.setProfession(VillagerProfession.NONE);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PROFESSION, 0);
        this.dataManager.register(TYPE, 0);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityNewVillager entityvillager = new EntityNewVillager(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_VILLAGER;
    }

    public VillagerProfession getProfession() {
        return VillagerProfession.REGISTRY.getValue(this.dataManager.get(PROFESSION));
    }

    public void setProfession(VillagerProfession type) {
        this.dataManager.set(PROFESSION, VillagerProfession.REGISTRY.getID(type));
    }

    public VillagerType getType() {
        return VillagerType.REGISTRY.getValue(this.dataManager.get(TYPE));
    }

    public void setType(VillagerType type) {
        this.dataManager.set(TYPE, VillagerType.REGISTRY.getID(type));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setString("Type", getType().getRegistryName().toString());
        compound.setString("Profession", getProfession().getRegistryName().toString());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("Type")) {
            setType(VillagerType.REGISTRY.getValue(new ResourceLocation(compound.getString("Type"))));
        }
        if(compound.hasKey("Profession")) {
            setProfession(VillagerProfession.REGISTRY.getValue(new ResourceLocation(compound.getString("Profession"))));
        }
    }

    public int getProfessionLevel() {
        return 0;
    }
}
