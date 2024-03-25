package com.pixelmonmod.pixelmon.client.gui.machines.washingmachine;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileWashingMachine;
import com.pixelmonmod.pixelmon.client.gui.machines.slots.SingleItemStackHandler;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public class ContainerWashingMachine extends Container {
   private final TileWashingMachine te;
   private int lastProgress = 0;

   public ContainerWashingMachine(InventoryPlayer inventory, TileWashingMachine tileEntity) {
      this.te = tileEntity;
      this.addOwnSlots();
      this.addPlayerSlots(inventory);
   }

   private void addPlayerSlots(IInventory playerInventory) {
      int row;
      int x;
      for(row = 0; row < 3; ++row) {
         for(x = 0; x < 9; ++x) {
            int x = 9 + x * 18;
            int y = row * 18 + 85;
            this.func_75146_a(new Slot(playerInventory, x + row * 9 + 10, x - 1, y - 1));
         }
      }

      for(row = 0; row < 9; ++row) {
         x = 9 + row * 18;
         int y = 143;
         this.func_75146_a(new Slot(playerInventory, row, x - 1, y - 1));
      }

   }

   private void addOwnSlots() {
      IItemHandler cooked = this.te.getCooked();
      IItemHandler cooking = this.te.getCooking();
      this.func_75146_a(new SingleItemStackHandler(cooking, 0, 54, 33));
      this.func_75146_a(new SingleItemStackHandler(cooked, 1, 111, 30));
   }

   public void func_75142_b() {
      super.func_75142_b();

      for(int i = 0; i < this.field_75149_d.size(); ++i) {
         IContainerListener crafting = (IContainerListener)this.field_75149_d.get(i);
         if (this.lastProgress != this.te.getSmeltTime()) {
            crafting.func_71112_a(this, 0, this.te.getSmeltTime());
         }
      }

      this.lastProgress = this.te.getSmeltTime();
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int id, int data) {
      if (id == 0) {
         this.te.setSmeltTime(data);
      } else if (id == 1) {
      }

   }

   @Nullable
   public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
      return ItemStack.field_190927_a;
   }

   public boolean func_75145_c(EntityPlayer playerIn) {
      return true;
   }
}
