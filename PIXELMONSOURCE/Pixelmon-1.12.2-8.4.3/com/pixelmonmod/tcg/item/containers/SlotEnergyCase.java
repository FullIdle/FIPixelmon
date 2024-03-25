package com.pixelmonmod.tcg.item.containers;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.item.ItemCard;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnergyCase extends Slot {
   public SlotEnergyCase(IInventory inv, int index, int xPos, int yPos) {
      super(inv, index, xPos, yPos);
   }

   public boolean func_75214_a(ItemStack itemstack) {
      if (itemstack.func_77973_b() instanceof ItemCard) {
         if (!itemstack.func_77942_o()) {
            return false;
         } else {
            ImmutableCard c1 = CardRegistry.fromId(itemstack.func_77978_p().func_74762_e("CardID"));
            ImmutableCard c2 = CardRegistry.fromId(itemstack.func_77978_p().func_74762_e("CardID"));
            return c1.getCardType() == CardType.ENERGY && c1.isSpecial() || c2.getCardType() == CardType.ENERGY && c2.isSpecial();
         }
      } else {
         return false;
      }
   }

   public int func_75219_a() {
      return 4000;
   }
}
