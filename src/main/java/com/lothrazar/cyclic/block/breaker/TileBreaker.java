package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileBreaker extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static final int TIMER_FULL = 500;
  static final int MAX = 64000;

  public TileBreaker() {
    super(TileRegistry.breakerTile);
  }
  //  public static IntValue POWERCONF;
  //  private CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  //  private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    BlockPos target = pos.offset(this.getCurrentFacing());
    BlockState state = world.getBlockState(target);
    if (state.getBlock() != Blocks.AIR &&
        state.getBlockHardness(world, target) >= 0) {
      this.world.destroyBlock(target, true);
      //      int cost = POWERCONF.get();
      //      ModCyclic.LOGGER.info("cost" + cost + " have " + energy.getEnergyStored());
      //      if (cost > 0) {
      //        energy.extractEnergy(cost, false);
      //      }
    }
    //else unbreakable
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerBreaker(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void read(@Nonnull BlockState bs, @Nonnull CompoundNBT tag) {
    //    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.read(bs, tag);
  }

  @Nonnull
  @Override
  public CompoundNBT write(@Nonnull CompoundNBT tag) {
    //    tag.put(NBTENERGY, energy.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
        break;
      case TIMER:
        timer = value;
        break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return timer;
    }
    return 0;
  }

  public int getEnergyMax() {
    return MAX;
  }

  static enum Fields {
    REDSTONE, TIMER;
  }
}
