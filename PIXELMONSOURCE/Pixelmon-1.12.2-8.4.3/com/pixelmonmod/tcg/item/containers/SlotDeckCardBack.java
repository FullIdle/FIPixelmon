package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.item.ItemCardBack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDeckCardBack extends Slot {
   public SlotDeckCardBack(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
   }

   public boolean func_75214_a(ItemStack itemstack) {
      return itemstack.func_77973_b() instanceof ItemCardBack;
   }
}
