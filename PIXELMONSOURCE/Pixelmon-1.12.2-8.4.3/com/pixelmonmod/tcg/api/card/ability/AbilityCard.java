package com.pixelmonmod.tcg.api.card.ability;

import com.pixelmonmod.tcg.api.card.CardAbilityType;
import com.pixelmonmod.tcg.duel.ability.BaseAbilityEffect;

public class AbilityCard {
   private String id;
   private CardAbilityType type;
   private BaseAbilityEffect effect;

   public AbilityCard(String id, CardAbilityType type, BaseAbilityEffect effect) {
      this.id = id;
      this.type = type;
      this.effect = effect;
   }

   public String getID() {
      return this.id;
   }

   public String getName() {
      return "ability." + this.id + ".name";
   }

   public String getDescription() {
      return "ability." + this.id + ".text";
   }

   public CardAbilityType getType() {
      return this.type;
   }

   public BaseAbilityEffect getEffect() {
      return this.effect;
   }
}
