package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.base.FluidTankBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import com.lothrazar.cyclic.util.UtilFluidHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableFluid extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static final int CAPACITY = 16 * FluidAttributes.BUCKET_VOLUME;
  public static final int FLOW_RATE = CAPACITY; //normal non-extract flow
  private static final int TIMER_SIDE_INPUT = 15;
  final ItemStackHandler filter = new ItemStackHandler(1) {
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  private final FluidTank fluidTank = new FluidTankBase(this, CAPACITY, fluidStack ->
          FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), fluidStack));
  private final Map<Direction, EnumConnectType> connectTypeMap = new HashMap<>();
  private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> fluidTank);
  private final Map<Direction, LazyOptional<IFluidHandler>> fluidCapSides = new HashMap<>();
  private final Map<Direction, IFluidHandler> fluidCache = new HashMap<>();
  private final Map<Direction, TileEntityBase> adjacentTileEntityBases = new HashMap<>();
  private final Map<Direction, Integer> receivedFrom = new HashMap<>();

  public TileCableFluid() {
    super(TileRegistry.fluid_pipeTile);
  }

  @Override
  public IFluidHandler getFluidHandler() {
    return fluidTank;
  }

  @Override
  protected IFluidHandler getAdjacentFluidHandler(@Nonnull final Direction side) {
    IFluidHandler adjacentHandler = fluidCache.computeIfAbsent(side, k -> {
      adjacentTileEntityBases.remove(k);
      if (world == null) {
        return null;
      }
      final TileEntity tileEntity = world.getTileEntity(pos.offset(k));
      if (tileEntity == null) {
        return null;
      } else if (tileEntity instanceof TileEntityBase) {
        adjacentTileEntityBases.put(k, (TileEntityBase) tileEntity);
      }
      final LazyOptional<IFluidHandler> optCap = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, k.getOpposite());
      final IFluidHandler handler = optCap.resolve().orElse(null);
      if (handler != null) {
        optCap.addListener((o) -> {
          adjacentTileEntityBases.remove(k);
          fluidCache.remove(k);
          receivedFrom.remove(k);
        });
      }
      return handler;
    });
    if (adjacentHandler == null) {
      if (world == null) {
        return null;
      }
      adjacentHandler = UtilFluidHandler.getFromBlock(world, pos.offset(side));
    }
    return adjacentHandler;
  }

  private EnumConnectType getConnectionType(@Nonnull final Direction side) {
    return connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
  }

  @Override
  public void setReceivedFrom(final @Nonnull Direction side) {
    receivedFrom.put(side, TIMER_SIDE_INPUT);
  }

  @Override
  public void updateConnection(final @Nonnull Direction side, final @Nonnull EnumConnectType connectType) {
    final EnumConnectType oldConnectType = connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      fluidCapSides.computeIfPresent(side, (k, v) -> {
        v.invalidate();
        return null;
      });
    } else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      fluidCapSides.put(side, LazyOptional.of(() -> fluidTank));
    }
    connectTypeMap.put(side, connectType);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    for (@Nonnull final Iterator<Map.Entry<Direction, Integer>> it = receivedFrom.entrySet().iterator(); it.hasNext(); ) {
      @Nonnull final Map.Entry<Direction, Integer> entry = it.next();
      entry.setValue(entry.getValue() - 1);
      if (entry.getValue() <= 0) {
        it.remove();
      }
    }
    int remainingAmount = FLOW_RATE;
    for (@Nonnull final Direction side : UtilDirection.getAllInDifferentOrder()) {
      final EnumConnectType connectType = getConnectionType(side);
      if (connectType == EnumConnectType.CABLE) {
        remainingAmount -= getFluidsFromAdjacent(fluidTank, side, remainingAmount).getAmount();
      } else if (connectType == EnumConnectType.INVENTORY && !receivedFrom.containsKey(side)) {
        final int moved = moveFluidsToAdjacent(fluidTank, side, remainingAmount).getAmount();
        if (moved <= 0) {
          continue;
        }
        remainingAmount -= moved;
        final TileEntityBase adjacentTileEntityBase = adjacentTileEntityBases.get(side);
        if (adjacentTileEntityBase != null) {
          adjacentTileEntityBase.setReceivedFrom(side.getOpposite());
        }
      }
    }
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if (side == null) {
        return fluidCap.cast();
      }
      final LazyOptional<IFluidHandler> fluidCapSide = fluidCapSides.computeIfAbsent(side, k -> {
        if (getConnectionType(k) != EnumConnectType.BLOCKED) {
          final LazyOptional<IFluidHandler> v = LazyOptional.of(() -> fluidTank);
          fluidCapSides.put(k, v);
          return v;
        }
        return null;
      });
      if (fluidCapSide != null) {
        return fluidCapSide.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    fluidCap.invalidate();
    for (@Nonnull final LazyOptional<IFluidHandler> sidedCap : fluidCapSides.values()) {
      sidedCap.invalidate();
    }
  }

  @Override
  public void read(@Nonnull BlockState bs, CompoundNBT tag) {
    filter.deserializeNBT(tag.getCompound(NBTFILTER));
    fluidTank.readFromNBT(tag.getCompound(NBTFLUID));
    super.read(bs, tag);
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTFILTER, filter.serializeNBT());
    tag.put(NBTFLUID, fluidTank.writeToNBT(new CompoundNBT()));
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public @Nullable Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
    if (world == null) {
      return null;
    }
    return new ContainerCableFluid(i, world, pos, playerInventory, playerEntity);
  }
}
