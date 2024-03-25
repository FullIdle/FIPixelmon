package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Dazzling extends AbilityBase {
   public boolean allowsIncomingAttackTeammate(PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user, Attack a) {
      if (user.priority > 0.0F) {
         if (a.getMove().getTargetingInfo().hitsOppositeFoe && a.getMove().getTargetingInfo().hitsAdjacentFoe && a.getMove().getTargetingInfo().hitsExtendedFoe && !a.getMove().getTargetingInfo().hitsSelf && !a.getMove().getTargetingInfo().hitsAdjacentAlly && !a.getMove().getTargetingInfo().hitsExtendedAlly) {
            return true;
         } else {
            AbilityBase ability = user.getBattleAbility();
            if (!(ability instanceof MoldBreaker) && !(ability instanceof Teravolt) && !(ability instanceof Turboblaze)) {
               if (user.zMove != null) {
                  pokemon.bc.sendToAll("pixelmon.abilities.dazzling", user.getNickname(), user.zMove.getLocalizedName());
               } else {
                  pokemon.bc.sendToAll("pixelmon.abilities.dazzling", user.getNickname(), a.getMove().getTranslatedName());
               }

               return false;
            } else {
               return true;
            }
         }
      } else {
         return true;
      }
   }
}
