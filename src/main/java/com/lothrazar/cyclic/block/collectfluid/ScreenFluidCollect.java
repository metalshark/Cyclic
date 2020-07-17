package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFluidCollect extends ScreenBase<ContainerFluidCollect> {

  private FluidBar fluid;
  private ButtonMachine btnRedstone;
  private ButtonMachine btnRender;

  public ScreenFluidCollect(ContainerFluidCollect screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    fluid = new FluidBar(this, 152, 14, TileFluidCollect.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    fluid.guiLeft = guiLeft;
    fluid.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      container.tile.setNeedsRedstone((container.tile.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileFluidCollect.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
    btnRender = addButton(new ButtonMachine(x + 20, y, 20, 20, "", (p) -> {
      int f = TileFluidCollect.Fields.RENDER.ordinal();
      container.tile.setField(f, (container.tile.getField(f) + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    fluid.renderHoveredToolTip(mouseX, mouseY, container.tile.getFluid());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.tile.getNeedsRedstone()));
    btnRedstone.setTextureId(container.tile.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
    int on = container.tile.getField(TileFluidCollect.Fields.RENDER.ordinal());
    btnRender.setTooltip(UtilChat.lang("gui.cyclic.render" + on));
    btnRender.setTextureId(on == 1 ? TextureEnum.RENDER_SHOW : TextureEnum.RENDER_HIDE);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(xSize / 2 - 9, 28);
    fluid.draw(container.tile.getFluid());
  }
}
