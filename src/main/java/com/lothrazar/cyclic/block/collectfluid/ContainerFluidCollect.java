package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFluidCollect extends ContainerBase {

  protected TileFluidCollect tile;

  public ContainerFluidCollect(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.COLLECTOR_FLUID, windowId);
    tile = (TileFluidCollect) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    final IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
    if (itemHandler != null) {
      this.endInv = itemHandler.getSlots();
      addSlot(new SlotItemHandler(itemHandler, 0, 10, 51));
    }
    layoutPlayerInventorySlots(8, 84);
    this.trackEnergy(tile);
    this.trackAllIntFields(tile, TileFluidCollect.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.collector_fluid);
  }
}
