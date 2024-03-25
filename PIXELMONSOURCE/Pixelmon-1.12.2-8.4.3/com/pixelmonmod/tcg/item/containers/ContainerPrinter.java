package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.inventory.SlotPrinterBlankCard;
import com.pixelmonmod.tcg.inventory.SlotPrinterOutput;
import com.pixelmonmod.tcg.inventory.SlotPrinterPhoto;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPrinter extends Container {
   private final TileEntityPrinter tileEntityPrinter;

   public ContainerPrinter(InventoryPlayer inventoryPlayer, TileEntityPrinter tileEntityPrinter) {
      this.tileEntityPrinter = tileEntityPrinter;
      this.func_75146_a(new SlotPrinterBlankCard(tileEntityPrinter, 0, 55, 8));
      this.func_75146_a(new SlotPrinterPhoto(tileEntityPrinter, 1, 81, 32));
      int slotSpace = 20;

      int i;
      for(i = 0; i < 8; ++i) {
         this.func_75146_a(new SlotPrinterOutput(inventoryPlayer.field_70458_d, tileEntityPrinter, i + 2, 118 + i / 4 * slotSpace, 4 + i % 4 * slotSpace));
      }

      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 103 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 161));
      }

   }

   public boolean func_75145_c(EntityPlayer playerIn) {
      return true;
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
      }

      return itemstack;
   }
}
