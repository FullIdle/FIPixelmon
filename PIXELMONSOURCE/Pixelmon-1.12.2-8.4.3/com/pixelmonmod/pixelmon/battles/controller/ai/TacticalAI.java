package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class TacticalAI extends AggressiveAI {
   ArrayList memory = new ArrayList(6);

   public TacticalAI(BattleParticipant participant) {
      super(participant);
   }

   public MoveChoice getNextMove(PixelmonWrapper pw) {
      ArrayList choices = this.getWeightedAttackChoices(pw);
      ArrayList bestChoices = this.getBestChoices(choices);
      return this.pickBestChoice(choices, bestChoices);
   }

   protected ArrayList getWeightedAttackChoices(PixelmonWrapper pw) {
      ArrayList choices = this.getAttackChoicesOpponentOnly(pw);
      ArrayList allies = pw.getTeamPokemonExcludeSelf();
      ArrayList saveTurnList = this.bc.turnList;
      boolean wasSimulateMode = this.bc.simulateMode;
      this.bc.simulateMode = true;

      try {
         this.bc.modifyStats();
         this.bc.modifyStatsCancellable(pw);
         Iterator var7 = choices.iterator();

         while(var7.hasNext()) {
            MoveChoice choice = (MoveChoice)var7.next();
            if (choice.attack.getAttackCategory() == AttackCategory.STATUS) {
               this.simulateStatusMove(pw, choice);
            } else {
               this.weightOffensiveMove(pw, choice, allies);
            }
         }

         ArrayList opponentChoices = new ArrayList();
         ArrayList bestOpponentChoices = new ArrayList();
         Iterator var9 = pw.getOpponentPokemon().iterator();

         while(var9.hasNext()) {
            PixelmonWrapper opponent = (PixelmonWrapper)var9.next();
            ArrayList singleOpponentChoices = pw.getBattleAI().getWeightedOffensiveChoices(opponent);
            opponentChoices.addAll(singleOpponentChoices);
            bestOpponentChoices.addAll(this.getBestChoices(singleOpponentChoices));
         }

         MoveChoice choice;
         for(int i = 0; i < opponentChoices.size(); ++i) {
            choice = (MoveChoice)opponentChoices.get(i);
            if (choice.tier == 0) {
               opponentChoices.remove(i--);
            }
         }

         saveTurnList = this.bc.turnList;
         CalcPriority.checkMoveSpeed(pw.bc);
         ArrayList bestChoices = this.getBestChoices(choices, true);
         var9 = choices.iterator();

         while(var9.hasNext()) {
            choice = (MoveChoice)var9.next();
            if (choice.isAttack() && (!choice.isOffensiveMove() || choice.tier != 0 || choice.result.result == AttackResult.hit) && (choice.result == null || choice.result.result != AttackResult.failed)) {
               pw.setAttack(choice.attack, choice.targets, false);
               Iterator var20 = choice.attack.getMove().effects.iterator();

               while(var20.hasNext()) {
                  EffectBase effect = (EffectBase)var20.next();
                  effect.weightEffect(pw, choice, choices, bestChoices, opponentChoices, bestOpponentChoices);
               }

               if (MoveChoice.canOutspeedAndOHKO(bestOpponentChoices, pw, choice.createList())) {
                  if (choice.tier == 2) {
                     choice.lowerTier(1);
                  } else if (choice.tier >= 3) {
                     choice.weight = 0.0F;
                  }
               } else if (choice.tier >= 3) {
                  float priority = (float)choice.attack.getMove().getPriority(choice.user);
                  if (priority > 0.0F) {
                     ++choice.tier;
                  } else if (priority < 0.0F) {
                     choice.weight = 1.0F;
                  }
               }
            }
         }

         return choices;
      } finally {
         this.bc.simulateMode = wasSimulateMode;
         this.bc.turnList = saveTurnList;
      }
   }

   protected MoveChoice pickBestChoice(ArrayList bestChoices) {
      return this.pickBestChoice((ArrayList)null, bestChoices);
   }

   protected MoveChoice pickBestChoice(ArrayList choices, ArrayList bestChoices) {
      if (bestChoices.isEmpty()) {
         return choices == null ? null : (MoveChoice)RandomHelper.getRandomElementFromList(choices);
      } else if (bestChoices.size() > 1 && ((MoveChoice)bestChoices.get(0)).isMiddleTier()) {
         float totalWeight = 0.0F;

         MoveChoice choice;
         for(Iterator var4 = bestChoices.iterator(); var4.hasNext(); totalWeight += choice.weight) {
            choice = (MoveChoice)var4.next();
         }

         float random = RandomHelper.getRandomNumberBetween(0.0F, totalWeight);
         float counter = 0.0F;
         Iterator var6 = bestChoices.iterator();

         MoveChoice choice;
         do {
            if (!var6.hasNext()) {
               return (MoveChoice)bestChoices.get(bestChoices.size() - 1);
            }

            choice = (MoveChoice)var6.next();
            counter += choice.weight;
         } while(!(counter >= random));

         return choice;
      } else {
         return (MoveChoice)RandomHelper.getRandomElementFromList(bestChoices);
      }
   }

   public void registerMove(PixelmonWrapper user) {
      OpponentMemory pokemon = this.getMemory(user);
      if (pokemon != null && user.attack != null) {
         pokemon.seeAttack(user.attack);
      }

   }

   public void simulateStatusMove(PixelmonWrapper pw, MoveChoice choice) {
      pw.setAttack(choice.attack, choice.targets, false);

      for(int j = 0; j < choice.targets.size(); ++j) {
         PixelmonWrapper target = (PixelmonWrapper)choice.targets.get(j);
         MoveResults result = new MoveResults(target);
         result.priority = CalcPriority.calculatePriority(pw);
         choice.result = result;
         choice.attack.saveAttack();
         choice.attack.use(pw, target, result);
         choice.attack.restoreAttack();
      }

   }

   protected OpponentMemory getMemory(PixelmonWrapper pw) {
      if (!this.participant.getOpponentPokemon().contains(pw)) {
         return null;
      } else {
         UUID userUUID = pw.getPokemonUUID();
         OpponentMemory pokemon = null;
         Iterator var4 = this.memory.iterator();

         while(var4.hasNext()) {
            OpponentMemory p = (OpponentMemory)var4.next();
            if (p.pokemonUUID.equals(userUUID)) {
               pokemon = p;
               break;
            }
         }

         if (pokemon == null) {
            pokemon = new OpponentMemory(pw);
            this.memory.add(pokemon);
         }

         return pokemon;
      }
   }

   protected ArrayList getBestChoices(ArrayList choices) {
      return this.getBestChoices(choices, false);
   }

   protected ArrayList getBestChoices(ArrayList choices, boolean excludeStatus) {
      ArrayList bestChoices = new ArrayList(choices.size());
      if (excludeStatus) {
         Iterator var4 = choices.iterator();

         while(var4.hasNext()) {
            MoveChoice choice = (MoveChoice)var4.next();
            MoveChoice.checkBestChoice(choice, bestChoices, excludeStatus);
         }

         return bestChoices;
      } else {
         MoveChoice bestChoice = null;
         ArrayList bestOffensiveChoices = new ArrayList(choices.size());
         ArrayList bestSwitchChoices = new ArrayList(choices.size());
         ArrayList statusChoices = new ArrayList(choices.size());
         Iterator var8 = choices.iterator();

         while(true) {
            MoveChoice choice;
            do {
               if (!var8.hasNext()) {
                  if (bestChoice != null) {
                     bestChoices.add(bestChoice);
                     this.addSubchoices(bestChoice, bestChoices, bestOffensiveChoices);
                     this.addSubchoices(bestChoice, bestChoices, bestSwitchChoices);
                     this.addSubchoices(bestChoice, bestChoices, statusChoices);
                  }

                  return bestChoices;
               }

               choice = (MoveChoice)var8.next();
            } while(choice.tier <= 0);

            if (bestChoice == null || bestChoice.compareTo(choice) < 0) {
               bestChoice = choice;
            }

            if (choice.isOffensiveMove()) {
               this.compareToBestChoices(choice, bestOffensiveChoices);
            }

            if (!choice.isAttack()) {
               this.compareToBestChoices(choice, bestSwitchChoices);
            }

            if (choice.isStatusMove()) {
               statusChoices.add(choice);
            }
         }
      }
   }

   private void compareToBestChoices(MoveChoice choice, ArrayList bestChoices) {
      float currentComparison = 0.0F;
      if (!bestChoices.isEmpty()) {
         currentComparison = (float)((MoveChoice)bestChoices.get(0)).compareTo(choice);
      }

      if (currentComparison <= 0.0F) {
         if (currentComparison < 0.0F) {
            bestChoices.clear();
         }

         bestChoices.add(choice);
      }

   }

   private void addSubchoices(MoveChoice bestChoice, ArrayList bestChoices, ArrayList subchoices) {
      Iterator var4 = subchoices.iterator();

      while(var4.hasNext()) {
         MoveChoice subchoice = (MoveChoice)var4.next();
         if (subchoice != bestChoice && subchoice.isSimilarWeight(bestChoice)) {
            bestChoices.add(subchoice);
         }
      }

   }
}
