package com.cleanroommc.millennium.network;

import com.cleanroommc.millennium.poi.IPOICapability;
import com.cleanroommc.millennium.poi.PointOfInterest;
import com.cleanroommc.millennium.poi.PointOfInterestType;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class POIBulkUpdateMessage implements IMessage {
    @Nonnull
    public Set<Pair<Long, Integer>> data = new HashSet<>();
    public int chunkX, chunkZ;

    public POIBulkUpdateMessage() {

    }

    public POIBulkUpdateMessage(@Nonnull ChunkPos chunkPos, @Nonnull Long2ObjectMap<PointOfInterest> map) {
        map.forEach((pos, poi) -> data.add(Pair.of(pos, poi != null ? poi.getType().serialize() : -1)));
        chunkX = chunkPos.x;
        chunkZ = chunkPos.z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            long pos = buf.readLong();
            int poiId = buf.readInt();
            data.add(Pair.of(pos, poiId));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
        buf.writeInt(data.size());
        data.forEach(pair -> {
           buf.writeLong(pair.getLeft());
           buf.writeInt(pair.getRight());
        });
    }

    public enum Handler implements IMessageHandler<POIBulkUpdateMessage, IMessage> {
        INSTANCE;

        @Override
        public IMessage onMessage(POIBulkUpdateMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Chunk chunk = Minecraft.getMinecraft().world.getChunk(message.chunkX, message.chunkZ);
                IPOICapability cap = IPOICapability.get(chunk);
                if(cap != null) {
                    cap.clearPOIs();
                    message.data.forEach(pair -> {
                       long pos = pair.getLeft();
                       PointOfInterestType poi = PointOfInterestType.deserialize(pair.getRight());
                       cap.setPOI(pos, poi, false);
                    });
                    chunk.markDirty();
                }
            });
            return null;
        }
    }
}
