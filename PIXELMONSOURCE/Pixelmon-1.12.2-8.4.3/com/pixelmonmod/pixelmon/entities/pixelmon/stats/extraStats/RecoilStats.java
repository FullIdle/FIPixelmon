package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import net.minecraft.nbt.NBTTagCompound;

public class RecoilStats extends ExtraStats {
   private int recoil = 0;

   public int recoil() {
      return this.recoil;
   }

   public void setRecoil(int recoil) {
      this.recoil = recoil;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      if (this.recoil > 0) {
         nbt.func_74768_a("battleRecoil", this.recoil);
      }

   }

   public void readFromNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("battleRecoil")) {
         this.recoil = nbt.func_74762_e("battleRecoil");
      }

   }
}
