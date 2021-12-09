package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilPlayer;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShapeCard extends ItemBase {

  public static final String VALID_SHAPE = "cyclic-shape";

  public ShapeCard(Properties properties) {
    super(properties);
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundNBT encoded = NBTUtil.writeBlockState(target);
    wand.getOrCreateTag().put(BuilderActionType.NBTBLOCKSTATE, encoded);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".count");
      t.appendString(shape.getCount() + "");
      tooltip.add(t);
      BlockState target = BuilderActionType.getBlockState(stack);
      String block = "scepter.cyclic.nothing";
      if (target != null) {
        block = target.getBlock().getTranslationKey();
      }
      tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + UtilChat.lang(block)));
      if (flagIn.isAdvanced()) {
        //        String side = "S: " + dim.getSide().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(side));
        //        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(sideF));
        //        tooltip.add(new TranslationTextComponent("H: " + dim.getHitVec().toString()));
      }
    }
    TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
    t.mergeStyle(TextFormatting.GRAY);
    tooltip.add(t);
    //    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      BlockState targetState = BuilderActionType.getBlockState(stack);
      if (targetState != null) {
        final BlockPos centerPos = player.getPosition();
        //        Direction side = context.getFace();
        BlockPos posBuild = null;
        for (BlockPos s : shape.getShape()) {
          posBuild = centerPos.add(s);
          if (World.isOutsideBuildHeight(posBuild) || !world.isAirBlock(posBuild)) {
            //if outside, or not air, then continue
            continue;
          }
          int slot = -1;
          if (!player.isCreative()) {
            //not creative
            slot = UtilPlayer.getFirstSlotWithBlock(player, targetState);
            if (slot < 0) {
              //cannot find material
              UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.empty");
              break; //stop looping
            }
          }
          if (world.setBlockState(posBuild, targetState, 1)) {
            UtilPlayer.decrStackSize(player, slot);
          }
        }
      } else { // no state selected
        UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.state");
      }
    } else {
      UtilChat.sendStatusMessage(player, "item.cyclic.shape_data.nothing");
    }
    player.swingArm(hand);
    return super.onItemRightClick(world, player, hand);
  }
}
