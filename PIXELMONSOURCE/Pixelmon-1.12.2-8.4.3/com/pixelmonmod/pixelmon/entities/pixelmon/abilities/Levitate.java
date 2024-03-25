package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Levitate extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (pokemon.isGrounded()) {
         return true;
      } else {
         return a.getType() != EnumType.Ground || a.getAttackCategory() == AttackCategory.STATUS || a.isAttack("Thousand Arrows");
      }
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      pokemon.bc.sendToAll("pixelmon.abilities.levitate", pokemon.getNickname());
   }
}
