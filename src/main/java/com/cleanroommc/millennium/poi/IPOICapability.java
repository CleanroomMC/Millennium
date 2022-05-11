package com.cleanroommc.millennium.poi;

import com.cleanroommc.millennium.poi.event.POIEvent;
import com.google.common.collect.HashMultimap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    @Nonnull Set<BlockPos> getAllLocationsOfType(PointOfInterestType poi);

    @Nonnull Set<PointOfInterest> getAllPOIsOfType(PointOfInterestType poi);

    Chunk getChunk();


    void setPOI(long pos, PointOfInterestType poiType, boolean fireEvents);

    void clearPOIs();

    @SuppressWarnings("ConstantConditions")
    @Nullable
    static IPOICapability get(@Nullable Chunk p) {
        if(p == null)
            return null;
        return p.getCapability(CAPABILITY, null);
    }

    //default implementation
    class Impl implements IPOICapability
    {
        private final Chunk chunk;

        public Impl() {
            throw new IllegalStateException("Do not use this constructor");
        }

        public Impl(Chunk chunk) {
            this.chunk = chunk;
        }
        @Nonnull
        protected final Long2ObjectMap<PointOfInterest> pointsOfInterest = new Long2ObjectOpenHashMap<>();

        protected final HashMultimap<PointOfInterestType, Long> poiLocations = HashMultimap.create();

        @Override
        public Chunk getChunk() {
            return chunk;
        }

        @Nonnull
        @Override
        public Long2ObjectMap<PointOfInterest> getPOIs() { return pointsOfInterest; }

        @Override
        public void setPOI(long pos, PointOfInterestType poiType, boolean fireEvents) {
            if(poiType == null) {
                PointOfInterest oldPoi = pointsOfInterest.get(pos);
                if(fireEvents && oldPoi != null) {
                    MinecraftForge.EVENT_BUS.post(new POIEvent.RemovedEvent(oldPoi));
                }
                setPOIObject(pos, null, null);
            } else {
                PointOfInterest poi = poiType.createPOI(chunk.getWorld(), BlockPos.fromLong(pos), 0);
                setPOIObject(pos, poiType, poi);
                if(fireEvents)
                    MinecraftForge.EVENT_BUS.post(new POIEvent.AddedEvent(poi));
            }
        }

        protected void setPOIObject(long pos, PointOfInterestType poiType, PointOfInterest poi) {
            if(poiType == null) {
                PointOfInterest oldPoi = pointsOfInterest.get(pos);
                if(oldPoi != null) {
                    pointsOfInterest.remove(pos);
                    poiLocations.remove(oldPoi.getType(), pos);
                }
            } else {
                pointsOfInterest.put(pos, poi);
                poiLocations.put(poiType, pos);
            }
        }

        @Override
        public void clearPOIs() {
            pointsOfInterest.clear();
            poiLocations.clear();
        }

        @Nonnull
        @Override
        public Set<BlockPos> getAllLocationsOfType(PointOfInterestType poi) {
             return poiLocations.get(poi).stream().map(BlockPos::fromLong).collect(Collectors.toSet());
        }

        @Nonnull
        @Override
        public Set<PointOfInterest> getAllPOIsOfType(PointOfInterestType poi) {
            return poiLocations.get(poi).stream().map(pointsOfInterest::get).collect(Collectors.toSet());
        }
    }

    @SuppressWarnings("ConstantConditions")
    final class Provider implements ICapabilitySerializable<NBTBase>
    {
        final IPOICapability instance;
        final Chunk chunk;

        public Provider(Chunk chunk) {
            this.chunk = chunk;
            this.instance = new Impl(chunk);
        }

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
                nbt = entry.getValue().writeToNBT(nbt);
                list.appendTag(nbt);
            }

            return list;
        }

        @Override
        public void readNBT(@Nullable Capability<IPOICapability> capability, @Nonnull IPOICapability instance, @Nullable EnumFacing side, @Nullable NBTBase nbtIn) {
            if(nbtIn instanceof NBTTagList) {
                instance.clearPOIs();
                IForgeRegistry<PointOfInterestType> typeRegistry = GameRegistry.findRegistry(PointOfInterestType.class);
                for(NBTBase tag : (NBTTagList)nbtIn) {
                    if(tag instanceof NBTTagCompound) {
                        NBTTagCompound nbt = (NBTTagCompound)tag;
                        if(nbt.hasKey("id", Constants.NBT.TAG_STRING)) {
                            PointOfInterestType type = typeRegistry.getValue(new ResourceLocation(nbt.getString("id")));
                            if(type != null) {
                                PointOfInterest poi = type.createPOIEmpty();
                                poi.readFromNBT(instance.getChunk().getWorld(), nbt);
                                ((Impl)instance).setPOIObject(poi.getPos().toLong(), poi.getType(), poi);
                            }
                        }
                    }
                }
            }
        }
    }
}
