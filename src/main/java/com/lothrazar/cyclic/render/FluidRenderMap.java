package com.lothrazar.cyclic.render;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * <p>
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 * <p>
 * Map which uses FluidStacks as keys, ignoring amount. Primary use: caching FluidStack aware fluid rendering (NBT, yay)
 */
@SuppressWarnings("serial")
public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {

  public FluidRenderMap() {
    super(FluidHashStrategy.INSTANCE);
  }

  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidType type) {
    Fluid fluid = fluidStack.getFluid();
    ResourceLocation spriteLocation;
    if (type == FluidType.STILL) {
      spriteLocation = fluid.getAttributes().getStillTexture(fluidStack);
    } else {
      spriteLocation = fluid.getAttributes().getFlowingTexture(fluidStack);
    }
    return getSprite(spriteLocation);
  }

  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
  }

  public enum FluidType {
    STILL, FLOWING
  }

  /**
   * Implements equals & hashCode that ignore FluidStack#amount
   */
  public static class FluidHashStrategy implements Hash.Strategy<FluidStack> {

    public static FluidHashStrategy INSTANCE = new FluidHashStrategy();

    @Override
    public int hashCode(FluidStack stack) {
      if (stack == null || stack.isEmpty()) {
        return 0;
      }
      int code = 1;
      code = 31 * code + stack.getFluid().hashCode();
      if (stack.hasTag()) {
        code = 31 * code + stack.getTag().hashCode();
      }
      return code;
    }

    @Override
    public boolean equals(FluidStack a, FluidStack b) {
      return a == null ? b == null : b != null && a.isFluidEqual(b);
    }
  }
}
