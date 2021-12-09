package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class CyclicFile {

  public static final String NBTINV = "inv";
  public final UUID playerId;
  public boolean storageVisible = false;
  public boolean todoVisible = false;
  public boolean stepHeight = false;
  public List<String> todoTasks = new ArrayList<>();
  public int flyTicks = 0;
  public int spectatorTicks = 0;
  // first 27 slots are for inventory cake storage. remaining unused
  public ItemStackHandler inventory = new ItemStackHandler(5 * 9);
  public boolean stepHeightForceOff;

  public CyclicFile(UUID playerId) {
    this.playerId = playerId;
  }

  @Override
  public String toString() {
    return "CyclicFile [playerId=" + playerId + ", storageVisible=" + storageVisible + ", todoVisible=" + todoVisible + ", stepHeight=" + stepHeight +
        ", todoTasks=" + todoTasks + ", flyTicks=" + flyTicks + ", spectatorTicks=" + spectatorTicks + ", inventory=" + inventory + ", stepHeightForceOff=" + stepHeightForceOff + "]";
  }

  public void read(CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    flyTicks = tag.getInt("flyTicks");
    spectatorTicks = tag.getInt("spectatorTicks");
    storageVisible = tag.getBoolean("storageVisible");
    stepHeightForceOff = tag.getBoolean("stepHeightForceOff");
    stepHeight = tag.getBoolean("stepHeight");
    if (tag.contains("tasks")) {
      ListNBT glist = tag.getList("tasks", Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < glist.size(); i++) {
        CompoundNBT row = glist.getCompound(i);
        todoTasks.add(row.getString("todo"));
      }
    }
  }

  public CompoundNBT write() {
    CompoundNBT tag = new CompoundNBT();
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("flyTicks", flyTicks);
    tag.putInt("spectatorTicks", spectatorTicks);
    tag.putBoolean("stepHeight", stepHeight);
    tag.putBoolean("stepHeightForceOff", stepHeightForceOff);
    tag.putBoolean("storageVisible", storageVisible);
    ListNBT glist = new ListNBT();
    int i = 0;
    for (String t : todoTasks) {
      CompoundNBT row = new CompoundNBT();
      row.putInt("index", i);
      row.putString("todo", t);
      glist.add(row);
    }
    tag.put("tasks", glist);
    return tag;
  }

  public void toggleStepHeight() {
    this.stepHeight = !this.stepHeight;
    if (!this.stepHeight) {
      this.stepHeightForceOff = true;
    }
  }
}
