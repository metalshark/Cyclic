package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;

public class UtilDirection {

  public static final List<List<Direction>> ALL_DIFFERENT_ORDER = permutateDirections(Arrays.asList(Direction.values()), 0);
  public static int allDifferentOrderIndex = -1;

  //determine all permutations of direction order at compile time and cycle through them at runtime
  //the alternative of using a random order is costly and left to chance
  private static List<List<Direction>> permutateDirections(final List<Direction> list, int pos) {
    final List<List<Direction>> results = new ArrayList<>();
    for (int i = pos; i < list.size(); i++) {
      Collections.swap(list, i, pos);
      results.addAll(permutateDirections(list, pos + 1));
      Collections.swap(list, pos, i);
    }
    if (pos == list.size() - 1) {
      results.add(new ArrayList<>(list));
    }
    return results;
  }

  public static List<Direction> getAllInDifferentOrder() {
    allDifferentOrderIndex++;
    allDifferentOrderIndex %= ALL_DIFFERENT_ORDER.size();
    return ALL_DIFFERENT_ORDER.get(allDifferentOrderIndex);
  }

  public static Direction getRandom(final Random rand) {
    int index = MathHelper.nextInt(rand, 0, Direction.values().length - 1);
    return Direction.values()[index];
  }
}
