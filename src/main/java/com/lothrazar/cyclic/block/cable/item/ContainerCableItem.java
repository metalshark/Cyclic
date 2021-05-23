package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCableItem extends ContainerBase {

  protected TileCableItem tile;

  public ContainerCableItem(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.item_pipe, windowId);
    tile = (TileCableItem) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    //    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    this.endInv = tile.filter.getSlots();
    //dont show 0 thats the actual thing in the slot
    addSlot(new SlotItemHandler(tile.filter, 0, 80, 29));
    //    });
    layoutPlayerInventorySlots(8, 84);
    this.trackEnergy(tile);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.item_pipe);
  }
}
