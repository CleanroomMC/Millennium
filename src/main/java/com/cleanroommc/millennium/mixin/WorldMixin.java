package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.network.POIUpdateMessage;
import com.cleanroommc.millennium.poi.IPOICapability;
import com.cleanroommc.millennium.poi.PointOfInterest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow @Final public boolean isRemote;

    @Shadow @Final public WorldProvider provider;

    @Inject(method = "markAndNotifyBlock", at = @At("TAIL"), remap = false)
    private void setPOIInfo(BlockPos pos, Chunk chunk, IBlockState state, IBlockState newState, int flags, CallbackInfo ci) {
        IPOICapability cap = IPOICapability.get(chunk);
        if(cap != null) {
            PointOfInterest poi = PointOfInterest.forState(newState);
            PointOfInterest oldPoi = cap.getPOIs().get(pos.toLong());
            if(oldPoi != poi) {
                cap.setPOI(pos.toLong(), poi);
                chunk.markDirty();
                if ((flags & Constants.BlockFlags.SEND_TO_CLIENTS) != 0 && !this.isRemote) {
                    /* Send update to clients */
                    POIUpdateMessage msg = new POIUpdateMessage(pos.toLong(), poi);
                    Millennium.CHANNEL.sendToAllAround(msg, new NetworkRegistry.TargetPoint(this.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                }
            }
        }
    }
}
