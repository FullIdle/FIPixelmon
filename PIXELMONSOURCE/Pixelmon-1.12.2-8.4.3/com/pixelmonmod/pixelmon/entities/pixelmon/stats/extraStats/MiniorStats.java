package com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import com.pixelmonmod.pixelmon.enums.forms.EnumMinior;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;

public class MiniorStats extends ExtraStats {
   public byte color = 0;

   public MiniorStats() {
   }

   public MiniorStats(byte readByte) {
      this.color = readByte;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("MiniorColor", this.color);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.color = nbt.func_74771_c("MiniorColor");
   }

   public boolean hasSpecialSetup() {
      return true;
   }

   public void specialPrep(Pokemon pokemon) {
      if (pokemon.getFormEnum() == EnumMinior.METEOR) {
         this.color = (byte)(new Random()).nextInt(EnumMinior.values().length - 1);
      } else {
         this.color = (byte)(pokemon.getForm() - 1);
      }

   }
}
