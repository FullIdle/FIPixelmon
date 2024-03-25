package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AggressiveAI extends BattleAIBase {
   public AggressiveAI(BattleParticipant participant) {
      super(participant);
   }

   public MoveChoice getNextMove(PixelmonWrapper pw) {
      ArrayList choices = this.getAttackChoicesOpponentOnly(pw);
      ArrayList allies = pw.getTeamPokemonExcludeSelf();
      ArrayList bestChoices = new ArrayList();
      boolean wasSimulateMode = this.bc.simulateMode;
      this.bc.simulateMode = true;

      try {
         this.bc.modifyStats();
         this.bc.modifyStatsCancellable(pw);
         Iterator var6 = choices.iterator();

         while(var6.hasNext()) {
            MoveChoice choice = (MoveChoice)var6.next();
            if (choice.attack.getAttackCategory() != AttackCategory.STATUS) {
               this.weightOffensiveMove(pw, choice, allies);
               if (choice.attack.isAttack("Explosion", "Final Gambit", "Selfdestruct")) {
                  choice.weight = 0.0F;
               }

               MoveChoice.checkBestChoice(choice, bestChoices);
            }
         }
      } finally {
         this.bc.simulateMode = wasSimulateMode;
      }

      return bestChoices.isEmpty() ? (MoveChoice)RandomHelper.getRandomElementFromList(choices) : (MoveChoice)RandomHelper.getRandomElementFromList(bestChoices);
   }

   protected MoveChoice getNextMoveAttackOnly(PixelmonWrapper pw) {
      return this.getNextMove(pw);
   }

   public UUID getNextSwitch(PixelmonWrapper pw) {
      ArrayList bestSwitches = new ArrayList();
      int controlledIndex = pw.getControlledIndex();
      List party = this.getPossibleSwitchIDs();
      if (party.isEmpty()) {
         return null;
      } else if (party.size() == 1) {
         return (UUID)party.get(0);
      } else {
         boolean wasSimulateMode = true;

         PixelmonWrapper opponent;
         for(Iterator var6 = pw.getOpponentPokemon().iterator(); var6.hasNext(); wasSimulateMode = wasSimulateMode && opponent.isFainted()) {
            opponent = (PixelmonWrapper)var6.next();
         }

         if (!wasSimulateMode && controlledIndex != -1) {
            wasSimulateMode = this.bc.simulateMode;
            this.bc.simulateMode = true;

            try {
               MoveChoice bestChoice = null;
               Iterator var21 = party.iterator();

               while(var21.hasNext()) {
                  UUID partyUUID = (UUID)var21.next();
                  this.bc.simulateMode = true;
                  pw.newPokemonUUID = partyUUID;
                  PixelmonWrapper nextPokemon = pw.doSwitch();

                  try {
                     if (this.validateSwitch(nextPokemon)) {
                        MoveChoice choice = this.getNextMoveAttackOnly(nextPokemon);
                        if (choice.compareTo(bestChoice) > 0) {
                           bestSwitches.clear();
                        }

                        if (choice.compareTo(bestChoice) >= 0) {
                           bestChoice = choice;
                           bestSwitches.add(partyUUID);
                        }
                     }
                  } finally {
                     this.resetSwitchSimulation(pw, controlledIndex, nextPokemon);
                  }
               }
            } finally {
               this.bc.simulateMode = wasSimulateMode;
            }

            UUID bestSwitch = (UUID)RandomHelper.getRandomElementFromList((List)(bestSwitches.isEmpty() ? party : bestSwitches));
            return bestSwitch;
         } else {
            return (UUID)RandomHelper.getRandomElementFromList(party);
         }
      }
   }

   protected boolean validateSwitch(PixelmonWrapper nextPokemon) {
      return true;
   }

   protected void resetSwitchSimulation(PixelmonWrapper current, int controlledIndex, PixelmonWrapper simulated) {
      this.participant.controlledPokemon.set(controlledIndex, current);
   }
}
