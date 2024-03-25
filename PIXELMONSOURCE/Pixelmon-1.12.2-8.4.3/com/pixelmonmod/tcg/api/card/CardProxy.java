package com.pixelmonmod.tcg.api.card;

import net.minecraft.item.ItemStack;

public class CardProxy {
   private static CardFactory factory;

   public static void setFactory(CardFactory factory) {
      CardProxy.factory = factory;
   }

   public static ItemStack createCardItem(ImmutableCard card) {
      return factory.createCardItem(card);
   }

   public static Card createCard(ImmutableCard card) {
      return factory.createCard(card);
   }

   public static Card readCard(ItemStack itemStack) {
      return factory.readCard(itemStack);
   }
}
