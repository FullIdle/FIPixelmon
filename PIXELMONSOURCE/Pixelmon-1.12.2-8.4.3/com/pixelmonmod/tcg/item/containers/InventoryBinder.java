package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.item.ItemCard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryBinder extends InventoryBasic {
   private String name = "Binder";
   public static String TAG_NAME = "BinderInventory";
   private final EntityPlayer player;
   private final ItemStack item;
   public static final int size = 288;
   public static final int sizePerPage = 24;
   private NonNullList inventory;
   public int page = 0;

   public InventoryBinder(EntityPlayer player) {
      super("container.binder", false, 288);
      this.player = player;
      this.item = player.func_184614_ca();
      this.inventory = NonNullList.func_191197_a(288, ItemStack.field_190927_a);
      this.readFromNBT(this.item.func_77978_p());
   }

   public String func_70005_c_() {
      return this.name;
   }

   public void writeToNBT(NBTTagCompound tagcompound) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.func_70302_i_(); ++i) {
         if (this.func_70301_a(i) != ItemStack.field_190927_a) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74768_a("Slot", i);
            this.func_70301_a(i).func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      tagcompound.func_74782_a(TAG_NAME, nbttaglist);
   }

   public void readFromNBT(NBTTagCompound compound) {
      NBTTagList items = compound.func_150295_c(TAG_NAME, compound.func_74732_a());

      for(int i = 0; i < items.func_74745_c(); ++i) {
         NBTTagCompound item = items.func_150305_b(i);
         int slot = item.func_74762_e("Slot");
         if (slot >= 0 && slot < this.func_70302_i_()) {
            this.func_70299_a(slot, new ItemStack(item));
         }
      }

   }

   public boolean func_145818_k_() {
      return this.name.length() > 0;
   }

   public ITextComponent func_145748_c_() {
      return new TextComponentString(this.name);
   }

   public int func_70302_i_() {
      return 288;
   }

   public int getSizePage() {
      return 24;
   }

   public int getPage() {
      return this.page;
   }

   public void incrementPage() {
      if (this.page < 11) {
         this.page = this.getPage() + 1;
      }

   }

   public void decrementPage() {
      if (this.page > 0) {
         this.page = this.getPage() - 1;
      }

   }

   public ItemStack func_70301_a(int index) {
      return (ItemStack)this.inventory.get(index);
   }

   public ItemStack func_70298_a(int index, int count) {
      if (this.inventory.get(index) != ItemStack.field_190927_a) {
         ItemStack itemstack;
         if (((ItemStack)this.inventory.get(index)).func_190916_E() <= count) {
            itemstack = (ItemStack)this.inventory.get(index);
            this.inventory.set(index, ItemStack.field_190927_a);
            return itemstack;
         } else {
            itemstack = ((ItemStack)this.inventory.get(index)).func_77979_a(count);
            if (((ItemStack)this.inventory.get(index)).func_190916_E() == 0) {
               this.inventory.set(index, ItemStack.field_190927_a);
            }

            return itemstack;
         }
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_70304_b(int index) {
      ((ItemStack)this.inventory.get(index)).func_190920_e(((ItemStack)this.inventory.get(index)).func_190916_E() - 1);
      return (ItemStack)this.inventory.get(index);
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.inventory.set(index, stack);
      if (stack != ItemStack.field_190927_a && stack.func_190916_E() > this.func_70297_j_()) {
         stack.func_190920_e(this.func_70297_j_());
      }

      this.func_70296_d();
   }

   public int func_70297_j_() {
      return 1;
   }

   public void func_70296_d() {
      super.func_70296_d();
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
      this.writeToNBT(this.item.func_77978_p());
   }

   public boolean func_94041_b(int index, ItemStack stack) {
      return stack.func_77973_b() instanceof ItemCard;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }
}
