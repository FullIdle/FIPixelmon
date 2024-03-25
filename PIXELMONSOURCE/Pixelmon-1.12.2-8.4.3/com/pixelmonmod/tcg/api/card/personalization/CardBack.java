package com.pixelmonmod.tcg.api.card.personalization;

import com.pixelmonmod.tcg.TCG;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

public class CardBack {
   private final String name;
   private final String ownerName;
   private final ResourceLocation file;

   public CardBack(String name, String ownerName, ResourceLocation file) {
      this.name = name;
      this.ownerName = ownerName;
      this.file = file;
   }

   public String getName() {
      return this.name;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public ResourceLocation getFile() {
      return this.file;
   }

   public ItemStack getItemStack(int count) {
      ItemStack i = new ItemStack(TCG.itemCardBack);
      if (i.func_77978_p() == null) {
         i.func_77982_d(new NBTTagCompound());
      }

      i.func_151001_c(this.name);
      i.func_190920_e(count);
      i.func_77978_p().func_74778_a("CardBackID", this.getName());
      NBTTagList lore = new NBTTagList();
      lore.func_74742_a(new NBTTagString("Artist: " + this.getOwnerName()));
      i.func_190925_c("display").func_74782_a("Lore", lore);
      return i;
   }

   public ItemStack getItemStack() {
      return this.getItemStack(1);
   }
}
