package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class FlowerVeil extends AbilityBase {
   public boolean allowsStatusTeammate(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user) {
      if (user != target && status.isPrimaryStatus() && target.hasType(EnumType.Grass) && !user.getBattleAbility().isAbility(MoldBreaker.class) && !user.getBattleAbility().isAbility(Teravolt.class) && !user.getBattleAbility().isAbility(Turboblaze.class)) {
         if (user != target && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll("pixelmon.abilities.flowerveil", target.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean allowsStatChangeTeammate(PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user, StatsEffect e) {
      if (e.amount < 0 && !e.getUser() && target.hasType(EnumType.Grass) && !user.getBattleAbility().isAbility(MoldBreaker.class) && !user.getBattleAbility().isAbility(Teravolt.class) && !user.getBattleAbility().isAbility(Turboblaze.class)) {
         if (!Attack.dealsDamage(user.attack)) {
            user.bc.sendToAll("pixelmon.abilities.flowerveil", target.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }
}
