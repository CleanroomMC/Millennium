package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.items.MillenniumItems;
import com.cleanroommc.millennium.common.tileentity.MillenniumSignTileEntity;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSign.class)
public abstract class SignBlockMixin {
  @Inject(method = "createNewTileEntity", at = @At(value = "HEAD"), cancellable = true)
  private void injectTileEntity(World worldIn, int meta, CallbackInfoReturnable<TileEntity> cir) {
    cir.setReturnValue(new MillenniumSignTileEntity());
  }

  @Inject(method = "onBlockActivated", at = @At(value = "HEAD"), cancellable = true)
  private void injectOnBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      CallbackInfoReturnable<Boolean> cir) {
    TileEntity te = worldIn.getTileEntity(pos);

    if (te instanceof MillenniumSignTileEntity) {
      MillenniumSignTileEntity mte = (MillenniumSignTileEntity) te;
      ItemStack stack = playerIn.getHeldItem(hand);
      Item item = stack.getItem();

      if (stack.getItem() instanceof ItemDye) {
        ItemDye dye = (ItemDye) item;

        mte.setColor(EnumDyeColor.byDyeDamage(dye.getDamage(stack)));

        cir.setReturnValue(true);
      } else if (stack.getItem().equals(MillenniumItems.GLOW_INK_SAC)) {
        mte.isGlowingText = true;

        cir.setReturnValue(true);
      }
    }
  }
}
