package com.pixelmonmod.tcg.api.card.spec.tcg;

import com.pixelmonmod.tcg.api.card.Card;
import com.pixelmonmod.tcg.api.card.spec.AbstractSpecification;
import java.util.List;
import net.minecraft.item.ItemStack;

public class CardSpecification extends AbstractSpecification {
   public CardSpecification(String originalSpec, List requirements) {
      super(Card.class, ItemStack.class, originalSpec, requirements);
   }

   public CardSpecification clone() {
      return CardSpecificationProxy.create(this.originalSpec);
   }

   public Card create() {
      return this.create(false);
   }

   public Card create(boolean shallow) {
      return null;
   }
}
