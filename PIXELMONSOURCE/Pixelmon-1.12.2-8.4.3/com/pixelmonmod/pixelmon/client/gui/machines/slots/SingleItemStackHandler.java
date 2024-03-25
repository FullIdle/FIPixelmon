package com.pixelmonmod.pixelmon.client.gui.machines.slots;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class SingleItemStackHandler extends Slot {
   private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
   private final IItemHandler itemHandler;
   private final int index;

   public SingleItemStackHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(emptyInventory, index, xPosition, yPosition);
      this.itemHandler = itemHandler;
      this.index = 0;
   }

   public boolean func_75214_a(@Nonnull ItemStack stack) {
      if (stack.func_190926_b()) {
         return false;
      } else {
         IItemHandler handler = this.getItemHandler();
         ItemStack remainder;
         if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable)handler;
            ItemStack currentStack = handlerModifiable.getStackInSlot(0);
            handlerModifiable.setStackInSlot(0, ItemStack.field_190927_a);
            remainder = handlerModifiable.insertItem(0, stack, true);
            handlerModifiable.setStackInSlot(0, currentStack);
         } else {
            remainder = handler.insertItem(0, stack, true);
         }

         return remainder.func_190926_b() || remainder.func_190916_E() < stack.func_190916_E();
      }
   }

   @Nonnull
   public ItemStack func_75211_c() {
      return this.getItemHandler().getStackInSlot(0);
   }

   public void func_75215_d(@Nonnull ItemStack stack) {
      ((IItemHandlerModifiable)this.getItemHandler()).setStackInSlot(this.index, stack);
      this.func_75218_e();
   }

   public void func_75220_a(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
   }

   public int func_75219_a() {
      return this.itemHandler.getSlotLimit(this.index);
   }

   public int func_178170_b(@Nonnull ItemStack stack) {
      ItemStack maxAdd = stack.func_77946_l();
      int maxInput = stack.func_77976_d();
      maxAdd.func_190920_e(maxInput);
      IItemHandler handler = this.getItemHandler();
      ItemStack currentStack = handler.getStackInSlot(this.index);
      if (handler instanceof IItemHandlerModifiable) {
         IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable)handler;
         handlerModifiable.setStackInSlot(this.index, ItemStack.field_190927_a);
         ItemStack remainder = handlerModifiable.insertItem(this.index, maxAdd, true);
         handlerModifiable.setStackInSlot(this.index, currentStack);
         return maxInput - remainder.func_190916_E();
      } else {
         ItemStack remainder = handler.insertItem(this.index, maxAdd, true);
         int current = currentStack.func_190916_E();
         int added = maxInput - remainder.func_190916_E();
         return current + added;
      }
   }

   public boolean func_82869_a(EntityPlayer playerIn) {
      return !this.getItemHandler().extractItem(this.index, 1, true).func_190926_b();
   }

   @Nonnull
   public ItemStack func_75209_a(int amount) {
      return this.getItemHandler().extractItem(this.index, amount, false);
   }

   public IItemHandler getItemHandler() {
      return this.itemHandler;
   }

   public boolean isSameInventory(Slot other) {
      return other instanceof SlotItemHandler && ((SlotItemHandler)other).getItemHandler() == this.itemHandler;
   }
}
