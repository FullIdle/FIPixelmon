package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import java.util.ArrayList;
import java.util.Iterator;

public class Nightmare extends StatusBase {
   public Nightmare() {
      super(StatusType.Nightmare);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Sleep) && target.addStatus(new Nightmare(), user)) {
         target.bc.sendToAll("pixelmon.status.nightmareadd", target.getNickname());
      } else {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (pw.hasStatus(StatusType.Sleep)) {
         if (!(pw.getBattleAbility() instanceof MagicGuard)) {
            pw.bc.sendToAll("pixelmon.status.nightmare", pw.getNickname());
            pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(25.0F), DamageTypeEnum.STATUS);
         }
      } else {
         pw.removeStatus((StatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (!(target.getBattleAbility() instanceof MagicGuard)) {
               userChoice.raiseWeight(25.0F);
            }
         }

      }
   }
}
