package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.util.ICachedTeleporter;
import com.cleanroommc.millennium.poi.IPOICapability;
import com.cleanroommc.millennium.poi.PointOfInterest;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Teleporter.class)
public abstract class TeleporterMixin implements ICachedTeleporter {
    @Shadow @Final protected Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache;

    @Shadow @Final protected WorldServer world;

    /**
     * @author embeddedt
     * @reason Don't clear old coordinates until the cache gets huge.
     */
    @Overwrite
    public void removeStalePortalLocations(long worldTime) {
        if (worldTime % 100L == 0L)
        {
            if(this.destinationCoordinateCache.size() > 65000) {
                this.clearDimensionCoordCache();
            }
        }
    }

    @Override
    public void clearAllDimensionCoordCaches()
    {
        MinecraftServer server = world.getMinecraftServer();
        if(server == null)
            return;
        for(int i = 0; i < server.worlds.length; ++i) {
            ((ICachedTeleporter)server.worlds[i].getDefaultTeleporter()).clearDimensionCoordCache();
        }
    }
    
    @Override
    public void clearDimensionCoordCache() {
        this.destinationCoordinateCache.clear();
    }

    /**
     * @author embeddedt
     * @reason Speed up searching for existing portals by using the POI system instead of scanning blockstates.
     * Also fixes vanilla issues with being teleported outside the world border.
     */
    @Inject(method = "placeInExistingPortal", at = @At("HEAD"), cancellable = true)
    private void overridePortalSearch(Entity entityIn, float rotationYaw, CallbackInfoReturnable<Boolean> cir) {
        int flooredEntityX = MathHelper.floor(entityIn.posX);
        int flooredEntityZ = MathHelper.floor(entityIn.posZ);
        long cachePosKey = ChunkPos.asLong(flooredEntityX, flooredEntityZ);
        Teleporter teleporterThis = (Teleporter) (Object) this;
        if(!this.destinationCoordinateCache.containsKey(cachePosKey)) {
            double bestPortalDistance = -1;
            int entityChunkX = flooredEntityX / 16;
            int entityChunkZ = flooredEntityZ / 16;
            BlockPos entityPosition = new BlockPos(entityIn);
            BlockPos desiredPortal = BlockPos.ORIGIN;
            WorldBorder border = world.getWorldBorder();
            for(int chunkX = -8; chunkX <= 8; chunkX++) {
                for(int chunkZ = -8; chunkZ <= 8; chunkZ++) {
                    if(!world.isChunkGeneratedAt(entityChunkX + chunkX, entityChunkZ + chunkZ)) {
                        continue;
                    }
                    Chunk chunk = world.getChunk(entityChunkX + chunkX, entityChunkZ + chunkZ);
                    BlockPos lowestPortalBlock;
                    for(BlockPos portalPos : IPOICapability.get(chunk).getAllLocationsOfType(PointOfInterest.NETHER_PORTAL)) {
                        if(!border.contains(portalPos))
                            continue;
                        for(lowestPortalBlock = new BlockPos(portalPos); this.world.getBlockState(lowestPortalBlock).getBlock() == Blocks.PORTAL; lowestPortalBlock = lowestPortalBlock.down()) {
                            portalPos = lowestPortalBlock;
                        }

                        double d1 = portalPos.distanceSq(entityPosition);
                        if (bestPortalDistance < 0.0D || d1 < bestPortalDistance) {
                            bestPortalDistance = d1;
                            desiredPortal = portalPos;
                        }
                    }
                }
            }
            if (bestPortalDistance >= 0.0D) {
                System.out.println(desiredPortal);
                this.destinationCoordinateCache.put(cachePosKey, teleporterThis.new PortalPosition(desiredPortal.toImmutable(), this.world.getTotalWorldTime()));
            } else {
                // No portal found
                cir.cancel();
                cir.setReturnValue(false);
            }
        }
    }
}
