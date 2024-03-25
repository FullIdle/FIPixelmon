package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class BattleAIBase {
   BattleParticipant participant;
   BattleControllerBase bc;

   public BattleAIBase(BattleParticipant participant) {
      this.participant = participant;
      this.bc = participant.bc;
   }

   public abstract MoveChoice getNextMove(PixelmonWrapper var1);

   public abstract UUID getNextSwitch(PixelmonWrapper var1);

   public void registerMove(PixelmonWrapper user) {
   }

   public void registerSwitch(PixelmonWrapper switchOut, PixelmonWrapper switchIn) {
   }

   protected ArrayList getChoices(PixelmonWrapper pw) {
      ArrayList choices = new ArrayList();
      if (pw != null) {
         choices.addAll(this.getAttackChoices(pw));
      }

      choices.addAll(this.getSwitchChoices(pw));
      return choices;
   }

   protected ArrayList getAttackChoices(PixelmonWrapper pw) {
      ArrayList choices = new ArrayList();
      List moveset = this.getMoveset(pw);
      Iterator var4 = moveset.iterator();

      Attack struggle;
      while(var4.hasNext()) {
         struggle = (Attack)var4.next();
         if (struggle != null && struggle.pp > 0 && !struggle.getDisabled()) {
            struggle.createMoveChoices(pw, choices, true);
         }
      }

      if (choices.isEmpty()) {
         ArrayList targets = new ArrayList();
         targets.addAll(this.bc.getAdjacentPokemon(pw));
         targets.addAll(this.bc.getOpponentPokemon(this.participant));
         struggle = new Attack("Struggle");
         struggle.createMoveChoices(pw, choices, false);
      }

      return choices;
   }

   protected ArrayList getAttackChoicesOpponentOnly(PixelmonWrapper pw) {
      ArrayList choices = this.getAttackChoices(pw);
      ArrayList allies = this.bc.getTeamPokemonExcludeSelf(pw);
      if (!allies.isEmpty()) {
         for(int i = 0; i < choices.size(); ++i) {
            MoveChoice choice = (MoveChoice)choices.get(i);
            if (choice.targets.size() == 1 && allies.contains(choice.targets.get(0))) {
               choices.remove(i--);
            }
         }
      }

      return choices;
   }

   public ArrayList getSwitchChoices(PixelmonWrapper pw) {
      ArrayList switchChoices = new ArrayList();
      if (pw == null || BattleParticipant.canSwitch(pw)[0]) {
         List switchUUIDs = this.getPossibleSwitchIDs();
         Iterator var4 = switchUUIDs.iterator();

         while(var4.hasNext()) {
            UUID switchUUID = (UUID)var4.next();
            switchChoices.add(new MoveChoice(pw, switchUUID));
         }
      }

      return switchChoices;
   }

   protected List getPossibleSwitchIDs() {
      List party = new ArrayList();
      PixelmonWrapper[] var2 = this.participant.allPokemon;
      int var3 = var2.length;

      int i;
      for(i = 0; i < var3; ++i) {
         PixelmonWrapper pw = var2[i];
         if (pw.isAlive() && pw.entity == null) {
            party.add(pw.getPokemonUUID());
         }
      }

      Iterator var6 = this.participant.switchingIn.iterator();

      while(true) {
         while(var6.hasNext()) {
            UUID switchingInUUID = (UUID)var6.next();

            for(i = 0; i < party.size(); ++i) {
               if (switchingInUUID.equals(party.get(i))) {
                  party.remove(i);
                  break;
               }
            }
         }

         return party;
      }
   }

   protected MoveChoice getRandomAttackChoice(PixelmonWrapper pw) {
      return (MoveChoice)RandomHelper.getRandomElementFromList(this.getAttackChoicesOpponentOnly(pw));
   }

   protected void weightOffensiveMove(PixelmonWrapper pw, MoveChoice choice) {
      this.weightOffensiveMove(pw, choice, pw.getTeamPokemonExcludeSelf());
   }

   protected void weightOffensiveMove(PixelmonWrapper pw, MoveChoice choice, ArrayList allies) {
      Attack saveAttack = pw.attack;
      List saveTargets = pw.targets;
      pw.setAttack(choice.attack, choice.targets, false);

      for(int j = 0; j < choice.targets.size(); ++j) {
         PixelmonWrapper target = (PixelmonWrapper)choice.targets.get(j);
         MoveResults result = new MoveResults(target);
         result.priority = CalcPriority.calculatePriority(pw);
         choice.result = result;
         choice.attack.saveAttack();
         choice.attack.use(pw, target, result);
         if (result.result == AttackResult.charging) {
            choice.attack.use(pw, target, result);
         }

         if (!choice.attack.isMax) {
            choice.attack.restoreAttack();
         }

         if (allies.contains(target)) {
            if (result.damage >= target.getHealth()) {
               --choice.tier;
            }

            choice.weight -= target.getHealthPercent((float)result.damage);
            if (choice.result.weightMod < 0.0F) {
               choice.raiseWeight(-choice.result.weightMod);
               choice.result.result = AttackResult.hit;
            }
         } else {
            choice.raiseWeight(choice.result.weightMod);
            if (result.damage >= target.getHealth()) {
               choice.tier = 3;
               int effectiveAccuracy = result.accuracy;
               if (effectiveAccuracy < 0) {
                  effectiveAccuracy = 100;
               }

               choice.weight += (float)effectiveAccuracy;
            } else if (result.damage > 0) {
               choice.raiseTier(2);
               choice.weight += target.getHealthPercent((float)result.damage);
            }
         }
      }

      pw.attack = saveAttack;
      pw.targets = saveTargets;
   }

   protected void weightOffensiveMoves(PixelmonWrapper pw, ArrayList choices) {
      Iterator var3 = choices.iterator();

      while(var3.hasNext()) {
         MoveChoice choice = (MoveChoice)var3.next();
         if (choice.isOffensiveMove()) {
            this.weightOffensiveMove(pw, choice);
         }
      }

   }

   protected ArrayList getWeightedOffensiveChoices(PixelmonWrapper pw) {
      ArrayList choices = this.getAttackChoicesOpponentOnly(pw);
      ArrayList allies = this.bc.getTeamPokemonExcludeSelf(pw);
      Iterator var4 = choices.iterator();

      while(var4.hasNext()) {
         MoveChoice choice = (MoveChoice)var4.next();
         if (choice.attack.getAttackCategory() != AttackCategory.STATUS) {
            this.weightOffensiveMove(pw, choice, allies);
         }
      }

      return choices;
   }

   public ArrayList getBestAttackChoices(PixelmonWrapper pw) {
      ArrayList choices = this.getAttackChoicesOpponentOnly(pw);
      ArrayList allies = pw.getTeamPokemonExcludeSelf();
      ArrayList bestChoices = new ArrayList();
      Iterator var5 = choices.iterator();

      while(var5.hasNext()) {
         MoveChoice choice = (MoveChoice)var5.next();
         if (choice.attack.getAttackCategory() != AttackCategory.STATUS) {
            this.weightOffensiveMove(pw, choice, allies);
            MoveChoice.checkBestChoice(choice, bestChoices);
         }
      }

      return bestChoices;
   }

   public ArrayList getBestAttackChoices(ArrayList pokemonList) {
      ArrayList choices = new ArrayList(pokemonList.size());
      Iterator var3 = pokemonList.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         ArrayList after = this.getBestAttackChoices(pw);
         choices.add(after);
      }

      return choices;
   }

   public List getMoveset(PixelmonWrapper pw) {
      if (this.participant.getOpponentPokemon().contains(pw) && this instanceof TacticalAI) {
         OpponentMemory memory = ((TacticalAI)this).getMemory(pw);
         return memory.getAttacks();
      } else {
         ArrayList moveset = new ArrayList(4);
         Iterator var3 = pw.getMoveset().iterator();

         while(var3.hasNext()) {
            Attack attack = (Attack)var3.next();
            if (attack != null) {
               moveset.add(attack);
            }
         }

         return moveset;
      }
   }

   public void weightRandomMove(PixelmonWrapper pw, MoveChoice userChoice, ArrayList possibleChoices) {
      if (!possibleChoices.isEmpty()) {
         float totalWeight = 0.0F;
         boolean hasKO = false;
         this.weightOffensiveMoves(pw, possibleChoices);

         MoveChoice possibleChoice;
         for(Iterator var6 = possibleChoices.iterator(); var6.hasNext(); totalWeight += possibleChoice.weight) {
            possibleChoice = (MoveChoice)var6.next();
            if (possibleChoice.tier >= 3) {
               hasKO = true;
            }
         }

         userChoice.raiseWeight(totalWeight / (float)possibleChoices.size());
         if (userChoice.weight >= 50.0F && hasKO) {
            userChoice.raiseTier(3);
         }

      }
   }

   public void weightFromUserOptions(PixelmonWrapper pw, MoveChoice userChoice, ArrayList bestChoicesBefore, ArrayList bestChoicesAfter) {
      this.weightFromUserOptions(pw, userChoice, bestChoicesBefore, bestChoicesAfter, true);
   }

   public void weightFromUserOptions(PixelmonWrapper pw, MoveChoice userChoice, ArrayList bestChoicesBefore, ArrayList bestChoicesAfter, boolean weightNegative) {
      if (bestChoicesBefore != null && bestChoicesAfter != null && !bestChoicesBefore.isEmpty() && !bestChoicesAfter.isEmpty()) {
         MoveChoice choiceBefore = (MoveChoice)bestChoicesBefore.get(0);
         MoveChoice choiceAfter = (MoveChoice)bestChoicesAfter.get(0);
         float healthBefore = 0.0F;
         float healthAfter = 0.0F;
         ArrayList allies = pw.bc.getTeamPokemonExcludeSelf(pw);
         Iterator var11 = choiceBefore.targets.iterator();

         PixelmonWrapper target;
         while(var11.hasNext()) {
            target = (PixelmonWrapper)var11.next();
            if (!allies.contains(target)) {
               healthBefore += target.getHealthPercent();
            }
         }

         var11 = choiceAfter.targets.iterator();

         while(var11.hasNext()) {
            target = (PixelmonWrapper)var11.next();
            if (!allies.contains(target)) {
               healthAfter += target.getHealthPercent();
            }
         }

         float damageBefore = choiceBefore.tier >= 3 ? healthBefore : choiceBefore.weight;
         float damageAfter = choiceAfter.tier >= 3 ? healthAfter : choiceAfter.weight;
         if (healthBefore != 0.0F && healthAfter != 0.0F && damageBefore != 0.0F && damageAfter != 0.0F) {
            double koTurnsBefore = Math.ceil((double)(healthBefore / damageBefore));
            double koTurnsAfter = Math.ceil((double)(healthAfter / damageAfter));
            if (koTurnsBefore > koTurnsAfter) {
               userChoice.raiseWeight((1.0F - (float)(koTurnsAfter / koTurnsBefore)) * 100.0F);
            } else if (koTurnsBefore < koTurnsAfter) {
               userChoice.weight -= (1.0F - (float)(koTurnsBefore / koTurnsAfter)) * 100.0F;
            }
         }

      }
   }

   public void weightFromOpponentOptions(PixelmonWrapper pw, MoveChoice userChoice, ArrayList bestChoicesBefore, ArrayList bestChoicesAfter) {
      this.weightFromOpponentOptions(pw, userChoice, bestChoicesBefore, bestChoicesAfter, true);
   }

   public void weightFromOpponentOptions(PixelmonWrapper pw, MoveChoice userChoice, ArrayList bestChoicesBefore, ArrayList bestChoicesAfter, boolean weightNegative) {
      if (bestChoicesBefore != null && bestChoicesAfter != null && bestChoicesBefore.size() == bestChoicesAfter.size()) {
         float totalHealthBefore = 0.0F;
         float totalHealthAfter = 0.0F;
         float damageBefore = 0.0F;
         float damageAfter = 0.0F;

         for(int i = 0; i < bestChoicesBefore.size(); ++i) {
            ArrayList bestChoicesBeforeSingle = (ArrayList)bestChoicesBefore.get(i);
            ArrayList bestChoicesAfterSingle = (ArrayList)bestChoicesAfter.get(i);
            if (!bestChoicesBeforeSingle.isEmpty() && !bestChoicesAfterSingle.isEmpty()) {
               MoveChoice choiceBefore = (MoveChoice)bestChoicesBeforeSingle.get(0);
               MoveChoice choiceAfter = (MoveChoice)bestChoicesAfterSingle.get(0);
               float healthBefore = 0.0F;
               float healthAfter = 0.0F;
               ArrayList allies = pw.bc.getTeamPokemonExcludeSelf(choiceBefore.user);
               Iterator var18 = choiceBefore.targets.iterator();

               PixelmonWrapper target;
               while(var18.hasNext()) {
                  target = (PixelmonWrapper)var18.next();
                  if (!allies.contains(target)) {
                     healthBefore += target.getHealthPercent();
                  }
               }

               var18 = choiceAfter.targets.iterator();

               while(var18.hasNext()) {
                  target = (PixelmonWrapper)var18.next();
                  if (!allies.contains(target)) {
                     healthAfter += target.getHealthPercent();
                  }
               }

               totalHealthBefore += healthBefore;
               totalHealthAfter += healthAfter;
               damageBefore += choiceBefore.tier >= 3 ? healthBefore : choiceBefore.weight;
               damageAfter += choiceAfter.tier >= 3 ? healthAfter : choiceAfter.weight;
            }
         }

         if (totalHealthBefore != 0.0F && totalHealthAfter != 0.0F && damageBefore != 0.0F && damageAfter != 0.0F) {
            double koTurnsBefore = Math.ceil((double)(totalHealthBefore / damageBefore));
            double koTurnsAfter = Math.ceil((double)(totalHealthAfter / damageAfter));
            if (koTurnsBefore < koTurnsAfter) {
               userChoice.raiseWeight((1.0F - (float)(koTurnsBefore / koTurnsAfter)) * 100.0F);
            } else if (koTurnsBefore > koTurnsAfter && weightNegative) {
               userChoice.weight -= (1.0F - (float)(koTurnsAfter / koTurnsBefore)) * 100.0F;
            }
         }

      }
   }

   public void weightTypeChange(PixelmonWrapper pw, MoveChoice userChoice, List possibleTypes, ArrayList bestUserChoices, ArrayList bestOpponentChoices) {
      this.weightTypeChange(pw, userChoice, possibleTypes, pw, bestUserChoices, bestOpponentChoices);
   }

   public void weightTypeChange(PixelmonWrapper pw, MoveChoice userChoice, List possibleTypes, PixelmonWrapper target, ArrayList bestUserChoices, ArrayList bestOpponentChoices) {
      List saveType = target.type;
      ArrayList opponents = pw.getOpponentPokemon();
      ArrayList bestUserChoicesAfter = null;
      ArrayList bestOpponentChoicesAfter = null;
      BattleAIBase ai = pw.getBattleAI();
      boolean will2HKO = true;

      try {
         Iterator var13 = possibleTypes.iterator();

         while(var13.hasNext()) {
            EnumType type = (EnumType)var13.next();
            if (type != null) {
               ArrayList newType = type.makeTypeList();
               target.type = newType;
               bestUserChoicesAfter = ai.getBestAttackChoices(pw);
               bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
               ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
               ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
               if (!MoveChoice.canOutspeedAnd2HKO(MoveChoice.mergeChoices(bestOpponentChoicesAfter), pw, bestUserChoicesAfter)) {
                  will2HKO = false;
               }
            }
         }
      } finally {
         target.type = saveType;
      }

      if (userChoice.weight <= 0.0F) {
         userChoice.tier = 0;
      } else {
         userChoice.weight /= (float)possibleTypes.size();
         if (will2HKO) {
            userChoice.lowerTier(1);
         }
      }
   }

   public void weightSingleTypeChange(PixelmonWrapper pw, MoveChoice userChoice, List newType, PixelmonWrapper target, ArrayList bestUserChoices, ArrayList bestOpponentChoices) {
      List saveType = target.type;
      ArrayList opponents = pw.getOpponentPokemon();
      ArrayList bestUserChoicesAfter = null;
      ArrayList bestOpponentChoicesAfter = null;
      BattleAIBase ai = pw.getBattleAI();
      boolean will2HKO = true;

      try {
         target.type = newType;
         bestUserChoicesAfter = ai.getBestAttackChoices(pw);
         bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
         ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
         ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
         if (!MoveChoice.canOutspeedAnd2HKO(MoveChoice.mergeChoices(bestOpponentChoicesAfter), pw, bestUserChoicesAfter)) {
            will2HKO = false;
         }
      } finally {
         target.type = saveType;
      }

      if (userChoice.weight <= 0.0F) {
         userChoice.tier = 0;
      } else {
         if (will2HKO) {
            userChoice.lowerTier(1);
         }

      }
   }

   public void weightStatusOpponentOptions(PixelmonWrapper pw, MoveChoice userChoice, PixelmonWrapper target, StatusBase status, ArrayList opponents, ArrayList bestOpponentChoices) {
      try {
         pw.bc.simulateMode = false;
         pw.bc.sendMessages = false;
         target.addStatus(status, pw);
         pw.bc.simulateMode = true;
         pw.bc.modifyStats();
         pw.bc.modifyStatsCancellable(pw);
         ArrayList bestOpponentChoicesAfter = this.getBestAttackChoices(opponents);
         this.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
      } finally {
         pw.bc.simulateMode = false;
         target.removeStatus(status.type);
         pw.bc.simulateMode = true;
         pw.bc.sendMessages = true;
         pw.bc.modifyStats();
         pw.bc.modifyStatsCancellable(pw);
      }

   }
}
