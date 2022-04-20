package io.github.cleanroommc.millennium.client.font;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextRenderer extends FontRenderer {
  private boolean drawOutline;

  public TextRenderer(
      GameSettings gameSettingsIn,
      ResourceLocation location,
      TextureManager textureManagerIn,
      boolean unicode) {
    super(gameSettingsIn, location, textureManagerIn, unicode);
  }

  @Override
  protected float renderDefaultChar(int ch, boolean italic) {
    if (!drawOutline) return super.renderDefaultChar(ch, italic);

    float lastValue = 0;
    float orgX = posX, orgY = posY;

    for (int j = -1; j < 2; j++)
      for (int k = -1; k < 2; k++) {
        posX = orgX + j;
        posY = orgY + k;
        lastValue = super.renderDefaultChar(ch, italic);
      }

    posX = orgX;
    posY = orgY;

    return lastValue;
  }

  @Override
  protected float renderUnicodeChar(char ch, boolean italic) {
    if (!drawOutline) return super.renderUnicodeChar(ch, italic);

    float lastValue = 0;
    float orgX = posX, orgY = posY;

    for (float j = -0.5F; j < 1.5; j += 0.5)
      for (float k = -0.5F; k < 1.5; k += 0.5) {
        posX = orgX + j;
        posY = orgY + k;
        lastValue = super.renderUnicodeChar(ch, italic);
      }

    posX = orgX;
    posY = orgY;

    return lastValue;
  }

  public void renderStringWithOutline(String text, float x, float y, int color, boolean shadow) {
    Optional<EnumDyeColor> dyeColor =
        Arrays.stream(EnumDyeColor.values())
            .filter(clr -> clr.getColorValue() == color)
            .collect(Collectors.toList())
            .stream()
            .findFirst(); // Should only have up to one match or none
    int outlineColor = getOutlineColor(dyeColor.orElse(EnumDyeColor.BLACK));

    setDrawOutline(true);
    drawString(text, x, y, outlineColor, shadow);
    setDrawOutline(false);
    drawString(text, x, y, color, shadow);
  }

  public void setDrawOutline(boolean drawOutline) {
    this.drawOutline = drawOutline;
  }

  public static int getOutlineColor(EnumDyeColor color) {
    switch (color) {
      case WHITE:
        return 0x656565;
      case ORANGE:
        return 0x65280C;
      case MAGENTA:
        return 0x650065;
      case LIGHT_BLUE:
        return 0x3C4B51;
      case YELLOW:
        return 0x656500;
      case LIME:
        return 0x4B6500;
      case PINK:
        return 0x652947;
      case GRAY:
        return 0x323232;
      case SILVER:
        return 0x535353;
      case CYAN:
        return 0x006565;
      case PURPLE:
        return 0x3F0C5F;
      case BLUE:
        return 0x000065;
      case BROWN:
        return 0x361B07;
      case GREEN:
        return 0x006500;
      case RED:
        return 0x650000;
      case BLACK:
      default:
        return 0xEDE8CA;
    }
  }
}
