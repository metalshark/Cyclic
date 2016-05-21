package com.lothrazar.cyclicmagic.gui.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerFakeWorkbench extends ContainerWorkbench {
	private World worldObj;

	public ContainerFakeWorkbench(InventoryPlayer par1InventoryPlayer, World par2World) {
		super(par1InventoryPlayer, par2World, par1InventoryPlayer.player.getPosition());
		worldObj = par2World;
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);

		if (!worldObj.isRemote) {
			for (int var2 = 0; var2 < 9; ++var2) {
				ItemStack var3 = craftMatrix.getStackInSlot(var2);

				
				if (var3 != null) {
					par1EntityPlayer.dropItem(var3, true);
				//	par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, true);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack var3 = null;
		Slot var4 = (Slot) inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack()) {
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 == 0) {
				if (!mergeItemStack(var5, 10, 46, true)) { return null; }

				var4.onSlotChange(var5, var3);
			}
			else if (par2 >= 10 && par2 < 37) {
				if (!mergeItemStack(var5, 37, 46, false)) { return null; }
			}
			else if (par2 >= 37 && par2 < 46) {
				if (!mergeItemStack(var5, 10, 37, false)) { return null; }
			}
			else if (!mergeItemStack(var5, 10, 46, false)) { return null; }

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			}
			else {
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize) { return null; }

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}
}