package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.client.font.TextRenderer;
import com.cleanroommc.millennium.common.tileentity.MillenniumSignTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(TileEntitySignRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class SignTileEntityRendererMixin {
  private final ThreadLocal<MillenniumSignTileEntity> te = new ThreadLocal<>();

  @Inject(
      method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V",
      at = @At(value = "HEAD"))
  private void injectTileEntity(
      TileEntitySign te,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float alpha,
      CallbackInfo ci) {
    if (te instanceof MillenniumSignTileEntity) {
      this.te.set((MillenniumSignTileEntity) te);
    }
  }

  @SuppressWarnings("InvalidInjectorMethodSignature")
  @Inject(
      method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I",
              ordinal = 1),
      locals = LocalCapture.CAPTURE_FAILSOFT)
  private void injectColoredTextRendering(
      TileEntitySign te,
      double x,
      double y,
      double z,
      float partialTicks,
      int destroyStage,
      float alpha,
      CallbackInfo ci,
      Block block,
      float f,
      FontRenderer fontrenderer,
      float f3,
      int i,
      int j,
      ITextComponent itextcomponent,
      List list,
      String s) {
    // te is always MillenniumSignTileEntity (probably not), fontrenderer is always TextRenderer
    if (te instanceof MillenniumSignTileEntity) {
      MillenniumSignTileEntity mte = (MillenniumSignTileEntity) te;

      if (mte.textColor != null && mte.isGlowingText) {
        TextRenderer tr = (TextRenderer) fontrenderer;

        tr.setDrawOutline(true);
        tr.drawString(
            s,
            -tr.getStringWidth(s) / 2,
            j * 10 - te.signText.length * 5,
            TextRenderer.getOutlineColor(mte.textColor));
        tr.setDrawOutline(false);
      }
    }
  }

  @ModifyArg(
      method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I",
              ordinal = 1),
      index = 3)
  private int modifyRenderingArgs(int x) {
    return te.get() != null
        ? te.get().textColor != null ? te.get().textColor.getColorValue() : x
        : x;
  }
}
