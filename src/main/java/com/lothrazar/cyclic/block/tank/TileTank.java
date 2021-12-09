package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileTank extends TileEntityBase implements ITickableTileEntity {
  public static final int CAPACITY = 64 * FluidAttributes.BUCKET_VOLUME;
  public static final int TRANSFER_FLUID_PER_TICK = FluidAttributes.BUCKET_VOLUME / 20;
  public FluidTankBase fluidTank = new FluidTankBase(this, CAPACITY, fluidStack -> true);
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> fluidTank);
  private IFluidHandler fluidHandlerBelow = null;

  public TileTank() {
    super(TileRegistry.tank);
  }

  @Nonnull
  @Override
  public IFluidHandler getFluidHandler() {
    return fluidTank;
  }

  @Override
  public void read(@Nonnull BlockState bs, CompoundNBT tag) {
    fluidTank.readFromNBT(tag.getCompound(NBTFLUID));
    super.read(bs, tag);
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTFLUID, fluidTank.writeToNBT(new CompoundNBT()));
    return super.write(tag);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return fluidCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    fluidCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void setFluid(FluidStack fluid) {
    fluidTank.setFluid(fluid);
  }

  @Override
  @Nullable
  protected IFluidHandler getAdjacentFluidHandler(@Nonnull final Direction side) {
    if (side != Direction.DOWN) {
      return super.getAdjacentFluidHandler(side);
    }
    if (fluidHandlerBelow != null) {
      return fluidHandlerBelow;
    }
    final LazyOptional<IFluidHandler> optCap = getAdjacentFluidHandlerOptCap(side);
    fluidHandlerBelow = optCap.resolve().orElse(null);
    if (fluidHandlerBelow != null) {
      optCap.addListener((o) -> fluidHandlerBelow = null);
    }
    return fluidHandlerBelow;
  }

  @Override
  public void tick() {
    if (world == null) {
      return;
    }
    //drain below
    moveFluidsToAdjacent(fluidTank, Direction.DOWN, TRANSFER_FLUID_PER_TICK);
  }
}
