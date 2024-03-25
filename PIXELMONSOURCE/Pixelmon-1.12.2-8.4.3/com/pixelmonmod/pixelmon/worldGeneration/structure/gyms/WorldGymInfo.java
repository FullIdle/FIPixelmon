package com.pixelmonmod.pixelmon.worldGeneration.structure.gyms;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class WorldGymInfo {
   public String name;
   public ItemStack badge;
   public int level;

   public WorldGymInfo(String name, ItemStack badge, int level) {
      this.name = name;
      this.badge = badge;
      this.level = level;
   }

   public WorldGymInfo(NBTTagCompound gymTag) {
      this.name = gymTag.func_74779_i("name");
      this.level = gymTag.func_74762_e("level");
      this.badge = new ItemStack((NBTTagCompound)gymTag.func_74781_a("badge"));
   }

   public NBTBase getTag() {
      NBTTagCompound gymTag = new NBTTagCompound();
      gymTag.func_74778_a("name", this.name);
      gymTag.func_74768_a("level", this.level);
      NBTTagCompound itemTag = new NBTTagCompound();
      this.badge.func_77955_b(itemTag);
      gymTag.func_74782_a("badge", itemTag);
      return gymTag;
   }
}
