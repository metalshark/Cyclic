package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class UnbreakablePoweredTile extends TileEntityBase implements ITickableTileEntity {

  public UnbreakablePoweredTile() {
    super(TileRegistry.unbreakable_reactive);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    boolean isBreakable = !this.isPowered();
    UnbreakablePoweredBlock.setBreakable(world, pos, isBreakable);
  }

  @Override
  public void read(@Nonnull BlockState bs, @Nonnull CompoundNBT tag) {
    super.read(bs, tag);
  }

  @Nonnull
  @Override
  public CompoundNBT write(@Nonnull CompoundNBT tag) {
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
