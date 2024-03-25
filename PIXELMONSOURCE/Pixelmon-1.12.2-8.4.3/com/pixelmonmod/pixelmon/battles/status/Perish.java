package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Soundproof;
import java.util.ArrayList;
import java.util.Iterator;

public class Perish extends StatusBase {
   public transient int effectTurns = 4;

   public Perish() {
      super(StatusType.Perish);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(this.type)) {
         user.bc.sendToAll("applyperish.already", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else if (target.addStatus(new Perish(), target)) {
         user.bc.sendToAll("applyperish.heard", target.getNickname());
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      --this.effectTurns;
      pw.bc.sendToAll("pixelmon.status.perishsongstruck", pw.getNickname(), this.effectTurns);
      if (this.effectTurns <= 0) {
         pw.doBattleDamage(pw, (float)pw.getHealth(), DamageTypeEnum.STATUS);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.getParticipant().countAblePokemon() >= pw.bc.rules.battleType.numPokemon * 2) {
         int userStages = 0;
         int opponentStages = 0;
         Iterator var9 = pw.getTeamPokemon().iterator();

         PixelmonWrapper opponent;
         while(var9.hasNext()) {
            opponent = (PixelmonWrapper)var9.next();
            if (!BattleParticipant.canSwitch(opponent)[0]) {
               userChoice.raiseWeight(-100.0F);
            }

            if (!(opponent.getBattleAbility() instanceof Soundproof)) {
               userStages += opponent.getBattleStats().getSumStages();
            }
         }

         for(var9 = pw.getOpponentPokemon().iterator(); var9.hasNext(); opponentStages += opponent.getBattleStats().getSumStages()) {
            opponent = (PixelmonWrapper)var9.next();
            if (opponent.getBattleAbility() instanceof Soundproof) {
               return;
            }

            if (!BattleParticipant.canSwitch(opponent)[0]) {
               userChoice.raiseWeight(100.0F);
            }
         }

         if (opponentStages > 1) {
            userChoice.raiseWeight((float)((opponentStages - userStages) * 20));
         }

         var9 = pw.getParticipant().getOpponents().iterator();

         while(var9.hasNext()) {
            BattleParticipant opponent = (BattleParticipant)var9.next();
            if (opponent.countAblePokemon() <= pw.bc.rules.battleType.numPokemon) {
               userChoice.raiseWeight(100.0F);
            }
         }

      }
   }
}
