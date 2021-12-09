package com.lothrazar.cyclic.item.scythe;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ScytheHarvest extends ItemBase {

  private static final int RADIUS = 6; //13x13
  private static final int RADIUS_SNEAKING = 2; //2x2

  public ScytheHarvest(Properties properties) {
    super(properties);
  }

  public List<BlockPos> getShape(BlockPos pos, int radius) {
    return UtilShape.squareHorizontalFull(pos, radius);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    if (side != null) {
      pos = pos.offset(side);
    }
    PlayerEntity player = context.getPlayer();
    int radius = (player.isCrouching()) ? RADIUS_SNEAKING : RADIUS;
    for (BlockPos p : getShape(pos, radius)) {
      TileHarvester.tryHarvestSingle(context.getWorld(), p);
    }
    context.getPlayer().swingArm(context.getHand());
    UtilItemStack.damageItem(context.getPlayer(), context.getItem());
    return super.onItemUse(context);
  }
}
