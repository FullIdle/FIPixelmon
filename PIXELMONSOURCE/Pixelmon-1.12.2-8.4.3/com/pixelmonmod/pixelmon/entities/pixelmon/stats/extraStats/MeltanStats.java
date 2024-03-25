package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class MeltanStats extends ExtraStats {
   public int oresSmelted = 0;

   public MeltanStats() {
   }

   public MeltanStats(int oresSmelted) {
      this.oresSmelted = oresSmelted;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("OresSmelted", this.oresSmelted);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.oresSmelted = nbt.func_74762_e("OresSmelted");
   }
}
