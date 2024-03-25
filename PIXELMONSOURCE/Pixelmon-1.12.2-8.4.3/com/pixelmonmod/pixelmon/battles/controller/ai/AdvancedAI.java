package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.Perish;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancedAI extends TacticalAI {
   public AdvancedAI(BattleParticipant participant) {
      super(participant);
   }

   public MoveChoice getNextMove(PixelmonWrapper pw) {
      ArrayList choices = this.getWeightedAttackChoices(pw);
      ArrayList bestChoices = this.getBestChoices(choices);
      MoveChoice bestChoice = this.pickBestChoice(choices, bestChoices);
      ArrayList switchChoices = this.getSwitchChoices(pw);
      ArrayList opponents = pw.getOpponentPokemon();
      if (!switchChoices.isEmpty()) {
         int controlledIndex = pw.getControlledIndex();
         int statBoostMod = pw.getBattleStats().getSumStages() * -20;
         boolean willFaintPerishSong = false;
         Perish perishSong = (Perish)pw.getStatus(StatusType.Perish);
         if (perishSong != null) {
            willFaintPerishSong = perishSong.effectTurns == 1;
         }

         boolean wasSimulateMode = this.bc.simulateMode;
         this.bc.simulateMode = true;

         try {
            ArrayList bestOpponentChoicesBefore = this.getNextOpponentMoves(pw, opponents);
            float opponentChoicesBeforeSum = MoveChoice.sumWeights(bestOpponentChoicesBefore);
            Iterator var14 = switchChoices.iterator();

            while(var14.hasNext()) {
               MoveChoice switchChoice = (MoveChoice)var14.next();
               pw.newPokemonUUID = switchChoice.switchPokemon;
               PixelmonWrapper nextPokemon = pw.doSwitch();
               int totalHazardDamage = 0;
               int beforeHealth = nextPokemon.getHealth();
               Iterator var19 = ((List)pw.getEntryHazards().stream().filter((hazardx) -> {
                  return !hazardx.isImmune(nextPokemon);
               }).collect(Collectors.toList())).iterator();

               float opponentChoiceWeight;
               while(var19.hasNext()) {
                  EntryHazard hazard = (EntryHazard)var19.next();
                  int damage = hazard.getDamage(nextPokemon);
                  if (damage > 0) {
                     totalHazardDamage += damage;
                     switchChoice.raiseWeight((float)(-damage));
                  } else {
                     opponentChoiceWeight = (float)(-hazard.getAIWeight() * hazard.getNumLayers());
                     if (hazard.type == StatusType.ToxicSpikes && pw.hasType(EnumType.Poison)) {
                        opponentChoiceWeight = -opponentChoiceWeight;
                     }

                     switchChoice.raiseWeight(opponentChoiceWeight);
                  }
               }

               if (totalHazardDamage >= beforeHealth) {
                  switchChoice.lowerTier(0);
                  nextPokemon.setHealth(beforeHealth);
                  this.resetSwitchSimulation(pw, controlledIndex, nextPokemon);
               } else {
                  nextPokemon.setHealth(beforeHealth - totalHazardDamage);

                  try {
                     MoveChoice choice = this.getNextMoveAttackOnly(nextPokemon);
                     CalcPriority.checkMoveSpeed(pw.bc);
                     ArrayList bestOpponentChoicesAfter = this.getNextOpponentMoves(pw, opponents);
                     if (choice != null && choice.weight > 0.0F && !MoveChoice.canOutspeedAnd2HKO(bestOpponentChoicesAfter, nextPokemon)) {
                        float userChoiceWeight = choice.weight - bestChoice.weight;
                        opponentChoiceWeight = opponentChoicesBeforeSum - MoveChoice.sumWeights(bestOpponentChoicesAfter);
                        Iterator var23 = opponents.iterator();

                        while(var23.hasNext()) {
                           PixelmonWrapper opponent = (PixelmonWrapper)var23.next();
                           if (opponent.lastAttack != null && opponent.lastAttack.getAttackCategory() != AttackCategory.STATUS && opponent.lastAttack.moveResult.target == pw && opponent.lastAttack.getTypeEffectiveness(opponent, nextPokemon) == (double)Effectiveness.None.value) {
                              userChoiceWeight += 30.0F;
                           }
                        }

                        userChoiceWeight += (float)statBoostMod;
                        switchChoice.raiseWeight(userChoiceWeight + opponentChoiceWeight);
                        if (willFaintPerishSong) {
                           switchChoice.raiseTier(3);
                        }
                     }
                  } finally {
                     nextPokemon.setHealth(beforeHealth);
                     this.resetSwitchSimulation(pw, controlledIndex, nextPokemon);
                  }
               }
            }
         } finally {
            this.bc.simulateMode = wasSimulateMode;
         }

         choices.addAll(switchChoices);
      }

      bestChoices = this.getBestChoices(choices);
      bestChoice = this.pickBestChoice(choices, bestChoices);
      return bestChoice;
   }

   protected MoveChoice getNextMoveAttackOnly(PixelmonWrapper pw) {
      return super.getNextMove(pw);
   }

   protected ArrayList getNextOpponentMoves(PixelmonWrapper pw, ArrayList opponents) {
      ArrayList bestOpponentChoices = new ArrayList();
      Iterator var4 = pw.getOpponentPokemon().iterator();

      while(var4.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var4.next();
         ArrayList singleOpponentChoices = pw.getBattleAI().getWeightedOffensiveChoices(opponent);
         MoveChoice bestChoice = this.pickBestChoice(this.getBestChoices(singleOpponentChoices));
         if (bestChoice != null) {
            bestOpponentChoices.add(bestChoice);
         }
      }

      return bestOpponentChoices;
   }

   protected boolean validateSwitch(PixelmonWrapper nextPokemon) {
      this.bc.modifyStats();
      this.bc.modifyStatsCancellable(nextPokemon);
      ArrayList saveTurnList = this.bc.turnList;
      CalcPriority.checkMoveSpeed(nextPokemon.bc);
      ArrayList opponentChoices = this.getNextOpponentMoves(nextPokemon, nextPokemon.getOpponentPokemon());
      boolean canSurvive = !MoveChoice.canOutspeedAndOHKO(opponentChoices, nextPokemon, this.getBestChoices(this.getWeightedAttackChoices(nextPokemon)));
      this.bc.turnList = saveTurnList;
      return canSurvive;
   }
}
