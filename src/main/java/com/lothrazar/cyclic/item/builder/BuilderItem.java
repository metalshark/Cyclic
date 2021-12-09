package com.lothrazar.cyclic.item.builder;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BuilderItem extends ItemBase {

  public BuildStyle style;

  public BuilderItem(Properties properties, BuildStyle t) {
    super(properties.maxStackSize(1).maxDamage(4096));
    style = t;
  }

  public static BuilderActionType getActionType(ItemStack stack) {
    return BuilderActionType.values()[BuilderActionType.get(stack)];
  }

  public static ItemStack getIfHeld(PlayerEntity player) {
    ItemStack heldItem = player.getHeldItemMainhand();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    heldItem = player.getHeldItemOffhand();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    String msg = TextFormatting.GREEN + UtilChat.lang(BuilderActionType.getName(stack));
    tooltip.add(new TranslationTextComponent(msg));
    BlockState target = BuilderActionType.getBlockState(stack);
    String block = "scepter.cyclic.nothing";
    if (target != null) {
      block = target.getBlock().getTranslationKey();
    }
    tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + UtilChat.lang(block)));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    BuilderActionType.tickTimeout(stack);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
    //TODO: ??INSIDE building no offset
    // on top of selected = do offset
    if (side != null && buildStyle.isOffset()) {
      pos = pos.offset(side);
    }
    if (context.getWorld().isRemote) {
      BuilderActionType type = getActionType(stack);
      PacketSwapBlock message = new PacketSwapBlock(pos, type, side, context.getHand());
      PacketRegistry.INSTANCE.sendToServer(message);
      return ActionResultType.SUCCESS;
    }
    return super.onItemUse(context);
  }
}
