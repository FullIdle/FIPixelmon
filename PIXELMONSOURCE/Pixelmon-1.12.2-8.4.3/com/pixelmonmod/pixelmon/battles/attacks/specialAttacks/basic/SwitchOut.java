package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import java.util.ArrayList;

public class SwitchOut extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!userWrapper.bc.simulateMode && (!userWrapper.inParentalBond || !targetWrapper.isAlive())) {
         ArrayList activePokemon = userWrapper.bc.getActivePokemon();
         if (activePokemon.contains(userWrapper) && activePokemon.contains(targetWrapper)) {
            BattleParticipant userParticipant = userWrapper.getParticipant();
            if (userParticipant.hasMorePokemonReserve() && (!targetWrapper.isFainted() || targetWrapper.getParticipant().countAblePokemon() != 0)) {
               userWrapper.nextSwitchIsMove = true;
               if (userParticipant instanceof TrainerParticipant) {
                  userWrapper.bc.switchPokemon(userWrapper.getPokemonUUID(), userWrapper.getBattleAI().getNextSwitch(userWrapper), true);
               } else if (userParticipant instanceof PlayerParticipant) {
                  if (userParticipant.hasMorePokemonReserve()) {
                     boolean pursuitAttacker = false;
                     int i = userWrapper.bc.turnList.size() - 1;

                     for(int j = i; i >= 0; --i) {
                        PixelmonWrapper queuedPokemon = (PixelmonWrapper)userWrapper.bc.turnList.get(j);
                        int queuedIndex = userWrapper.bc.turnList.indexOf(queuedPokemon);
                        int userIndex = userWrapper.bc.turnList.indexOf(userWrapper);
                        if (userIndex == i) {
                           break;
                        }

                        if (queuedPokemon.attack.isAttack("Pursuit") && queuedPokemon.targets.contains(userWrapper) && userIndex < queuedIndex) {
                           queuedPokemon.bc.turnList.remove(queuedPokemon);
                           queuedPokemon.bc.turnList.add(userWrapper.bc.turn + 1, queuedPokemon);
                           pursuitAttacker = true;
                           ++j;
                        }

                        --j;
                     }

                     if (!pursuitAttacker) {
                        userWrapper.wait = true;
                        Pixelmon.network.sendTo(new EnforcedSwitchTask(userWrapper.bc.getPositionOfPokemon(userWrapper, userWrapper.getParticipant())), userWrapper.getPlayerOwner());
                     }
                  } else {
                     userWrapper.nextSwitchIsMove = false;
                  }
               } else {
                  userWrapper.nextSwitchIsMove = false;
               }

            }
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, userChoice.createList())) {
         userChoice.raiseWeight(30.0F);
      }

   }
}
