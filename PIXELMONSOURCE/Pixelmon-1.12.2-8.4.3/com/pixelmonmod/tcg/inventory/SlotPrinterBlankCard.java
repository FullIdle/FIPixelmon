package com.pixelmonmod.tcg.inventory;

import com.pixelmonmod.tcg.item.ItemBlankCard;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPrinterBlankCard extends Slot {
   public SlotPrinterBlankCard(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
      super(inventoryIn, slotIndex, xPosition, yPosition);
   }

   public boolean func_75214_a(ItemStack stack) {
      return stack != ItemStack.field_190927_a && stack.func_77973_b() != null && stack.func_77973_b() instanceof ItemBlankCard;
   }
}
