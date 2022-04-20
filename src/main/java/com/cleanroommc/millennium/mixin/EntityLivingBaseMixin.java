package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.blocks.MillenniumBlocks;
import com.cleanroommc.millennium.common.blocks.ScaffoldingBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
    public EntityLivingBaseMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "isOnLadder",
            at = @At(value = "RETURN", ordinal = 1),
            cancellable = true)
    private void injectScaffoldingClimbingCheck(CallbackInfoReturnable<Boolean> cir) {
        if (isInScaffolding(world, getEntityBoundingBox())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "onLivingUpdate",
            at =
            @At(
                    value = "INVOKE_ASSIGN",
                    target = "net/minecraft/entity/EntityLivingBase.isInWater ()Z",
                    shift = At.Shift.BEFORE))
    private void injectScaffoldingClimbing(CallbackInfo ci) {
        if (isInScaffolding(world, getEntityBoundingBox())) {
            motionY = 0.2;
        }
    }

    private boolean isInScaffolding(World world, AxisAlignedBB bb) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.ceil(bb.maxX);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.ceil(bb.maxY);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos pooled = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = minX; l3 < maxX; ++l3) {
            for (int i4 = minY; i4 < maxY; ++i4) {
                for (int j4 = minZ; j4 < maxZ; ++j4) {
                    IBlockState state = world.getBlockState(pooled.setPos(l3, i4, j4));
                    if (state.getBlock() instanceof ScaffoldingBlock) {
                        pooled.release();
                        return true;
                    }
                }
            }
        }

        pooled.release();
        return false;
    }
}
