package com.pixelmonmod.tcg.api.card.set;

import com.pixelmonmod.tcg.TCG;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CardSet {
   private int id;
   private String name;
   private boolean hasPack = true;

   public int getID() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getUnlocalizedName() {
      return "set." + this.name + ".name";
   }

   public ItemStack getItemStack(int count) {
      ItemStack pack = new ItemStack(TCG.itemPack, 1);
      pack.func_77982_d(new NBTTagCompound());
      pack.func_77978_p().func_74768_a("SetID", this.getID());
      pack.func_77978_p().func_74776_a("Weight", 0.5F);
      return pack;
   }

   public boolean hasPack() {
      return this.hasPack;
   }
}
