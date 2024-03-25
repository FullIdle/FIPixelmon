package com.pixelmonmod.pixelmon.blocks.tileEntities;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public abstract class TileEntityInventory extends TileEntity implements IBasicInventory, IInteractionObject {
   protected NonNullList contents;
   private String customName;

   protected TileEntityInventory() {
      this.contents = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      if (nbt.func_150297_b("CustomName", 8)) {
         this.customName = nbt.func_74779_i("CustomName");
      }

      this.contents = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
      if (nbt.func_74764_b("Items")) {
         NBTTagList itemsList = nbt.func_150295_c("Items", 10);

         for(int i = 0; i < itemsList.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = itemsList.func_150305_b(i);
            int slot = nbttagcompound.func_74771_c("Slot");
            if (slot >= 0 && slot < this.contents.size()) {
               this.contents.set(slot, new ItemStack(nbttagcompound));
            }
         }
      }

   }

   public boolean addToInventory(ItemStack stack) {
      for(int i = 0; i < this.contents.size(); ++i) {
         if (((ItemStack)this.contents.get(i)).func_190926_b()) {
            this.contents.set(i, stack);
            return true;
         }

         if (((ItemStack)this.contents.get(i)).func_190916_E() < 64) {
            if (((ItemStack)this.contents.get(i)).func_190916_E() + stack.func_190916_E() > 64) {
               stack.func_190920_e((((ItemStack)this.contents.get(i)).func_190916_E() + stack.func_190916_E()) % 64);
               ((ItemStack)this.contents.get(i)).func_190920_e(64);
               return this.addToInventory(stack);
            }

            ((ItemStack)this.contents.get(i)).func_190920_e(((ItemStack)this.contents.get(i)).func_190916_E() + stack.func_190916_E());
            return true;
         }
      }

      return false;
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      if (this.func_145818_k_()) {
         nbt.func_74778_a("CustomName", this.customName);
      }

      NBTTagList itemsList = new NBTTagList();
      nbt.func_74782_a("Items", itemsList);

      for(int slot = 0; slot < this.contents.size(); ++slot) {
         if (!((ItemStack)this.contents.get(slot)).func_190926_b()) {
            NBTTagCompound item = new NBTTagCompound();
            item.func_74774_a("Slot", (byte)slot);
            ((ItemStack)this.contents.get(slot)).func_77955_b(item);
            itemsList.func_74742_a(item);
         }
      }

      return nbt;
   }

   public boolean func_191420_l() {
      Iterator var1 = this.contents.iterator();

      ItemStack itemstack;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         itemstack = (ItemStack)var1.next();
      } while(itemstack == null || itemstack.func_190926_b());

      return false;
   }

   public ItemStack func_70301_a(int index) {
      return (ItemStack)this.contents.get(index);
   }

   public ItemStack func_70298_a(int index, int count) {
      if (!((ItemStack)this.contents.get(index)).func_190926_b()) {
         ItemStack itemstack;
         if (((ItemStack)this.contents.get(index)).func_190916_E() <= count) {
            itemstack = (ItemStack)this.contents.get(index);
            this.contents.set(index, ItemStack.field_190927_a);
            this.func_70296_d();
            return itemstack;
         } else {
            itemstack = ((ItemStack)this.contents.get(index)).func_77979_a(count);
            if (((ItemStack)this.contents.get(index)).func_190916_E() == 0) {
               this.contents.set(index, ItemStack.field_190927_a);
               this.func_70296_d();
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int index) {
      ItemStack itemStack = (ItemStack)this.contents.get(index);
      this.contents.set(index, ItemStack.field_190927_a);
      this.func_70296_d();
      return itemStack;
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.contents.set(index, stack);
      this.func_70296_d();
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) == this && player.func_70092_e((double)this.field_174879_c.func_177958_n() + 0.5, (double)this.field_174879_c.func_177956_o() + 0.5, (double)this.field_174879_c.func_177952_p() + 0.5) <= 64.0;
   }

   public String func_70005_c_() {
      return this.func_145818_k_() ? this.customName : this.getInventoryName();
   }

   abstract String getInventoryName();

   public boolean func_145818_k_() {
      return this.customName != null && this.customName.length() > 0;
   }

   public ITextComponent func_145748_c_() {
      return (ITextComponent)(this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
   }

   public Container func_174876_a(InventoryPlayer playerInventory, EntityPlayer playerIn) {
      return new ContainerChest(playerInventory, this, playerIn);
   }

   public String func_174875_k() {
      return "minecraft:container";
   }
}
