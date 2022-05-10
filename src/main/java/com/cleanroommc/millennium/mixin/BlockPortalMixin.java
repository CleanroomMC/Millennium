package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.util.ICachedTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPortal.class)
public class BlockPortalMixin {
    @Inject(method = "trySpawnPortal", at = @At("RETURN"))
    private void clearCacheEntriesWhenSpawning(World worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(!worldIn.isRemote && cir.getReturnValue()) {
            ((ICachedTeleporter)((WorldServer)worldIn).getDefaultTeleporter()).clearAllDimensionCoordCaches();
        }
    }

    @Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z", shift = At.Shift.AFTER))
    private void clearCacheEntries(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, CallbackInfo ci) {
        if(!worldIn.isRemote)
            ((ICachedTeleporter)((WorldServer)worldIn).getDefaultTeleporter()).clearAllDimensionCoordCaches();
    }

}
