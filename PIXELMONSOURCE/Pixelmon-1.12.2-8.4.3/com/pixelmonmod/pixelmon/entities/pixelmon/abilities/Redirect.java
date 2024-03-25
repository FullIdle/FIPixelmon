package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public abstract class Redirect extends AbilityBase {
   EnumType type;
   String langImmune;
   String langRedirect;

   public Redirect(EnumType type, String langImmune, String langRedirect) {
      this.type = type;
      this.langImmune = langImmune;
      this.langRedirect = langRedirect;
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() == this.type && !user.getBattleAbility().isAbility(MoldBreaker.class)) {
         if (a.getTypeEffectiveness(user, pokemon) == 0.0) {
            pokemon.bc.sendToAll("pixelmon.battletext.noeffect", pokemon.getNickname());
            return false;
         } else {
            pokemon.bc.sendToAll(this.langImmune, pokemon.getNickname());
            if (pokemon.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack)) {
               MoveResults var10000 = a.moveResult;
               var10000.weightMod -= 25.0F;
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public boolean redirectAttack(PixelmonWrapper user, PixelmonWrapper targetAlly, Attack attack) {
      if (attack.getType() == this.type && !attack.isAttack("Snipe Shot") && !user.getBattleAbility().isAbility(PropellerTail.class) && !user.getBattleAbility().isAbility(Stalwart.class) && !user.getBattleAbility().isAbility(MoldBreaker.class)) {
         targetAlly.bc.sendToAll(this.langRedirect, targetAlly.getNickname());
         return true;
      } else {
         return false;
      }
   }
}
