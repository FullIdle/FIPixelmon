package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumTypeEnhancingItems;

public class ItemTimespaceOrb extends TypeEnhancingItems {
   EnumSpecies pokemon;

   public ItemTimespaceOrb(String itemName, EnumType type, EnumSpecies pokemon) {
      super(EnumTypeEnhancingItems.orb, itemName, type);
      this.pokemon = pokemon;
   }

   public double preProcessDamagingAttackUser(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, double damage) {
      if (this.pokemon == attacker.getSpecies()) {
         if (attack.getMove().getAttackType() == EnumType.Dragon) {
            return damage * 1.2;
         }

         if (this.type != null && attack.getMove().getAttackType() == this.type) {
            return damage * 1.2;
         }
      }

      return damage;
   }
}
