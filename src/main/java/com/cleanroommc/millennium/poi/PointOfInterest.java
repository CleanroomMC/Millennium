package com.cleanroommc.millennium.poi;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.network.POIBulkUpdateMessage;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a block which needs to be found quickly within a world.
 */
@Mod.EventBusSubscriber
public class PointOfInterest extends IForgeRegistryEntry.Impl<PointOfInterest> {
    private static IForgeRegistry<PointOfInterest> REGISTRY = null;
    private static final ResourceLocation POI_CAP = new ResourceLocation(Millennium.MODID, "poi");
    private static final ResourceLocation STATE_TO_POI = new ResourceLocation(Millennium.MODID, "state_to_poi");

    @GameRegistry.ObjectHolder(Millennium.MODID + ":nether_portal")
    public static final PointOfInterest NETHER_PORTAL = null;

    public static Set<IBlockState> allStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getBlockState().getValidStates());
    }

    private final Set<IBlockState> validStates;

    public PointOfInterest(Set<IBlockState> states) {
        validStates = states;
    }

    public Set<IBlockState> getStates() {
        return validStates;
    }

    @Override
    public String toString() {
        return getRegistryName().toString() + "[" + Joiner.on(",").join(validStates) + "]";
    }

    public static PointOfInterest forState(IBlockState state) {
        @SuppressWarnings("unchecked")
        HashMap<IBlockState, PointOfInterest> poiMap = REGISTRY.getSlaveMap(STATE_TO_POI, HashMap.class);
        return poiMap.get(state);
    }

    public int serialize() {
        return ((ForgeRegistry<PointOfInterest>)REGISTRY).getID(this);
    }

    public static PointOfInterest deserialize(int id) {
        if(id == -1)
            return null;
        return ((ForgeRegistry<PointOfInterest>)REGISTRY).getValue(id);
    }

    private static class POICallbacks implements IForgeRegistry.CreateCallback<PointOfInterest>, IForgeRegistry.AddCallback<PointOfInterest>, IForgeRegistry.ClearCallback<PointOfInterest> {
        @Override
        public void onAdd(IForgeRegistryInternal<PointOfInterest> owner, RegistryManager stage, int id, PointOfInterest poi, @Nullable PointOfInterest oldPoi) {
            @SuppressWarnings("unchecked")
            HashMap<IBlockState, PointOfInterest> poiMap = owner.getSlaveMap(STATE_TO_POI, HashMap.class);
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
        public void onCreate(IForgeRegistryInternal<PointOfInterest> owner, RegistryManager stage) {
            owner.setSlaveMap(STATE_TO_POI, new HashMap<IBlockState, PointOfInterest>());
        }

        @Override
        public void onClear(IForgeRegistryInternal<PointOfInterest> owner, RegistryManager stage) {
            owner.getSlaveMap(STATE_TO_POI, HashMap.class).clear();
        }
    }


    @SubscribeEvent
    public static void setupRegistry(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<PointOfInterest>()
                .setType(PointOfInterest.class)
                .setName(POI_CAP)
                .addCallback(new POICallbacks())
                .create();
    }

    @SubscribeEvent
    public static void registerDefaults(RegistryEvent.Register<PointOfInterest> event) {
        REGISTRY.register(new PointOfInterest(PointOfInterest.allStatesOfBlock(Blocks.PORTAL)).setRegistryName("nether_portal"));
    }

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Chunk> event) {
        event.addCapability(POI_CAP, new IPOICapability.Provider());
    }

    @SubscribeEvent
    public static void sendToPlayer(@Nonnull ChunkWatchEvent.Watch event) {
        final @Nullable IPOICapability cap = IPOICapability.get(event.getChunkInstance());
        if(cap != null) Millennium.CHANNEL.sendTo(new POIBulkUpdateMessage(event.getChunk(), cap.getPOIs()), event.getPlayer());
    }
}
