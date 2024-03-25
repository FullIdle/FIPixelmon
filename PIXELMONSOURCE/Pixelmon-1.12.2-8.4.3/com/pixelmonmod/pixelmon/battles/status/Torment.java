package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import java.util.ArrayList;
import net.minecraft.util.text.TextComponentTranslation;

public class Torment extends StatusBase {
   transient Attack lastAttack;

   public Torment() {
      super(StatusType.Torment);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         TextComponentTranslation message = ChatHandler.getMessage("pixelmon.status.torment", target.getNickname());
         target.addStatus(new Torment(), user, message);
      }

   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      this.lastAttack = attack;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      Attack[] var2 = pw.getMoveset().attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack attack = var2[var4];
         if (attack != null && attack.equals(this.lastAttack)) {
            attack.setDisabled(true, pw);
         }
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.getAffectedChoices(userChoice, bestOpponentChoices).size() <= 1) {
         userChoice.raiseWeight(20.0F);
      }

   }
}
