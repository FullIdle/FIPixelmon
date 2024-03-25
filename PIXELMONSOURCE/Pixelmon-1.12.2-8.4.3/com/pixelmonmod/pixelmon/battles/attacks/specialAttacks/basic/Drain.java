package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.LiquidOoze;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBigRoot;
import java.util.ArrayList;
import java.util.Iterator;

public class Drain extends SpecialAttackBase {
   int drainPercent;

   public Drain(Value... values) {
      this.drainPercent = values[0].value;
   }

   public void dealtDamage(PixelmonWrapper user, PixelmonWrapper target, Attack attack, DamageTypeEnum damageType) {
      if (user != null && user.attack != null && user.attack.moveResult != null && user != target) {
         int restoration = this.getRestoration((float)user.attack.moveResult.damage, user, target);
         if (restoration > 0) {
            if (target.getBattleAbility() instanceof LiquidOoze) {
               user.bc.sendToAll("pixelmon.abilities.liquidooze", user.getNickname());
               user.doBattleDamage(target, (float)restoration, DamageTypeEnum.ABILITY);
            } else if (!user.hasFullHealth() && !user.hasStatus(StatusType.HealBlock)) {
               user.bc.sendToAll("pixelmon.effect.regainedenergy", user.getNickname());
               user.healEntityBy(restoration);
            }
         }

      }
   }

   private int getRestoration(float damage, PixelmonWrapper user, PixelmonWrapper target) {
      int restoration = 0;
      if (damage > 0.0F && user.isAlive()) {
         restoration = Math.max(1, (int)damage * this.drainPercent / 100);
         if (user.getUsableHeldItem() instanceof ItemBigRoot) {
            restoration = (int)((double)restoration * 1.3);
         }
      }

      return restoration;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            int restoration = this.getRestoration((float)userChoice.result.damage, pw, target);
            if (restoration > 0) {
               float restorePercent = pw.getHealthPercent((float)restoration);
               if (target.getBattleAbility() instanceof LiquidOoze) {
                  userChoice.weight -= restorePercent;
               } else if (!pw.hasStatus(StatusType.HealBlock)) {
                  float healthDeficit = (float)pw.getHealthDeficit();
                  if (healthDeficit < (float)restoration) {
                     restorePercent = pw.getHealthPercent(healthDeficit);
                  }

                  userChoice.raiseWeight(restorePercent);
               }
            }
         }

      }
   }
}
