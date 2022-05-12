package com.cleanroommc.millennium.poi;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.network.POIBulkUpdateMessage;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a block which needs to be found quickly within a world.
 */
@Mod.EventBusSubscriber
public class PointOfInterestType extends IForgeRegistryEntry.Impl<PointOfInterestType> {
    private static IForgeRegistry<PointOfInterestType> REGISTRY = null;
    private static final ResourceLocation POI_CAP = new ResourceLocation(Millennium.MODID, "poi");
    private static final ResourceLocation STATE_TO_POI = new ResourceLocation(Millennium.MODID, "state_to_poi");

    @GameRegistry.ObjectHolder(Millennium.MODID + ":nether_portal")
    public static final PointOfInterestType NETHER_PORTAL = null;

    public static Set<IBlockState> allStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getBlockState().getValidStates());
    }

    private final Set<IBlockState> validStates;
    private final int maxReservations;

    public PointOfInterestType(Set<IBlockState> states, int maxReservations) {
        this.validStates = states;
        this.maxReservations = maxReservations;
    }

    public Set<IBlockState> getStates() {
        return validStates;
    }

    public int getMaxReservations() {
        return maxReservations;
    }

    public PointOfInterest createPOI(World world, BlockPos pos, int reservations) {
        return new PointOfInterest(this, world, pos, reservations);
    }

    public PointOfInterest createPOIEmpty() {
        return new PointOfInterest(null);
    }

    @Override
    public String toString() {
        return getRegistryName().toString() + "[" + Joiner.on(",").join(validStates) + "]";
    }

    public static PointOfInterestType forState(IBlockState state) {
        @SuppressWarnings("unchecked")
        HashMap<IBlockState, PointOfInterestType> poiMap = REGISTRY.getSlaveMap(STATE_TO_POI, HashMap.class);
        return poiMap.get(state);
    }

    public int serialize() {
        return ((ForgeRegistry<PointOfInterestType>)REGISTRY).getID(this);
    }

    public static PointOfInterestType deserialize(int id) {
        if(id == -1)
            return null;
        return ((ForgeRegistry<PointOfInterestType>)REGISTRY).getValue(id);
    }

    private static class POICallbacks implements IForgeRegistry.CreateCallback<PointOfInterestType>, IForgeRegistry.AddCallback<PointOfInterestType>, IForgeRegistry.ClearCallback<PointOfInterestType> {
        @Override
        public void onAdd(IForgeRegistryInternal<PointOfInterestType> owner, RegistryManager stage, int id, PointOfInterestType poi, @Nullable PointOfInterestType oldPoi) {
            @SuppressWarnings("unchecked")
            HashMap<IBlockState, PointOfInterestType> poiMap = owner.getSlaveMap(STATE_TO_POI, HashMap.class);
            if(oldPoi != null) {
                for(IBlockState state : oldPoi.getStates()) {
                    poiMap.remove(state);
                }
            }
            for(IBlockState state : poi.getStates()) {
                poiMap.put(state, poi);
            }
        }

        @Override
        public void onCreate(IForgeRegistryInternal<PointOfInterestType> owner, RegistryManager stage) {
            owner.setSlaveMap(STATE_TO_POI, new HashMap<IBlockState, PointOfInterestType>());
        }

        @Override
        public void onClear(IForgeRegistryInternal<PointOfInterestType> owner, RegistryManager stage) {
            owner.getSlaveMap(STATE_TO_POI, HashMap.class).clear();
        }
    }


    @SubscribeEvent
    public static void setupRegistry(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<PointOfInterestType>()
                .setType(PointOfInterestType.class)
                .setName(POI_CAP)
                .addCallback(new POICallbacks())
                .create();
    }

    @SubscribeEvent
    public static void registerDefaults(RegistryEvent.Register<PointOfInterestType> event) {
        REGISTRY.register(new PointOfInterestType(PointOfInterestType.allStatesOfBlock(Blocks.PORTAL), 0).setRegistryName("nether_portal"));
    }

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Chunk> event) {
        event.addCapability(POI_CAP, new IPOICapability.Provider(event.getObject()));
    }

    @SubscribeEvent
    public static void sendToPlayer(@Nonnull ChunkWatchEvent.Watch event) {
        final @Nullable IPOICapability cap = IPOICapability.get(event.getChunkInstance());
        if(cap != null) Millennium.CHANNEL.sendTo(new POIBulkUpdateMessage(event.getChunk(), cap.getPOIs()), event.getPlayer());
    }

    @SubscribeEvent
    public static void rescanIfNeeded(@Nonnull ChunkEvent.Load event) {
        final @Nullable IPOICapability cap = IPOICapability.get(event.getChunk());
        if(cap != null) {
            if(cap.needsRescan()) {
                PointOfInterestHelper.rescanPOIs(event.getChunk());
                cap.setNeedsRescan(false);
            }
        }
    }
}
