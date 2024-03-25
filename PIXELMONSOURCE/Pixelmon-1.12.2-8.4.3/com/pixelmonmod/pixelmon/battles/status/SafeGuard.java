package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Infiltrator;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class SafeGuard extends StatusBase {
   transient int effectTurns = 5;

   public SafeGuard() {
      super(StatusType.SafeGuard);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (user.hasStatus(this.type)) {
            if (user == target) {
               user.bc.sendToAll("pixelmon.effect.alreadysafeguard", user.getNickname());
            }

            user.attack.moveResult.result = AttackResult.failed;
            return;
         }

         if (user.addTeamStatus(new SafeGuard(), user)) {
            user.bc.sendToAll("pixelmon.effect.guarded", user.getNickname());
         }
      }

   }

   public boolean stopsStatusChange(StatusType t, PixelmonWrapper target, PixelmonWrapper user) {
      if ((t.isPrimaryStatus() || t.isStatus(StatusType.Confusion, StatusType.Yawn)) && !(user.getBattleAbility() instanceof Infiltrator)) {
         if (user != target && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            target.bc.sendToAll("pixelmon.status.safeguard", target.getNickname());
         }

         return true;
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.effectTurns <= 0) {
         pw.bc.sendToAll("pixelmon.status.safeguardoff", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public StatusBase copy() {
      SafeGuard copy = new SafeGuard();
      copy.effectTurns = this.effectTurns;
      return copy;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = pw.getOpponentPokemon().iterator();

      PixelmonWrapper opponent;
      do {
         if (!var7.hasNext()) {
            userChoice.raiseWeight(15.0F);
            return;
         }

         opponent = (PixelmonWrapper)var7.next();
      } while(!(opponent.getBattleAbility() instanceof Infiltrator));

   }
}
