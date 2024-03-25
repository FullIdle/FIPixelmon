package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class Taunt extends StatusBase {
   transient int turnsRemaining = 3;

   public Taunt() {
      super(StatusType.Taunt);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         TextComponentTranslation message = ChatHandler.getMessage("pixelmon.status.taunted", target.getNickname());
         target.addStatus(new Taunt(), user, message);
      }

   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (a.getAttackCategory() != AttackCategory.STATUS) {
         return true;
      } else if (a.isZ && (a.isAttack("Geomancy") || a.isAttack("Z-Geomancy")) && !user.hasStatus(StatusType.MultiTurn)) {
         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.tauntcantuse", user.getNickname(), a.getMove().getTranslatedName());
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.turnsRemaining <= 0) {
         pw.bc.sendToAll("pixelmon.status.tauntnolonger", pw.getNickname());
         pw.removeStatus((StatusBase)this);
      } else {
         Iterator var2 = pw.getMoveset().iterator();

         while(var2.hasNext()) {
            Attack attack = (Attack)var2.next();
            if (attack != null && attack.getAttackCategory() == AttackCategory.STATUS) {
               attack.setDisabled(true, pw);
            }
         }

      }
   }

   public void onEndOfTurn(PixelmonWrapper pw) {
      --this.turnsRemaining;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly() && !MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, bestUserChoices)) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            Iterator var9 = pw.getBattleAI().getMoveset(target).iterator();

            while(var9.hasNext()) {
               Attack attack = (Attack)var9.next();
               if (attack.getAttackCategory() == AttackCategory.STATUS) {
                  userChoice.raiseWeight(20.0F);
               }
            }
         }

      }
   }
}
