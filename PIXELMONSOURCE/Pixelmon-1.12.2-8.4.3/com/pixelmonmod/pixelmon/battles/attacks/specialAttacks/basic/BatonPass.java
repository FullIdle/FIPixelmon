package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.status.Autotomize;
import com.pixelmonmod.pixelmon.battles.status.DefenseCurl;
import com.pixelmonmod.pixelmon.battles.status.GuardSplit;
import com.pixelmonmod.pixelmon.battles.status.Infatuated;
import com.pixelmonmod.pixelmon.battles.status.PowerSplit;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Substitute;
import com.pixelmonmod.pixelmon.battles.status.Transformed;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import java.util.ArrayList;

public class BatonPass extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      BattleParticipant userParticipant = user.getParticipant();
      if (!userParticipant.hasMorePokemonReserve()) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else if (user.bc.simulateMode) {
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("batonpass.pass", user.getNickname());
         user.nextSwitchIsMove = true;
         if (userParticipant instanceof TrainerParticipant) {
            user.bc.switchPokemon(user.getPokemonUUID(), user.getBattleAI().getNextSwitch(user), true);
         } else if (userParticipant instanceof PlayerParticipant) {
            if (!user.bc.simulateMode) {
               user.wait = true;
               Pixelmon.network.sendTo(new EnforcedSwitchTask(user.bc.getPositionOfPokemon(user, user.getParticipant())), user.getPlayerOwner());
            }
         } else {
            user.nextSwitchIsMove = false;
         }

         return AttackResult.succeeded;
      }
   }

   public static boolean isBatonPassable(StatusBase s) {
      return !(s instanceof StatusPersist) && !(s instanceof Autotomize) && !(s instanceof DefenseCurl) && !(s instanceof Infatuated) && !(s instanceof Transformed) && !(s instanceof GuardSplit) && !(s instanceof PowerSplit);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!pw.hasStatus(StatusType.Perish)) {
         int stageMods = pw.getBattleStats().getSumStages();
         if (stageMods >= 0) {
            float weightIncrease = 0.0F;
            Substitute substitute = (Substitute)((Substitute)pw.getStatus(StatusType.Substitute));
            if (substitute != null) {
               weightIncrease += pw.getHealthPercent((float)substitute.health);
            }

            weightIncrease += (float)(stageMods * 20);
            if (weightIncrease > 0.0F && MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, userChoice.createList())) {
               weightIncrease += 30.0F;
            }

            userChoice.raiseWeightLimited(weightIncrease);
         }

      }
   }
}
