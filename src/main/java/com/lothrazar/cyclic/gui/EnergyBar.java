package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EnergyBar {

  public int capacity;
  public int guiLeft;
  public int guiTop;
  public boolean visible = true;
  private Screen parent;
  private int x = 154;
  private int y = 8;
  private int width = 16;
  private int height = 62;

  public EnergyBar(Screen parent, int cap) {
    this.parent = parent;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + getHeight();
  }

  public void draw(MatrixStack ms, float energ) {
    if (!visible) {
      return;
    }
    int relX;
    int relY;
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.ENERGY_BAR);
    relX = guiLeft + x;
    relY = guiTop + y;
    Screen.blit(ms, relX, relY, 16, 0, width, getHeight(), 32, getHeight());
    float pct = Math.min(energ / capacity, 1.0F);
    Screen.blit(ms, relX, relY, 0, 0, width, getHeight() - (int) (getHeight() * pct), 32, getHeight());
  }

  public void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY, int energ) {
    if (visible && this.isMouseover(mouseX, mouseY)) {
      String tt = energ + "/" + this.capacity;
      List<ITextComponent> list = new ArrayList<>();
      list.add(new TranslationTextComponent(tt));
      parent.func_243308_b(ms, list, mouseX, mouseY);
    }
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
