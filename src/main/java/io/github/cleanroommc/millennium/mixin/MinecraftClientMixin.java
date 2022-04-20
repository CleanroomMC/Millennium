package io.github.cleanroommc.millennium.mixin;

import io.github.cleanroommc.millennium.client.font.TextRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MinecraftClientMixin {
    @Redirect(method = "init", at = @At(value = "NEW", target = "net/minecraft/client/gui/FontRenderer"))
    private FontRenderer redirectFontRendererCtor(GameSettings k1, ResourceLocation l1, TextureManager j, boolean k) {
        return new TextRenderer(k1, l1, j, k);
    }
}
