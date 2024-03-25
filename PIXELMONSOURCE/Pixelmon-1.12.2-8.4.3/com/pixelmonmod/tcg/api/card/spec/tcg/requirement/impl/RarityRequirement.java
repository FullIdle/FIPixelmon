package com.pixelmonmod.tcg.api.card.spec.tcg.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.tcg.api.card.Card;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import com.pixelmonmod.tcg.api.card.spec.tcg.requirement.AbstractCardRequirement;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RarityRequirement extends AbstractCardRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"rarity"});
   private CardRarity rarity;

   public RarityRequirement() {
      super(KEYS);
   }

   public RarityRequirement(CardRarity rarity) {
      this();
      this.rarity = rarity;
   }

   public Requirement createInstance(CardRarity value) {
      return new RarityRequirement(value);
   }

   public boolean isDataMatch(Card card) {
      return card.getBase().getRarity() == this.rarity;
   }

   public void applyData(Card card) {
   }

   public CardRarity getValue() {
      return this.rarity;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         return args.length != 0 && args.length != 1 ? Collections.singletonList(this.createInstance(CardRarity.valueOf(args[1].toUpperCase()))) : Collections.emptyList();
      }
   }
}
