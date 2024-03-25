package com.pixelmonmod.tcg.inventory;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPrinterPhoto extends Slot {
   public SlotPrinterPhoto(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
      super(inventoryIn, slotIndex, xPosition, yPosition);
   }

   public boolean func_75214_a(ItemStack stack) {
      return stack != null && stack.func_77973_b() != null && stack.func_77973_b() == PixelmonItems.itemPixelmonSprite;
   }
}
