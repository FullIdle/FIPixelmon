package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class Yawn extends StatusBase {
   transient int effectTurns = 0;

   public Yawn() {
      super(StatusType.Yawn);
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return true;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Sleep)) {
         user.bc.sendToAll("pixelmon.effect.alreadysleeping", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else if (target.hasStatus(StatusType.Yawn)) {
         user.bc.sendToAll("pixelmon.effect.alreadydrowsy", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else if (!target.hasPrimaryStatus()) {
         if (this.isImmune(target)) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.attack.moveResult.result = AttackResult.failed;
         } else if (target.addStatus(new Yawn(), user)) {
            user.bc.sendToAll("pixelmon.effect.becamedrowsy", target.getNickname());
         } else {
            user.attack.moveResult.result = AttackResult.failed;
         }
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (++this.effectTurns >= 2) {
         if (pw.addStatus(new Sleep(), pw)) {
            pw.bc.sendToAll("pixelmon.status.fellasleep", pw.getNickname());
         }

         pw.removeStatus((StatusBase)this);
      }

   }

   public boolean isImmune(PixelmonWrapper pokemon) {
      if (pokemon.bc.rules.hasClause("sleep")) {
         PixelmonWrapper[] var2 = pokemon.getParticipant().allPokemon;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PixelmonWrapper pw = var2[var4];
            if (pw.getPrimaryStatus().type == StatusType.Sleep) {
               return true;
            }
         }
      }

      return false;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         userChoice.raiseWeight(40.0F);
      }

   }
}
