package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MegaLauncher;
import java.util.ArrayList;
import java.util.Iterator;

public class Recover extends SpecialAttackBase {
   private int increment = 0;

   public Recover(Value... values) {
      this.increment = values[0].value;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.HealBlock)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else if (target.hasFullHealth()) {
         user.bc.sendToAll("pixelmon.effect.healfailed", target.getNickname());
         return AttackResult.failed;
      } else {
         float healAmount = this.getHealAmount(user, target);
         target.healEntityBy((int)healAmount);
         if (user == target) {
            user.bc.sendToAll("pixelmon.effect.washealed", target.getNickname());
         } else {
            user.bc.sendToAll("pixelmon.effect.healother", user.getNickname(), target.getNickname());
         }

         return AttackResult.succeeded;
      }
   }

   private float getHealAmount(PixelmonWrapper user, PixelmonWrapper target) {
      float healPercent = (float)this.increment;
      if (user != target && user.getBattleAbility() instanceof MegaLauncher) {
         healPercent = (float)((double)healPercent * 1.5);
      }

      return (float)target.getPercentMaxHealth(healPercent);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         float healAmount = this.getHealAmount(pw, target);
         if (pw.getTeamPokemon().contains(target)) {
            userChoice.raiseWeight(target.getHealthPercent(healAmount));
         }
      }

   }
}
