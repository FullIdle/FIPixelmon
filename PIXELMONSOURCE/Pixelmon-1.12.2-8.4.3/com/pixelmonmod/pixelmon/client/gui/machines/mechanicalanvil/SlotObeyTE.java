package com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotObeyTE extends Slot {
   public SlotObeyTE(ISidedInventory inventory, int slotID, int x, int y) {
      super(inventory, slotID, x, y);
   }

   public boolean func_75214_a(ItemStack itemStack) {
      return this.field_75224_c.func_94041_b(this.getSlotIndex(), itemStack);
   }
}
