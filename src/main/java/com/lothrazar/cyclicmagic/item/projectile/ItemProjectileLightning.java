package com.lothrazar.cyclicmagic.item.projectile;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileLightning extends BaseItemProjectile implements IHasRecipe {
 
	@Override
	public void addRecipe() {


		GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.QUARTZ), new ItemStack(Items.GHAST_TEAR));
		
	}

	@Override
	void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
		this.doThrow(world, player, hand, new EntityLightningballBolt(world, player));
	}

}
