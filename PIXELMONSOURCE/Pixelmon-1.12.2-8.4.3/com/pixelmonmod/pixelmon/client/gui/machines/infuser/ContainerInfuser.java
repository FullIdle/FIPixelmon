package com.pixelmonmod.pixelmon.client.gui.machines.infuser;

import com.pixelmonmod.pixelmon.api.recipe.InfuserRecipes;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil.SlotObeyTE;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInfuser extends Container {
   private TileEntityInfuser tileInfuser;
   private int lastInfusionTime;
   private int lastRunTime;
   private int lastItemRunTime;

   public ContainerInfuser(InventoryPlayer inventoryPlayer, TileEntityInfuser tileInfuser) {
      this.tileInfuser = tileInfuser;
      this.func_75146_a(new SlotObeyTE(tileInfuser, 0, 28, 30));
      this.func_75146_a(new SlotObeyTE(tileInfuser, 1, 82, 22));
      this.func_75146_a(new SlotObeyTE(tileInfuser, 2, 82, 41));
      this.func_75146_a(new SlotFurnaceOutput(inventoryPlayer.field_70458_d, tileInfuser, 3, 136, 30));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 10 + j * 18, 70 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 10 + i * 18, 128));
      }

   }

   public void func_75142_b() {
      super.func_75142_b();
      Iterator var1 = this.field_75149_d.iterator();

      while(var1.hasNext()) {
         Object crafter = var1.next();
         IContainerListener icrafting = (IContainerListener)crafter;
         if (this.lastInfusionTime != this.tileInfuser.infuserProgressTime) {
            icrafting.func_71112_a(this, 0, this.tileInfuser.infuserProgressTime);
         }

         if (this.lastRunTime != this.tileInfuser.infuserRunTime) {
            icrafting.func_71112_a(this, 1, this.tileInfuser.infuserRunTime);
         }

         if (this.lastItemRunTime != this.tileInfuser.currentItemRunTime) {
            icrafting.func_71112_a(this, 2, this.tileInfuser.currentItemRunTime);
         }
      }

      this.lastInfusionTime = this.tileInfuser.infuserProgressTime;
      this.lastRunTime = this.tileInfuser.infuserRunTime;
      this.lastItemRunTime = this.tileInfuser.currentItemRunTime;
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int p_75137_1_, int p_75137_2_) {
      if (p_75137_1_ == 0) {
         this.tileInfuser.infuserProgressTime = p_75137_2_;
      }

      if (p_75137_1_ == 1) {
         this.tileInfuser.infuserRunTime = p_75137_2_;
      }

      if (p_75137_1_ == 2) {
         this.tileInfuser.currentItemRunTime = p_75137_2_;
      }

   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.tileInfuser.func_70300_a(player);
   }

   public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(slotIndex);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if (slotIndex == 3) {
            if (!this.func_75135_a(itemstack1, 4, 40, true)) {
               return ItemStack.field_190927_a;
            }

            slot.func_75220_a(itemstack1, itemstack);
         } else if (slotIndex != 2 && slotIndex != 1 && slotIndex != 0) {
            if (TileEntityFurnace.func_145954_b(itemstack1)) {
               if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (InfuserRecipes.instance().isValidEssence(itemstack1)) {
               if (!this.func_75135_a(itemstack1, 1, 2, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (InfuserRecipes.instance().isValidSolvent(itemstack1)) {
               if (!this.func_75135_a(itemstack1, 2, 3, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (slotIndex >= 4 && slotIndex < 31) {
               if (!this.func_75135_a(itemstack1, 31, 40, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (slotIndex >= 31 && slotIndex < 40 && !this.func_75135_a(itemstack1, 4, 31, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (!this.func_75135_a(itemstack1, 4, 40, false)) {
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
      } else {
         itemstack = ItemStack.field_190927_a;
      }

      return itemstack;
   }
}
