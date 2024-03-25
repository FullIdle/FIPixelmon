package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;
import java.util.Iterator;

public class Imprison extends StatusBase {
   transient PixelmonWrapper user;

   public Imprison() {
      super(StatusType.Imprison);
   }

   public Imprison(PixelmonWrapper user) {
      super(StatusType.Imprison);
      this.user = user;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Imprison)) {
         if (user.targetIndex == 0 || user.bc.simulateMode) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.attack.moveResult.result = AttackResult.failed;
         }
      } else {
         user.bc.sendToAll("pixelmon.status.imprison", user.getNickname());
         target.addTeamStatus(new Imprison(user), user);
      }

   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (this.user.getMoveset().hasAttack(a)) {
         user.bc.sendToAll("pixelmon.status.disabled", user.getNickname(), a.getMove().getTranslatedName());
         return false;
      } else {
         return true;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.user.isAlive() && pw.bc.getActivePokemon().contains(this.user)) {
         pw.getMoveset().stream().filter((attackx) -> {
            return this.user.getMoveset().hasAttack(attackx);
         }).forEach((attackx) -> {
            attackx.setDisabled(true, pw);
         });
      } else {
         Iterator var2 = pw.getMoveset().iterator();

         while(var2.hasNext()) {
            Attack attack = (Attack)var2.next();
            attack.setDisabled(false, pw);
         }

         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Moveset moveset = pw.getMoveset();
      Iterator var8 = pw.getOpponentPokemon().iterator();

      while(var8.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var8.next();
         Iterator var10 = pw.getBattleAI().getMoveset(target).iterator();

         while(var10.hasNext()) {
            Attack attack = (Attack)var10.next();
            if (moveset.hasAttack(attack)) {
               userChoice.raiseWeight(25.0F);
            }
         }
      }

   }
}
