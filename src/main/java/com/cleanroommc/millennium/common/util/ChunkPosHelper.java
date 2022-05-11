package com.cleanroommc.millennium.common.util;

import net.minecraft.util.math.ChunkPos;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ChunkPosHelper {
    /**
     * Stream a set of chunk positions.
     * @param center The central chunk
     * @param radiusChunks radius of chunks around
     */
    public static Stream<ChunkPos> streamSquare(ChunkPos center, int radiusChunks) {
        return streamSquare(new ChunkPos(center.x - radiusChunks, center.z - radiusChunks), new ChunkPos(center.x + radiusChunks, center.z + radiusChunks));
    }

    /**
     * Stream a set of chunk positions.
     * @param start the starting position
     * @param end the ending position
     * @return
     */
    public static Stream<ChunkPos> streamSquare(ChunkPos start, ChunkPos end) {
        int width = Math.abs(end.x - start.x) + 1;
        int height = Math.abs(end.z - start.z) + 1;
        int xDelta = end.x > start.x ? 1 : -1;
        int zDelta = end.z > start.z ? 1 : -1;
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((long)width * height, Spliterator.NONNULL | Spliterator.SIZED | Spliterator.IMMUTABLE) {
            private ChunkPos currentPosition = null;
            @Override
            public boolean tryAdvance(Consumer<? super ChunkPos> consumer) {
                if(currentPosition == null) {
                    currentPosition = start;
                } else {
                    if(currentPosition.x == end.x) {
                        if(currentPosition.z == end.z) {
                            /* Reached the end on the X and Z axes */
                            return false;
                        } else
                            currentPosition = new ChunkPos(start.x, currentPosition.z + zDelta);
                    } else
                        currentPosition = new ChunkPos(currentPosition.x + xDelta, currentPosition.z);
                }
                consumer.accept(currentPosition);
                return true;
            }
        }, false);
    }
}
