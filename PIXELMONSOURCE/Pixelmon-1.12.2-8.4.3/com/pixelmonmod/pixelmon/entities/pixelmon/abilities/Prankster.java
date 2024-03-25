package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class Prankster extends AbilityBase {
   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if (pokemon.attack.getAttackCategory() == AttackCategory.STATUS) {
         ++priority;
         triggered.setTrue();
      }

      return priority;
   }

   public boolean allowsOutgoingAttack(PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getAttackCategory() == AttackCategory.STATUS && target.type.contains(EnumType.Dark)) {
         if (a.getMove().getTargetingInfo().hitsAll && !a.isAttack("Perish Song", "Rototiller")) {
            return true;
         } else if (a.isAttack(EntryHazard.ENTRY_HAZARDS)) {
            return true;
         } else {
            user.bc.sendToAll("pixelmon.battletext.movefailed");
            return false;
         }
      } else {
         return true;
      }
   }
}
