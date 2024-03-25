package com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
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

public class ContainerMechanicalAnvil extends Container {
   private TileEntityMechanicalAnvil tileMechanicalAnvil;
   private int lastHammerTime;
   private int lastRunTime;
   private int lastItemRunTime;

   public ContainerMechanicalAnvil(InventoryPlayer inventoryPlayer, TileEntityMechanicalAnvil tileMechanicalAnvil) {
      this.tileMechanicalAnvil = tileMechanicalAnvil;
      this.func_75146_a(new SlotObeyTE(tileMechanicalAnvil, 0, 56, 17));
      this.func_75146_a(new SlotObeyTE(tileMechanicalAnvil, 1, 56, 53));
      this.func_75146_a(new SlotFurnaceOutput(inventoryPlayer.field_70458_d, tileMechanicalAnvil, 2, 116, 35));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public void func_75142_b() {
      super.func_75142_b();
      Iterator var1 = this.field_75149_d.iterator();

      while(var1.hasNext()) {
         Object crafter = var1.next();
         IContainerListener listener = (IContainerListener)crafter;
         if (this.lastRunTime != this.tileMechanicalAnvil.fuelBurnTime) {
            listener.func_71112_a(this, 0, this.tileMechanicalAnvil.fuelBurnTime);
         }

         if (this.lastItemRunTime != this.tileMechanicalAnvil.currentFuelBurnTime) {
            listener.func_71112_a(this, 1, this.tileMechanicalAnvil.currentFuelBurnTime);
         }

         if (this.lastHammerTime != this.tileMechanicalAnvil.anvilHammerTime) {
            listener.func_71112_a(this, 2, this.tileMechanicalAnvil.anvilHammerTime);
         }
      }

      this.lastHammerTime = this.tileMechanicalAnvil.anvilHammerTime;
      this.lastRunTime = this.tileMechanicalAnvil.fuelBurnTime;
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int id, int data) {
      this.tileMechanicalAnvil.func_174885_b(id, data);
   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.tileMechanicalAnvil.func_70300_a(player);
   }

   public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(slotIndex);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if (slotIndex == 2) {
            if (!this.func_75135_a(itemstack1, 3, 39, true)) {
               return ItemStack.field_190927_a;
            }

            slot.func_75220_a(itemstack1, itemstack);
         } else if (slotIndex != 1 && slotIndex != 0) {
            if (!TileEntityMechanicalAnvil.getHammerResult(this.tileMechanicalAnvil, itemstack1).func_190926_b()) {
               if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (TileEntityFurnace.func_145954_b(itemstack1)) {
               if (!this.func_75135_a(itemstack1, 1, 2, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (slotIndex >= 3 && slotIndex < 30) {
               if (!this.func_75135_a(itemstack1, 30, 39, false)) {
                  return ItemStack.field_190927_a;
               }
            } else if (slotIndex >= 30 && slotIndex < 39 && !this.func_75135_a(itemstack1, 3, 30, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (!this.func_75135_a(itemstack1, 3, 39, false)) {
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
