package com.pixelmonmod.pixelmon.worldGeneration.structure.gyms;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;

public class WorldGymData extends WorldSavedData {
   private ArrayList gymList = new ArrayList();

   public WorldGymData() {
      super("gyminfo");
   }

   public WorldGymData(String name) {
      super(name);
   }

   public void addGym(String name, ItemStack badge, int level) {
      this.gymList.add(new WorldGymInfo(name, badge, level));
      this.func_76186_a(true);
   }

   public int getGymCount() {
      return this.gymList.size();
   }

   public void func_76184_a(NBTTagCompound nbt) {
      NBTTagList tagList = nbt.func_150295_c("gymlist", 10);

      for(int i = 0; i < tagList.func_74745_c(); ++i) {
         if (tagList.func_179238_g(i) instanceof NBTTagCompound) {
            NBTTagCompound gymTag = tagList.func_150305_b(i);
            this.gymList.add(new WorldGymInfo(gymTag));
         }
      }

   }

   public NBTTagCompound func_189551_b(NBTTagCompound nbt) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.gymList.size(); ++i) {
         nbttaglist.func_74742_a(((WorldGymInfo)this.gymList.get(i)).getTag());
      }

      nbt.func_74782_a("gymlist", nbttaglist);
      return nbt;
   }

   public ArrayList getGymList() {
      return this.gymList;
   }

   public int getGymLevel() {
      int size = this.gymList.size();
      if (size == 0) {
         return 15;
      } else if (size == 1) {
         return 25;
      } else if (size == 2) {
         return 35;
      } else {
         return size == 3 ? 50 : -1;
      }
   }
}
