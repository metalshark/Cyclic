package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RenderMelter extends TileEntityRenderer<TileMelter> {

  public RenderMelter(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileMelter tankHere, float v, MatrixStack matrixStack,
                     IRenderTypeBuffer buffer, int light, int overlayLight) {
    //TODO: recipe from tag
    IItemHandler itemHandler = tankHere.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
    if (itemHandler != null) {
      ItemStack stack = itemHandler.getStackInSlot(0);
      if (!stack.isEmpty()) {
        matrixStack.push();
        matrixStack.translate(0.5, 0.60, 0.5);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.GROUND, 0x111111, 200, matrixStack, buffer);
        matrixStack.pop();
      }
      stack = itemHandler.getStackInSlot(1);
      if (!stack.isEmpty()) {
        matrixStack.push();
        matrixStack.translate(0.5, 0.10, 0.5);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.GROUND, 0x999999, 0, matrixStack, buffer);
        matrixStack.pop();
      }
    }
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    IVertexBuilder vertexBuffer = buffer.getBuffer(FluidTankRenderType.resizableCuboid());
    matrixStack.push();
    matrixStack.scale(1F, UtilFluid.getScale(tankHere.tank), 1F);
    UtilRender.renderObject(UtilFluid.getFluidModel(fluid, UtilFluid.STAGES - 1),
        matrixStack, vertexBuffer, UtilRender.getColorARGB(fluid, 0.1F),
        UtilRender.calculateGlowLight(light, fluid));
    matrixStack.pop();
  }
}
