package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class MewStats extends ExtraStats {
   public int numCloned = 0;
   public static final int MAX_CLONES = 3;

   public MewStats() {
   }

   public MewStats(int numCloned) {
      this.numCloned = numCloned;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("NumCloned", this.numCloned);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.numCloned = nbt.func_74762_e("NumCloned");
   }
}
