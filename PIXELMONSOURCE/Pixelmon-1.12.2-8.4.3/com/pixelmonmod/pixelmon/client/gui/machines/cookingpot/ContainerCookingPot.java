package com.pixelmonmod.pixelmon.client.gui.machines.cookingpot;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCookingPot extends Container {
   private TileEntityCookingPot tileCookingPot;

   public ContainerCookingPot(InventoryPlayer inventoryPlayer, TileEntityCookingPot tileInfuser) {
      this.tileCookingPot = tileInfuser;

      int i;
      for(i = 0; i < 5; ++i) {
         this.func_75146_a(new Slot(tileInfuser, i, 28 + 18 * i, 17) {
            public boolean func_75214_a(ItemStack stack) {
               return this.field_75224_c.func_94041_b(this.getSlotIndex(), stack);
            }
         });
         this.func_75146_a(new Slot(tileInfuser, 5 + i, 28 + 18 * i, 35) {
            public boolean func_75214_a(ItemStack stack) {
               return this.field_75224_c.func_94041_b(this.getSlotIndex(), stack);
            }
         });
      }

      this.func_75146_a(new Slot(tileInfuser, 10, 136, 27) {
         public boolean func_75214_a(ItemStack stack) {
            return this.field_75224_c.func_94041_b(this.getSlotIndex(), stack);
         }
      });

      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 10 + j * 18, 70 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 10 + i * 18, 128));
      }

   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.tileCookingPot.func_70300_a(player);
   }

   public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
      return ItemStack.field_190927_a;
   }
}
