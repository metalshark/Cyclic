package com.lothrazar.cyclic.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public final class UtilEnergyStorage {
  private UtilEnergyStorage() {
  }

  @Nullable
  public static IEnergyStorage get(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final Direction side) {
    return getOptCap(world, blockPos, side).resolve().orElse(null);
  }

  @Nonnull
  public static LazyOptional<IEnergyStorage> getOptCap(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final Direction side) {
    final TileEntity tileEntity = world.getTileEntity(blockPos);
    if (tileEntity != null) {
      return tileEntity.getCapability(CapabilityEnergy.ENERGY, side);
    }
    return LazyOptional.empty();
  }

  public static int moveEnergy(@Nonnull final IEnergyStorage input, @Nonnull final IEnergyStorage output, final int amount) {
    int outputCapacity = output.getMaxEnergyStored() - output.getEnergyStored();
    if (outputCapacity == 0) {
      return 0;
    } else if (outputCapacity < 0) {
      //buggy mods, most likely using a long instead of an int for capacity
      outputCapacity = amount;
    }

    //first simulate
    final int amountToExtract = input.extractEnergy(Math.min(amount, outputCapacity), true);
    if (amountToExtract <= 0) {
      return 0;
    }

    //now push it into output, but find out what was ACTUALLY received
    final int amountReceived = output.receiveEnergy(amountToExtract, false);
    if (amountReceived <= 0) {
      return 0;
    }

    //now actually extract that much from here
    return input.extractEnergy(amountReceived, false);
  }
}
