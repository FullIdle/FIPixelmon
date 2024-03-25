package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Infiltrator;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Steadfast;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class Flinch extends StatusBase {
   public static boolean targetHadSubstitute;

   public Flinch() {
      super(StatusType.Flinch);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.isDynamax()) {
         if (this.checkChance()) {
            flinch(user, target);
         }

      }
   }

   public static void flinch(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasStatus(StatusType.Flinch) && target.bc != null && target.bc.battleLog.getActionForPokemon(target.bc.battleTurn, target) == null && target.attack != null && (!target.hasStatus(StatusType.Substitute) || user.getBattleAbility() instanceof Infiltrator) && !targetHadSubstitute) {
         target.addStatus(new Flinch(), user);
      }

   }

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.Substitute)) {
         targetHadSubstitute = true;
      } else {
         targetHadSubstitute = false;
      }

      return AttackResult.proceed;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      user.bc.sendToAll("battlecontroller.flinched", user.getNickname());
      AbilityBase ability = user.getBattleAbility();
      if (ability instanceof Steadfast) {
         ability.sendActivatedMessage(user);
         user.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed);
      }

      return false;
   }

   public void onEndOfTurn(PixelmonWrapper user) {
      user.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      float chance = this.getChance();
      if (!userChoice.hitsAlly()) {
         Iterator var8 = userChoice.targets.iterator();

         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            if (!MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList()) && target.addStatus(new Flinch(), pw)) {
               if (chance == 100.0F) {
                  userChoice.raiseWeight(chance);
               } else if (chance >= 50.0F) {
                  userChoice.raiseWeight(chance / 2.0F);
               } else {
                  userChoice.raiseWeight(chance / 100.0F);
               }
            }
         }

      }
   }
}
