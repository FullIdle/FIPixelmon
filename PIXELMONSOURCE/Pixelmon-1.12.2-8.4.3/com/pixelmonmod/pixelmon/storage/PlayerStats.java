package com.pixelmonmod.pixelmon.storage;

import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats {
   private int wins;
   private int losses;
   private int totalExp;
   private int totalKills;
   private int currentExp;
   private int currentKills;
   private int totalBred;
   private int totalHatched;
   private int totalEvolved;
   private HashMap caughtTypeCount = new HashMap();

   public PlayerStats() {
      EnumType[] var1 = EnumType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumType type = var1[var3];
         this.caughtTypeCount.put(type.func_176610_l(), 0);
      }

   }

   public static void getNBTTags(HashMap tags) {
      tags.put("Wins", Integer.class);
      tags.put("Losses", Integer.class);
      tags.put("TotalExp", Integer.class);
      tags.put("CurrentExp", Integer.class);
      tags.put("TotalKills", Integer.class);
      tags.put("CurrentKills", Integer.class);
      tags.put("TotalBred", Integer.class);
      tags.put("TotalHatched", Integer.class);
      tags.put("CaughtTypeCount", NBTTagCompound.class);
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74768_a("Wins", this.wins);
      nbt.func_74768_a("Losses", this.losses);
      nbt.func_74768_a("TotalExp", this.totalExp);
      nbt.func_74768_a("CurrentExp", this.currentExp);
      nbt.func_74768_a("TotalKills", this.totalKills);
      nbt.func_74768_a("CurrentKills", this.currentKills);
      nbt.func_74768_a("TotalBred", this.totalBred);
      nbt.func_74768_a("TotalHatched", this.totalHatched);
      NBTTagCompound caughtTypeNBT = new NBTTagCompound();
      this.caughtTypeCount.forEach(caughtTypeNBT::func_74768_a);
      nbt.func_74782_a("CaughtTypeCount", caughtTypeNBT);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.wins = nbt.func_74762_e("Wins");
      this.losses = nbt.func_74762_e("Losses");
      this.totalExp = nbt.func_74762_e("TotalExp");
      this.currentExp = nbt.func_74762_e("CurrentExp");
      this.totalKills = nbt.func_74762_e("TotalKills");
      this.currentKills = nbt.func_74762_e("CurrentKills");
      this.totalBred = nbt.func_74762_e("TotalBred");
      this.totalHatched = nbt.func_74762_e("TotalHatched");
      NBTTagCompound caughtTypeNBT;
      if (nbt.func_74764_b("CaughtTypeCount")) {
         caughtTypeNBT = nbt.func_74775_l("CaughtTypeCount");
      } else {
         caughtTypeNBT = new NBTTagCompound();
      }

      EnumType[] var3 = EnumType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumType type = var3[var5];
         int count = 0;
         if (caughtTypeNBT.func_74764_b(type.func_176610_l())) {
            count = caughtTypeNBT.func_74762_e(type.func_176610_l());
         }

         this.caughtTypeCount.put(type.func_176610_l(), count);
      }

   }

   public void addWin() {
      ++this.wins;
   }

   public void addLoss() {
      ++this.losses;
   }

   public void addKill() {
      ++this.totalKills;
      ++this.currentKills;
   }

   public int getCurrentKills() {
      return this.currentKills;
   }

   public void setCurrentKills(int currentKills) {
      this.currentKills = currentKills;
   }

   public int getTotalExp() {
      return this.totalExp;
   }

   public void setTotalExp(int totalExp) {
      this.totalExp = totalExp;
   }

   public int getTotalKills() {
      return this.totalKills;
   }

   public void setTotalKills(int totalKills) {
      this.totalKills = totalKills;
   }

   public int getCurrentExp() {
      return this.currentExp;
   }

   public void setCurrentExp(int currentExp) {
      this.currentExp = currentExp;
   }

   public int getWins() {
      return this.wins;
   }

   public void setWins(int wins) {
      this.wins = wins;
   }

   public int getLosses() {
      return this.losses;
   }

   public void setLosses(int losses) {
      this.losses = losses;
   }

   public void addExp(int amount) {
      this.totalExp += amount;
      this.currentExp += amount;
   }

   public void setExp(int exp) {
      this.currentExp = exp;
   }

   public void resetWinLoss() {
      this.wins = 0;
      this.losses = 0;
   }

   public int getTotalBred() {
      return this.totalBred;
   }

   public void setTotalBred(int totalBred) {
      this.totalBred = totalBred;
   }

   public void addToTotalBred() {
      ++this.totalBred;
   }

   public int getTotalHatched() {
      return this.totalHatched;
   }

   public void setTotalHatched(int totalHatched) {
      this.totalHatched = totalHatched;
   }

   public void addHatched() {
      ++this.totalHatched;
   }

   public int getTotalEvolved() {
      return this.totalEvolved;
   }

   public void setTotalEvolved(int totalEvolved) {
      this.totalEvolved = totalEvolved;
   }

   public HashMap getCaughtTypeCount() {
      return this.caughtTypeCount;
   }

   public void setCaughtTypeCount(HashMap caughtTypeCount) {
      this.caughtTypeCount = caughtTypeCount;
   }

   public void addCaughtType(EnumType type) {
      int count = (Integer)this.caughtTypeCount.get(type.func_176610_l()) + 1;
      this.caughtTypeCount.put(type.func_176610_l(), count);
   }

   public void addCaughtTypes(ArrayList types) {
      Iterator var2 = types.iterator();

      while(var2.hasNext()) {
         EnumType type = (EnumType)var2.next();
         int count = (Integer)this.caughtTypeCount.get(type.func_176610_l()) + 1;
         this.caughtTypeCount.put(type.func_176610_l(), count);
      }

   }
}
