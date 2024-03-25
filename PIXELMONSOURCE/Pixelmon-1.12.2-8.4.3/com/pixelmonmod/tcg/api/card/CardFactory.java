package com.pixelmonmod.tcg.api.card;

import com.pixelmonmod.tcg.TCG;
import net.minecraft.item.ItemStack;

public interface CardFactory {
   default ItemStack createCardItem(ImmutableCard card) {
      Card instance = this.createCard(card);
      ItemStack itemStack = new ItemStack(TCG.itemCard, 1);
      instance.write(itemStack);
      return itemStack;
   }

   Card createCard(ImmutableCard var1);

   Card readCard(ItemStack var1);
}
