package com.pixelmonmod.tcg.item.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBinder extends Container {
   public final InventoryBinder inventory;
   private static final int INV_S = 288;
   private static final int INV_E = 314;
   private static final int HB_S = 315;
   private static final int HB_E = 323;

   public ContainerBinder(EntityPlayer player, InventoryPlayer invPlayer, InventoryBinder inventory) {
      this.inventory = inventory;
      int count = 0;

      int i;
      int j;
      for(i = 0; i < 288; ++i) {
         j = inventory.getPage() * inventory.getSizePage();
         if (i >= j && count < 24) {
            ++count;
            this.func_75146_a(new SlotDeck(this.inventory, i, -1 + 18 * (i / 6), -10 + 18 * (i % 6)));
         } else {
            this.func_75146_a(new SlotDeck(this.inventory, i, -40, -40));
         }
      }

      for(i = 0; i < 3; ++i) {
         for(j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 93 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(invPlayer, i, 8 + i * 18, 151));
      }

   }

   public boolean func_75145_c(EntityPlayer playerIn) {
      return this.inventory.func_70300_a(playerIn);
   }

   public ItemStack func_82846_b(EntityPlayer player, int index) {
      ItemStack itemstack = ItemStack.field_190927_a;
      Slot slot = (Slot)this.field_75151_b.get(index);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         int containerSlots = this.field_75151_b.size() - player.field_71071_by.field_70462_a.size();
         if (index < containerSlots) {
            if (!this.func_75135_a(itemstack1, containerSlots, this.field_75151_b.size(), true)) {
               return ItemStack.field_190927_a;
            }
         } else if (!this.func_75135_a(itemstack1, 0, containerSlots, false)) {
            return ItemStack.field_190927_a;
         }

         if (itemstack1.func_190916_E() == 0) {
            slot.func_75215_d(ItemStack.field_190927_a);
         } else {
            slot.func_75218_e();
         }

         if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
            return ItemStack.field_190927_a;
         }

         slot.func_190901_a(player, itemstack1);
         slot.func_75218_e();
         this.inventory.func_70296_d();
      }

      return itemstack;
   }

   public ItemStack func_184996_a(int slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {
      return slot >= 0 && this.func_75139_a(slot) != null && this.func_75139_a(slot).func_75211_c() == player.func_184614_ca() ? ItemStack.field_190927_a : super.func_184996_a(slot, dragType, clickTypeIn, player);
   }

   protected boolean func_75135_a(ItemStack stack, int start, int end, boolean backwards) {
      boolean flag1 = false;
      int k = backwards ? end - 1 : start;
      Slot slot;
      ItemStack itemstack1;
      int l;
      if (stack.func_77985_e()) {
         label116:
         while(true) {
            while(true) {
               if (stack.func_190916_E() <= 0 || (backwards || k >= end) && (!backwards || k < start)) {
                  break label116;
               }

               slot = (Slot)this.field_75151_b.get(k);
               itemstack1 = slot.func_75211_c();
               if (!slot.func_75214_a(stack)) {
                  k += backwards ? -1 : 1;
               } else {
                  if (!itemstack1.func_190926_b() && itemstack1.func_77973_b() == stack.func_77973_b() && (!stack.func_77981_g() || stack.func_77952_i() == itemstack1.func_77952_i()) && ItemStack.func_77970_a(stack, itemstack1)) {
                     l = itemstack1.func_190916_E() + stack.func_190916_E();
                     if (l <= stack.func_77976_d() && l <= slot.func_75219_a()) {
                        stack.func_190920_e(0);
                        itemstack1.func_190920_e(l);
                        this.inventory.func_70296_d();
                        flag1 = true;
                     } else if (itemstack1.func_190916_E() < stack.func_77976_d() && l < slot.func_75219_a()) {
                        stack.func_190920_e(stack.func_190916_E() - stack.func_77976_d() - itemstack1.func_190916_E());
                        itemstack1.func_190920_e(stack.func_77976_d());
                        this.inventory.func_70296_d();
                        flag1 = true;
                     }
                  }

                  k += backwards ? -1 : 1;
               }
            }
         }
      }

      if (stack.func_190916_E() > 0) {
         k = backwards ? end - 1 : start;

         while(!backwards && k < end || backwards && k >= start) {
            slot = (Slot)this.field_75151_b.get(k);
            itemstack1 = slot.func_75211_c();
            if (!slot.func_75214_a(stack)) {
               k += backwards ? -1 : 1;
            } else {
               if (itemstack1.func_190926_b()) {
                  l = stack.func_190916_E();
                  if (l <= slot.func_75219_a()) {
                     slot.func_75215_d(stack.func_77946_l());
                     stack.func_190920_e(0);
                     this.inventory.func_70296_d();
                     flag1 = true;
                     break;
                  }

                  ItemStack newItem = new ItemStack(stack.func_77973_b(), slot.func_75219_a(), stack.func_77952_i());
                  newItem.func_77982_d(stack.func_77978_p());
                  this.func_75141_a(k, newItem);
                  stack.func_190920_e(stack.func_190916_E() - slot.func_75219_a());
                  this.inventory.func_70296_d();
                  flag1 = true;
               }

               k += backwards ? -1 : 1;
            }
         }
      }

      return flag1;
   }

   public void func_75134_a(EntityPlayer player) {
      super.func_75134_a(player);
      this.inventory.func_174886_c(player);
   }
}
