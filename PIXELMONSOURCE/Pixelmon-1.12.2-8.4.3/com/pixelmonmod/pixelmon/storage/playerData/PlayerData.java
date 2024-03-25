package com.pixelmonmod.pixelmon.storage.playerData;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData implements ISaveData {
   private boolean gifted = false;
   public boolean giftOpened = false;
   private short giftYear = 2020;

   public void writeToNBT(NBTTagCompound var1) {
      var1.func_74757_a("gifted", this.gifted);
      var1.func_74757_a("giftOpened", this.giftOpened);
      var1.func_74777_a("giftYear", this.giftYear);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.gifted = var1.func_74767_n("gifted");
      this.giftOpened = var1.func_74767_n("giftOpened");
      if (var1.func_74764_b("giftYear")) {
         this.giftYear = var1.func_74765_d("giftYear");
      } else {
         this.giftYear = 2020;
      }

   }

   public boolean getWasGifted() {
      return this.gifted;
   }

   public boolean getWasGifted(int year) {
      return this.gifted && this.giftYear == year;
   }

   public void receivedGift(int year) {
      this.gifted = true;
      this.giftYear = (short)year;
   }
}
