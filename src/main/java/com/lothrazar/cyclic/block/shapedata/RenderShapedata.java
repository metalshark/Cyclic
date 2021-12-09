package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderShapedata extends TileEntityRenderer<TileShapedata> {

  public RenderShapedata(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileShapedata te, float v, MatrixStack matrixStack,
                     IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    if (1 == te.getField(TileShapedata.Fields.RENDER.ordinal())) {
      if (te.getTarget(0) != null) {
        UtilRender.renderOutline(te.getPos(), te.getTarget(0), matrixStack, 1.05F, Color.BLUE);
      }
      if (te.getTarget(1) != null) {
        UtilRender.renderOutline(te.getPos(), te.getTarget(1), matrixStack, 1.05F, Color.RED);
      }
      //      ItemStack stack = inv.getStackInSlot(0);
      //      if (stack.isEmpty()) {
      //      }
      //      else {
      //        //              UtilRender.renderAsBlock(te.getPos(), te.getShape(), matrixStack, stack, 0.5F, 1.0F);
      //      }
    }
  }
}
