package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWorkbench extends TileEntityBase implements INamedContainerProvider {

  ItemStackHandler inventory = new ItemStackHandler(9);
  IItemHandler output = new ItemStackHandler(1);

  public TileWorkbench() {
    super(TileRegistry.workbench);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerWorkbench(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void read(@Nonnull BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    //    CompoundNBT compound = ((INBTSerializable<CompoundNBT>) inventory).serializeNBT();
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {
    //
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
