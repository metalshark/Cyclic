/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.item.scythe.ScytheType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilScythe {

  private static final INamedTag<Block> PLANTS = BlockTags.makeWrapperTag("forge:plants");
  private static final INamedTag<Block> MUSHROOM = BlockTags.makeWrapperTag("forge:mushroom");
  private static final INamedTag<Block> VINES = BlockTags.makeWrapperTag("forge:vines");
  private static final INamedTag<Block> SBRUSH = BlockTags.makeWrapperTag(ScytheType.BRUSH.type().toString());
  //this is valid but not needed
  //  private static final INamedTag<Block> SLEAVES = BlockTags.makeWrapperTag("minecraft:leaves");
  private static final INamedTag<Block> SFORAGE = BlockTags.makeWrapperTag(ScytheType.FORAGE.type().toString());

  public static boolean harvestSingle(World world, PlayerEntity player, BlockPos posCurrent, ScytheType type) {
    boolean doBreak = false;
    BlockState blockState = world.getBlockState(posCurrent);
    switch (type) {
      case LEAVES:
        doBreak = blockState.isIn(BlockTags.LEAVES);
      break;
      case BRUSH:
        doBreak = blockState.isIn(PLANTS);
      break;
      case FORAGE:
        doBreak = blockState.isIn(SFORAGE);
      break;
    }
    if (doBreak) {
      //harvest block with player context: better mod compatibility
      blockState.getBlock().harvestBlock(world, player, posCurrent, blockState, world.getTileEntity(posCurrent), player.getHeldItemMainhand());
      //sometimes this doesnt work and/or doesnt sync ot client, so force it
      world.destroyBlock(posCurrent, false);
      //break with false to disable dropsfor the above versions, dont want to dupe tallflowers
      return true;
    }
    return false;
  }
}
