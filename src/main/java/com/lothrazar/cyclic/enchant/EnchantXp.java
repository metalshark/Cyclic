package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantXp extends EnchantBase {

  public static final String ID = "experience_boost";
  public static BooleanValue CFG;

  public EnchantXp(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... mainhand) {
    super(rarityIn, typeIn, mainhand);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void handleBlockBreakEvent(BlockEvent.BreakEvent event) {
    int level = getCurrentLevelTool(event.getPlayer().getHeldItemMainhand());
    if (level <= 0) {
      return;
    }
    event.setExpToDrop(event.getExpToDrop() + getRandomExpAmount(level, event.getPlayer().world));
  }

  @SubscribeEvent
  public void handleEntityDropEvent(LivingExperienceDropEvent event) {
    if (event.getAttackingPlayer() == null) {
      return;
    }
    int level = getCurrentLevelTool(event.getAttackingPlayer().getHeldItemMainhand());
    if (level <= 0) {
      return;
    }
    event.setDroppedExperience(event.getDroppedExperience() + getRandomExpAmount(level, event.getAttackingPlayer().world));
  }

  private int getRandomExpAmount(int level, World world) {
    return world.rand.nextInt(getMaxLevel()) * (level + 1);
  }

  @Override
  public boolean canApply(ItemStack stack) {
    if (stack.getItem() instanceof SwordItem) {
      return true; //override even though digger type
    }
    return super.canApply(stack);
  }

  @Override
  public boolean canApplyTogether(Enchantment ench) {
    return super.canApplyTogether(ench) && ench != EnchantRegistry.EXCAVATE;
  }

  /**
   * Returns the maximum level that the enchantment can have.
   */
  @Override
  public int getMaxLevel() {
    return 3;
  }
}
