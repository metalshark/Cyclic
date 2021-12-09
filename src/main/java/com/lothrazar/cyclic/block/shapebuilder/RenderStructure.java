package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderStructure extends TileEntityRenderer<TileStructure> {

  public RenderStructure(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileStructure te, float v, MatrixStack matrixStack, IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    if (1 == te.getField(TileStructure.Fields.RENDER.ordinal())) {
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.isEmpty()) {
        UtilRender.renderOutline(te.getPos(), te.getShape(), matrixStack, 0.7F, ClientConfigCyclic.getColor(te));
      } else {
        UtilRender.renderAsBlock(te.getPos(), te.getShape(), matrixStack, stack, 1, 1);
      }
    }
  }
}
