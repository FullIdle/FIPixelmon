package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemWeaknessPolicy extends ItemHeld {
   public ItemWeaknessPolicy() {
      super(EnumHeldItems.weaknessPolicy, "weakness_policy");
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      if ((damage > 0.0F || target.getSpecies() == EnumSpecies.Shedinja) && attack.getTypeEffectiveness(attacker, target) >= 2.0) {
         target.bc.sendToAll("pixelmon.abilities.activated", target.getNickname(), target.getHeldItem().getLocalizedName());
         target.getBattleStats().modifyStat(2, (StatsType[])(StatsType.Attack, StatsType.SpecialAttack));
         target.consumeItem();
      }

   }
}
