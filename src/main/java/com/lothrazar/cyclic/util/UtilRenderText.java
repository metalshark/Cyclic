package com.lothrazar.cyclic.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

/**
 * as of minecraft 1.16 much of this file contains code from this mod which is MIT License, the same as this project
 * <p>
 * https://github.com/jaquadro/StorageDrawers/blob/1.16/LICENSE
 */
public class UtilRenderText {

  static final float[] SIDE_ROTATION = {0, 0, 2, 0, 3, 1};

  public static void alignRendering(MatrixStack matrix, Direction side) {
    // Rotate to face the correct direction for the drawer's orientation.
    matrix.translate(.5f, .5f, .5f);
    matrix.rotate(new Quaternion(Vector3f.YP, getRotationYForSide2D(side), true));
    matrix.translate(-.5f, -.5f, -.5f);
  }

  public static float getRotationYForSide2D(Direction side) {
    return SIDE_ROTATION[side.ordinal()] * 90;
  }
}
