package com.pixelmonmod.tcg.api.card.attack;

import com.pixelmonmod.tcg.api.card.CardModifier;
import com.pixelmonmod.tcg.api.card.Energy;
import java.util.List;

public class AttackCard {
   private String cardCode;
   private int attackNumber;
   private String name;
   private Energy[] energy;
   private int damage;
   private CardModifier cardModifier;
   private String text;
   private List effects;

   public String getCardCode() {
      return this.cardCode;
   }

   public int getAttackNumber() {
      return this.attackNumber;
   }

   public String getName() {
      return this.name;
   }

   public Energy[] getEnergy() {
      return this.energy;
   }

   public int getDamage() {
      return this.damage;
   }

   public CardModifier getModifier() {
      return this.cardModifier;
   }

   public String getText() {
      return this.text;
   }

   public List getEffects() {
      return this.effects;
   }
}
