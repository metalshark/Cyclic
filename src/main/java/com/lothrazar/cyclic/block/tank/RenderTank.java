package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class RenderTank extends TileEntityRenderer<TileTank> {

  public RenderTank(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileTank tankHere, float v, @Nonnull MatrixStack matrix,
                     @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight) {
    IFluidHandler handler = tankHere.getFluidHandler();
    handler.getFluidInTank(0);
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    IVertexBuilder buffer = renderer.getBuffer(FluidTankRenderType.resizableCuboid());
    matrix.scale(1F, UtilFluid.getScale(tankHere.fluidTank), 1F);
    UtilRender.renderObject(UtilFluid.getFluidModel(fluid, UtilFluid.STAGES - 1),
        matrix, buffer, UtilRender.getColorARGB(fluid, 0.1F),
        UtilRender.calculateGlowLight(light, fluid));
  }
}
