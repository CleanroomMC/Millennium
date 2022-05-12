package com.cleanroommc.millennium.poi;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.util.ChunkPosHelper;
import com.cleanroommc.millennium.network.POIUpdateMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class PointOfInterestHelper {
    public static Stream<PointOfInterest> getInChunk(World world, ChunkPos chunkPos, Predicate<PointOfInterest> filter) {
        IChunkProvider provider = world.getChunkProvider();
        if(provider.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
            IPOICapability cap = IPOICapability.get(provider.provideChunk(chunkPos.x, chunkPos.z));
            if(cap != null) {
                return cap.getPOIs().values().stream().filter(filter);
            }
        }
        return Stream.empty();
    }
    public static Stream<PointOfInterest> getPOIsOfType(World world, BlockPos center, int radius, Predicate<PointOfInterest> filter) {
        int chunkRadius = Math.floorDiv(radius, 16) + 1;
        return ChunkPosHelper.streamSquare(new ChunkPos(center), chunkRadius).flatMap(chunkPos -> getInChunk(world, chunkPos, filter)).filter(poi -> {
            BlockPos poiPos = poi.getPos();
            return Math.abs(poiPos.getX() - center.getX()) <= radius && Math.abs(poiPos.getZ() - center.getZ()) <= radius;
        });
    }

    /**
     * Rescan the POI information for all blocks in a chunk.
     * @param chunk The chunk to scan
     */
    public static void rescanPOIs(Chunk chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for(int y = 0; y < chunk.getWorld().getHeight(); y++) {
            for(int z = 0; z < 16; z++) {
                for(int x = 0; x < 16; x++) {
                    pos.setPos(x, y, z);
                    rescanPOIAtLocation(chunk, pos, chunk.getBlockState(pos), true);
                }
            }
        }
    }

    /**
     * Rescan the POI information at a given location in a chunk.
     * @param chunk The chunk to scan
     * @param pos A location at which the POI information should be recomputed
     * @param newState The state to consider the block at this location as having.
     * @param sendToClients Whether or not to send the POI update to clients.
     */
    public static void rescanPOIAtLocation(Chunk chunk, BlockPos pos, IBlockState newState, boolean sendToClients) {
        IPOICapability cap = IPOICapability.get(chunk);
        if(cap != null) {
            PointOfInterestType poiType = PointOfInterestType.forState(newState);
            PointOfInterest oldPoi = cap.getPOIs().get(pos.toLong());
            PointOfInterestType oldPoiType = oldPoi != null ? oldPoi.getType() : null;
            if(oldPoiType != poiType) {
                cap.setPOI(pos.toLong(), poiType, true);
                System.out.println(pos + " changed to " + poiType);
                chunk.markDirty();
                if (sendToClients && !chunk.getWorld().isRemote) {
                    /* Send update to clients */
                    POIUpdateMessage msg = new POIUpdateMessage(pos.toLong(), poiType);
                    Millennium.CHANNEL.sendToAllAround(msg, new NetworkRegistry.TargetPoint(chunk.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                }
            }
        }
    }
}
