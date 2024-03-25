package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class WonderGuard extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return a.getAttackCategory() == AttackCategory.STATUS || a.getType() == EnumType.Mystery || a.getTypeEffectiveness(user, pokemon) >= 2.0;
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      pokemon.bc.sendToAll("pixelmon.abilities.wonderguard", pokemon.getNickname());
   }
}
