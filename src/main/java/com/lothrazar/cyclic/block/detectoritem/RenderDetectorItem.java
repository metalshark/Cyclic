package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderDetectorItem extends TileEntityRenderer<TileDetectorItem> {

  public RenderDetectorItem(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileDetectorItem te, float v, MatrixStack matrixStack,
                     IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
    if (te.getField(TileDetectorItem.Fields.RENDER.ordinal()) == 1) {
      UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.6F, ClientConfigCyclic.getColor(te));
    }
  }
}
