package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class ShearableStats extends ExtraStats {
   public byte growthStage = 0;

   public ShearableStats() {
   }

   public ShearableStats(byte readByte) {
      this.growthStage = readByte;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("WoolStage", this.growthStage);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.growthStage = nbt.func_74771_c("WoolStage");
   }
}
