package io.github.cleanroommc.millennium.mixin;

import io.github.cleanroommc.millennium.common.blocks.MillenniumBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    int i = MathHelper.floor(posX);
    int j = MathHelper.floor(getEntityBoundingBox().minY);
    int k = MathHelper.floor(posZ);

    if (getEntityWorld()
        .getBlockState(new BlockPos(i, j, k))
        .getBlock()
        .equals(MillenniumBlocks.SCAFFOLDING_BLOCK)) {
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
    int i = MathHelper.floor(posX);
    int j = MathHelper.floor(getEntityBoundingBox().minY);
    int k = MathHelper.floor(posZ);

    if (getEntityWorld()
        .getBlockState(new BlockPos(i, j, k))
        .getBlock()
        .equals(MillenniumBlocks.SCAFFOLDING_BLOCK)) {
      motionY = 0.2;
    }
  }
}
