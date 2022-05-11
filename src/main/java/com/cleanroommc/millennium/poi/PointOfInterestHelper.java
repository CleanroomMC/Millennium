package com.cleanroommc.millennium.poi;

import com.cleanroommc.millennium.common.util.ChunkPosHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

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
}
