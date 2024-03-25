package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class LakeTrioStats extends ExtraStats {
   public int numEnchanted = 0;

   public LakeTrioStats() {
   }

   public LakeTrioStats(int numEnchanted) {
      this.numEnchanted = numEnchanted;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("NumEnchanted", this.numEnchanted);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.numEnchanted = nbt.func_74762_e("NumEnchanted");
   }
}
