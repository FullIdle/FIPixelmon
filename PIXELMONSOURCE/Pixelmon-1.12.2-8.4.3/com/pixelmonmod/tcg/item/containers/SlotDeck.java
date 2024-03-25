package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.item.ItemCard;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDeck extends Slot {
   public SlotDeck(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
   }

   public boolean func_75214_a(ItemStack itemstack) {
      return itemstack.func_77973_b() instanceof ItemCard;
   }

   public int func_75219_a() {
      return 1;
   }
}
