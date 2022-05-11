package com.cleanroommc.millennium.poi;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Predicate;

/**
 * Represents a POI at a given location in the world.
 */
public class PointOfInterest {
    private World world;
    private PointOfInterestType poiType;
    private BlockPos pos;
    private int reservations;

    public static final Predicate<PointOfInterest> ANY = poi -> true;

    public PointOfInterest(World world) {
        this.world = world;
        this.poiType = null;
        this.pos = null;
    }

    public PointOfInterest(PointOfInterestType type, World world, BlockPos pos) {
        this(type, world, pos, 0);
    }
    public PointOfInterest(PointOfInterestType type, World world, BlockPos pos, int reservations) {
        this.poiType = type;
        this.world = world;
        this.pos = pos;
        this.reservations = reservations;
    }

    public PointOfInterestType getType() {
        return poiType;
    }

    public BlockPos getPos() {
        return pos;
    }

    /**
     * Get the number of reservations currently held on this POI.
     */
    public int getReservations() {
        return reservations;
    }

    /**
     * Try to reserve this POI.
     */
    public boolean tryReserve() {
        if(this.reservations >= getType().getMaxReservations()) {
            return false;
        }
        this.reservations++;
        return true;
    }

    /**
     * Try to release this POI.
     */
    public boolean tryRelease() {
        if(this.reservations <= 0) {
            return false;
        }
        this.reservations--;
        return true;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("pos", pos.toLong());
        nbt.setString("id", String.valueOf(getType().getRegistryName()));
        nbt.setInteger("reserved", reservations);
        return nbt;
    }

    public void readFromNBT(World world, NBTTagCompound nbt) {
        pos = BlockPos.fromLong(nbt.getLong("pos"));
        ResourceLocation poiLoc = new ResourceLocation(nbt.getString("id"));
        PointOfInterestType poiType = GameRegistry.findRegistry(PointOfInterestType.class).getValue(poiLoc);
        if(poiType != null)
            this.poiType = poiType;
        else
            throw new IllegalArgumentException("Unexpected POI type: " + poiLoc);
        this.world = world;
        this.reservations = nbt.getInteger("reserved");
    }
}
