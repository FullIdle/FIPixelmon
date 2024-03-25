package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class Disable extends StatusBase {
   transient Attack disabledMove;
   transient int effectiveTurns = 4;

   public Disable() {
      super(StatusType.Disable);
   }

   public Disable(Attack attack) {
      super(StatusType.Disable);
      this.disabledMove = attack;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Attack lastAttack = target.lastAttack;
      if (target.isDynamax() || lastAttack != null && lastAttack.isAttack("Struggle")) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else if (target.hasStatus(StatusType.Disable)) {
         target.bc.sendToAll("pixelmon.effect.alreadydisabled", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         if (lastAttack != null) {
            TextComponentTranslation message = ChatHandler.getMessage("pixelmon.status.disable", target.getNickname(), lastAttack.getMove().getTranslatedName());
            target.addStatus(new Disable(lastAttack), user, message);
         } else {
            target.bc.sendToAll("pixelmon.effect.effectfailed");
         }

      }
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (a.equals(this.disabledMove)) {
         user.bc.sendToAll("pixelmon.status.disabled", user.getNickname(), a.getMove().getTranslatedName());
         return false;
      } else {
         return true;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.effectiveTurns <= 0) {
         pw.bc.sendToAll("pixelmon.status.nolongerdisabled", pw.getNickname(), this.disabledMove.getMove().getTranslatedName());
         pw.removeStatus((StatusBase)this);
      } else {
         pw.getMoveset().stream().filter((attack) -> {
            return attack.equals(this.disabledMove);
         }).forEach((attack) -> {
            attack.setDisabled(true, pw);
         });
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(true) {
         String attackName;
         do {
            PixelmonWrapper target;
            do {
               if (!var7.hasNext()) {
                  return;
               }

               target = (PixelmonWrapper)var7.next();
            } while(target.isDynamax != 0);

            attackName = target.lastAttack == null ? "" : target.lastAttack.getMove().getLocalizedName();
         } while(!MoveChoice.canOutspeed(bestOpponentChoices, pw, bestUserChoices) && !MoveChoice.hasSuccessfulAttackChoice(bestOpponentChoices, attackName));

         userChoice.raiseWeight(40.0F);
      }
   }
}
