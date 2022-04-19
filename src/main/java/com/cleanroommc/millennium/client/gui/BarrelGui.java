package com.cleanroommc.millennium.client.gui;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.container.BarrelContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class BarrelGui extends GuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(Millennium.MODID, "textures/gui/container/shulker_box.png");
  private final IInventory playerInv;
  private final IInventory inventory;

  public BarrelGui(IInventory playerInv, IInventory inventory, EntityPlayer player) {
    super(new BarrelContainer(playerInv, inventory, player));

    this.playerInv = playerInv;
    this.inventory = inventory;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String s = inventory.getDisplayName().getUnformattedText();
    fontRenderer.drawString(s, 8, 6, 4210752);
    fontRenderer.drawString(
        playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(TEXTURE);
    int i = (width - xSize) / 2;
    int j = (height - ySize) / 2;
    drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
  }
}
