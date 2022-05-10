package com.cleanroommc.millennium.poi;

import com.google.common.collect.HashMultimap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to store the locations of points-of-interest in the world.
 */
public interface IPOICapability {
    @CapabilityInject(IPOICapability.class)
    Capability<IPOICapability> CAPABILITY = null;

    @Nonnull Long2ObjectMap<PointOfInterest> getPOIs();

    @Nonnull Set<BlockPos> getAllLocationsOfType(PointOfInterest poi);


    void setPOI(long pos, PointOfInterest fluid);

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static IPOICapability get(@Nullable ICapabilityProvider p) {
        if(p == null)
            return null;
        return p.getCapability(CAPABILITY, null);
    }

    //default implementation
    class Impl implements IPOICapability
    {
        @Nonnull
        protected final Long2ObjectMap<PointOfInterest> pointsOfInterest = new Long2ObjectOpenHashMap<>();

        protected final HashMultimap<PointOfInterest, Long> poiLocations = HashMultimap.create();

        @Nonnull
        @Override
        public Long2ObjectMap<PointOfInterest> getPOIs() { return pointsOfInterest; }

        @Override
        public void setPOI(long pos, PointOfInterest poi) {
            if(poi == null) {
                PointOfInterest oldPoi = pointsOfInterest.get(pos);
                if(oldPoi != null) {
                    pointsOfInterest.remove(pos);
                    poiLocations.remove(oldPoi, pos);
                }
            } else {
                pointsOfInterest.put(pos, poi);
                poiLocations.put(poi, pos);
            }
        }

        @Nonnull
        @Override
        public Set<BlockPos> getAllLocationsOfType(PointOfInterest poi) {
             return poiLocations.get(poi).stream().map(BlockPos::fromLong).collect(Collectors.toSet());
        }
    }

    @SuppressWarnings("ConstantConditions")
    final class Provider implements ICapabilitySerializable<NBTBase>
    {
        final IPOICapability instance = CAPABILITY.getDefaultInstance();

        @Override
        public boolean hasCapability(@Nullable Capability<?> capabilityIn, @Nullable EnumFacing facing) {
            return capabilityIn == CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nullable Capability<T> capabilityIn, @Nullable EnumFacing facing) {
            if(!hasCapability(capabilityIn, facing)) return null;
            else return CAPABILITY.cast(instance);
        }

        @Nonnull
        @Override
        public NBTBase serializeNBT() { return CAPABILITY.writeNBT(instance, null); }

        @Override
        public void deserializeNBT(@Nonnull NBTBase nbt) { CAPABILITY.readNBT(instance, null, nbt); }
    }

    enum Storage implements Capability.IStorage<IPOICapability>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nullable Capability<IPOICapability> capability, @Nonnull IPOICapability instance, @Nullable EnumFacing side) {
            final NBTTagList list = new NBTTagList();

            for(Map.Entry<Long, PointOfInterest> entry : instance.getPOIs().entrySet()) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setLong("pos", entry.getKey());
                nbt.setString("id", String.valueOf(entry.getValue().getRegistryName()));
                list.appendTag(nbt);
            }

            return list;
        }

        @Override
        public void readNBT(@Nullable Capability<IPOICapability> capability, @Nonnull IPOICapability instance, @Nullable EnumFacing side, @Nullable NBTBase nbtIn) {
            if(nbtIn instanceof NBTTagList) {
                for(NBTBase tag : (NBTTagList)nbtIn) {
                    if(tag instanceof NBTTagCompound) {
                        NBTTagCompound nbt = (NBTTagCompound)tag;
                        if(nbt.hasKey("id", Constants.NBT.TAG_STRING) && nbt.hasKey("pos", Constants.NBT.TAG_LONG)) {
                            PointOfInterest poi = GameRegistry.findRegistry(PointOfInterest.class).getValue(new ResourceLocation(nbt.getString("id")));
                            if(poi != null)
                                instance.setPOI(nbt.getLong("pos"), poi);
                        }
                    }
                }
            }
        }
    }
}
