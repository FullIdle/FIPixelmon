package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class DeoxysStats extends ExtraStats {
   boolean hasSusForm = false;

   public DeoxysStats() {
   }

   public DeoxysStats(boolean hasSusForm) {
      this.hasSusForm = hasSusForm;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74757_a("hasSus", this.hasSusForm);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.hasSusForm = nbt.func_74767_n("hasSus");
   }

   public boolean isSus() {
      return this.hasSusForm;
   }

   public void setSus(boolean sus) {
      this.hasSusForm = sus;
   }
}
