package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Dancer extends AbilityBase {
   private static final String[] danceMoves = new String[]{"Feather Dance", "Fiery Dance", "Dragon Dance", "Lunar Dance", "Petal Dance", "Revelation Dance", "Quiver Dance", "Swords Dance", "Teeter Dance"};

   public void postProcessAttackOther(PixelmonWrapper pokemon, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (pokemon != user && !a.fromDancer && a.isAttack(danceMoves)) {
         a.fromDancer = true;
         this.sendActivatedMessage(pokemon);
         Attack lastUsed = pokemon.lastAttack;
         PixelmonWrapper newTarget;
         if (!a.isAttack("Teeter Dance") && !a.isAttack("Feather Dance")) {
            if (a.getAttackCategory() == AttackCategory.STATUS) {
               newTarget = pokemon;
            } else if (user.isSameTeam(pokemon)) {
               newTarget = target;
            } else {
               newTarget = user;
            }
         } else {
            newTarget = user;
         }

         pokemon.useTempAttack(a, Lists.newArrayList(new PixelmonWrapper[]{newTarget}), false);
         pokemon.lastAttack = lastUsed;
         a.fromDancer = false;
      }

   }
}
