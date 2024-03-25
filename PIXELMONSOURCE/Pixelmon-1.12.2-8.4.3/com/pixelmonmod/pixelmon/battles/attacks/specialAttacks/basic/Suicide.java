package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Damp;
import java.util.ArrayList;
import java.util.Iterator;

public class Suicide extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hasDamp = false;
      Iterator var4 = user.bc.getActiveUnfaintedPokemon().iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         if (pw.getBattleAbility(user) instanceof Damp) {
            hasDamp = true;
            break;
         }
      }

      if (hasDamp) {
         if (target.getBattleAbility() instanceof Damp) {
            user.bc.sendToAll("pixelmon.abilities.damp", target.getNickname());
         }

         return AttackResult.failed;
      } else if (target.isFainted()) {
         return AttackResult.notarget;
      } else {
         if (user.isAlive()) {
            user.bc.sendToAll("pixelmon.effect.suicide", user.getNickname());
            user.doBattleDamage(user, (float)user.getHealth(), DamageTypeEnum.SELF);
         }

         return AttackResult.proceed;
      }
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      try {
         this.applyEffectDuring(user, target);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.weight = 0.0F;
   }
}
