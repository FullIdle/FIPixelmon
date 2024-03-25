package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Competitive;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Contrary;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Defiant;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Simple;
import java.util.ArrayList;
import java.util.Iterator;

public class BattleStats {
   private PixelmonWrapper pixelmon;
   public int accuracy;
   public int evasion;
   public int attackModifier;
   public int defenceModifier;
   public int specialAttackModifier;
   public int specialDefenceModifier;
   public int speedModifier;
   public int attackStat;
   public int defenceStat;
   public int specialAttackStat;
   public int specialDefenceStat;
   public int speedStat;
   private int critStage;
   private boolean focused;
   private int[] stages = new int[7];
   private boolean loweredThisTurn = false;
   private boolean raisedThisTurn = false;

   public BattleStats(PixelmonWrapper pixelmon) {
      this.pixelmon = pixelmon;
      this.clearBattleStats(true);
   }

   public BattleStats(BattleStats original) {
      this.copyStats(original);
   }

   public int getAccuracy() {
      return this.accuracy;
   }

   public int getEvasion() {
      return this.evasion;
   }

   public double getAttackModifier() {
      return (double)this.attackModifier;
   }

   public int getDefenceModifier() {
      return this.defenceModifier;
   }

   public double getSpecialAttackModifier() {
      return (double)this.specialAttackModifier;
   }

   public int getSpecialDefenceModifier() {
      return this.specialDefenceModifier;
   }

   public double getSpeedModifier() {
      return (double)this.speedModifier;
   }

   public int getAccuracyStage() {
      return this.stages[StatsType.Accuracy.getStatIndex()];
   }

   public int getEvasionStage() {
      return this.stages[StatsType.Evasion.getStatIndex()];
   }

   public int[] getStages() {
      return this.stages;
   }

   public boolean modifyStat(int amount, StatsType stat) {
      return this.modifyStat(new int[]{amount}, new StatsType[]{stat}, this.pixelmon, false);
   }

   public boolean modifyStat(int amount, StatsType... stats) {
      int[] amounts = new int[stats.length];

      for(int i = 0; i < stats.length; ++i) {
         amounts[i] = amount;
      }

      return this.modifyStat(amounts, stats, this.pixelmon, false);
   }

   public boolean modifyStat(int[] amounts, StatsType[] stats) {
      return this.modifyStat(amounts, stats, this.pixelmon, false);
   }

   public boolean modifyStat(int amount, StatsType[] stats, PixelmonWrapper user) {
      int[] amounts = new int[stats.length];

      for(int i = 0; i < stats.length; ++i) {
         amounts[i] = amount;
      }

      return this.modifyStat(amounts, stats, user, false);
   }

   public boolean modifyStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack) {
      return this.modifyStat(new int[]{amount}, new StatsType[]{stat}, user, isAttack);
   }

   public boolean modifyStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack, boolean messages) {
      return this.modifyStat(new int[]{amount}, new StatsType[]{stat}, user, isAttack, messages);
   }

   public boolean modifyStat(int[] amounts, StatsType[] stats, PixelmonWrapper user, boolean isAttack) {
      return this.modifyStat(amounts, stats, user, isAttack, true);
   }

   public boolean modifyStat(int[] amounts, StatsType[] stats, PixelmonWrapper user, boolean isAttack, boolean messages) {
      if (this.pixelmon.isFainted()) {
         return false;
      } else {
         boolean anySucceeded = false;

         for(int i = 0; i < stats.length; ++i) {
            int amount = amounts[i];
            StatsType stat = stats[i];
            AbilityBase thisAbility = this.pixelmon.getBattleAbility(user);
            if (thisAbility instanceof Contrary) {
               if (user.attack != null) {
                  if (!user.attack.isZ || user.attack.isAttack("Extreme Evoboost") || user.attack.isAttack("Clangorous Soulblaze")) {
                     amount *= -1;
                  }
               } else {
                  amount *= -1;
               }
            } else if (thisAbility instanceof Simple) {
               if (user.attack != null) {
                  if (!user.attack.isZ || user.attack.isAttack("Extreme Evoboost") || user.attack.isAttack("Clangorous Soulblaze")) {
                     amount *= 2;
                  }
               } else {
                  amount *= 2;
               }
            }

            boolean succeeded = false;
            if (amount > 0) {
               succeeded = this.increaseStat(amount, stat, user, isAttack, messages);
            } else if (amount < 0) {
               succeeded = this.decreaseStat(amount * -1, stat, user, isAttack, messages);
            }

            if (succeeded) {
               anySucceeded = true;
            }
         }

         if (!isAttack) {
            this.pixelmon.getUsableHeldItem().onStatModified(this.pixelmon);
         }

         return anySucceeded;
      }
   }

   public boolean increaseCritStage(int value, boolean isFocusEnergy) {
      if (this.focused && isFocusEnergy) {
         return false;
      } else {
         if (!this.pixelmon.bc.simulateMode) {
            this.critStage += value;
            if (isFocusEnergy) {
               this.focused = true;
            }
         }

         return true;
      }
   }

   public boolean decreaseCritStage(int value) {
      if (!this.pixelmon.bc.simulateMode) {
         this.critStage -= value;
         return true;
      } else {
         return false;
      }
   }

   public int getCritStage() {
      return this.critStage;
   }

   public int GetAccOrEva(double stage) {
      if (stage > 6.0) {
         stage = 6.0;
      } else if (stage < -6.0) {
         stage = -6.0;
      }

      return stage < 1.0 ? (int)Math.round(3.0 / (Math.abs(stage) + 3.0) * 100.0) : (int)Math.round((Math.abs(stage) + 3.0) / 3.0 * 100.0);
   }

   private int GetStat(double stage) {
      if (stage > 6.0) {
         stage = 6.0;
      } else if (stage < -6.0) {
         stage = -6.0;
      }

      return stage < 1.0 ? (int)Math.round(2.0 / (Math.abs(stage) + 2.0) * 100.0) : (int)Math.round((Math.abs(stage) + 2.0) / 2.0 * 100.0);
   }

   public boolean increaseStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack) {
      return this.increaseStat(amount, stat, user, isAttack, true);
   }

   public boolean increaseStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack, boolean sendMessage) {
      if (amount < 0) {
         return this.decreaseStat(Math.abs(amount), stat, user, isAttack);
      } else {
         int stageIndex = stat.getStatIndex();
         if (stageIndex == -1) {
            return false;
         } else {
            int currentStage = this.stages[stageIndex];
            if (currentStage == 6) {
               if (!isAttack) {
                  this.getStatFailureMessage(stat, true);
               }

               return false;
            } else {
               currentStage += Math.abs(amount);
               if (currentStage > 6) {
                  amount -= currentStage - 6;
                  currentStage = 6;
               }

               if (!this.pixelmon.bc.simulateMode) {
                  this.stages[stageIndex] = currentStage;
               }

               if (stat != StatsType.Accuracy && stat != StatsType.Evasion) {
                  int newValue = this.GetStat((double)currentStage);
                  this.changeStat(stat, newValue);
               }

               if (sendMessage) {
                  String statMessage = "pixelmon.effect." + this.getStatStringLang(stat);
                  statMessage = statMessage + "increased";
                  if (amount == 2) {
                     statMessage = statMessage + "2";
                  } else if (amount >= 3) {
                     statMessage = statMessage + "3";
                  }

                  this.pixelmon.bc.sendToAll(statMessage, this.pixelmon.getNickname());
               }

               this.setRaisedThisTurn(true);
               return true;
            }
         }
      }
   }

   public boolean decreaseStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack) {
      return this.decreaseStat(amount, stat, user, isAttack, true);
   }

   public boolean decreaseStat(int amount, StatsType stat, PixelmonWrapper user, boolean isAttack, boolean messages) {
      int stageIndex = stat.getStatIndex();
      if (stageIndex == -1) {
         return false;
      } else {
         StatsEffect effect = new StatsEffect(stat, amount * -1, user == this.pixelmon);
         if (!this.pixelmon.getBattleAbility(user).allowsStatChange(this.pixelmon, user, effect)) {
            return false;
         } else {
            Iterator var8 = this.pixelmon.getStatuses().iterator();

            StatusBase status;
            do {
               if (!var8.hasNext()) {
                  int currentStage = this.stages[stageIndex];
                  if (currentStage == -6) {
                     if (!isAttack) {
                        this.getStatFailureMessage(stat, false);
                     }

                     return false;
                  }

                  currentStage -= Math.abs(amount);
                  if (currentStage < -6) {
                     amount -= -6 - currentStage;
                     currentStage = -6;
                  }

                  if (!this.pixelmon.bc.simulateMode) {
                     this.stages[stageIndex] = currentStage;
                  }

                  if (stat != StatsType.Accuracy && stat != StatsType.Evasion) {
                     int newValue = this.GetStat((double)currentStage);
                     this.changeStat(stat, newValue);
                  }

                  if (messages) {
                     String statMessage = "pixelmon.effect." + this.getStatStringLang(stat);
                     statMessage = statMessage + "decreased";
                     if (amount == 2) {
                        statMessage = statMessage + "2";
                     } else if (amount >= 3) {
                        statMessage = statMessage + "3";
                     }

                     this.pixelmon.bc.sendToAll(statMessage, this.pixelmon.getNickname());
                  }

                  if (user != this.pixelmon && this.pixelmon.bc.sendMessages) {
                     AbilityBase thisAbility = this.pixelmon.getBattleAbility();
                     if (thisAbility instanceof Defiant) {
                        thisAbility.sendActivatedMessage(this.pixelmon);
                        this.modifyStat(2, (StatsType)StatsType.Attack);
                     } else if (thisAbility instanceof Competitive) {
                        thisAbility.sendActivatedMessage(this.pixelmon);
                        this.modifyStat(2, (StatsType)StatsType.SpecialAttack);
                     }
                  }

                  this.loweredThisTurn = true;
                  return true;
               }

               status = (StatusBase)var8.next();
            } while(status.allowsStatChange(this.pixelmon, user, effect));

            return false;
         }
      }
   }

   public void changeStat(StatsType stat, int value) {
      if (!this.pixelmon.bc.simulateMode) {
         switch (stat) {
            case Accuracy:
               this.accuracy = value;
               break;
            case Evasion:
               this.evasion = value;
               break;
            case Attack:
               this.attackModifier = value;
               break;
            case Defence:
               this.defenceModifier = value;
               break;
            case SpecialAttack:
               this.specialAttackModifier = value;
               break;
            case SpecialDefence:
               this.specialDefenceModifier = value;
               break;
            case Speed:
               this.speedModifier = value;
         }
      }

   }

   public int getStatFromEnum(StatsType stat) {
      switch (stat) {
         case Accuracy:
            return this.accuracy;
         case Evasion:
            return this.evasion;
         case Attack:
            return this.attackStat;
         case Defence:
            return this.defenceStat;
         case SpecialAttack:
            return this.specialAttackStat;
         case SpecialDefence:
            return this.specialDefenceStat;
         case Speed:
            return this.speedStat;
         default:
            return 0;
      }
   }

   public int getStatModFromEnum(StatsType stat) {
      switch (stat) {
         case Accuracy:
            return this.accuracy;
         case Evasion:
            return this.evasion;
         case Attack:
            return this.attackModifier;
         case Defence:
            return this.defenceModifier;
         case SpecialAttack:
            return this.specialAttackModifier;
         case SpecialDefence:
            return this.specialDefenceModifier;
         case Speed:
            return this.speedModifier;
         default:
            return 0;
      }
   }

   public StatsType getStageEnum(int index) {
      switch (index) {
         case 0:
            return StatsType.Accuracy;
         case 1:
            return StatsType.Evasion;
         case 2:
            return StatsType.Attack;
         case 3:
            return StatsType.Defence;
         case 4:
            return StatsType.SpecialAttack;
         case 5:
            return StatsType.SpecialDefence;
         case 6:
            return StatsType.Speed;
         default:
            return StatsType.None;
      }
   }

   public int getStage(StatsType stat) {
      return this.stages[stat.getStatIndex()];
   }

   public void setStage(StatsType stat, int value) {
      if (!this.pixelmon.bc.simulateMode) {
         this.stages[stat.getStatIndex()] = value;
      }

   }

   public void swapStats(BattleStats otherPokemon, StatsType... stats) {
      if (!this.pixelmon.bc.simulateMode) {
         StatsType[] var3 = stats;
         int var4 = stats.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            StatsType stat = var3[var5];
            int index = stat.getStatIndex();
            int tempStat = this.getStatModFromEnum(stat);
            int tempStage = this.stages[index];
            this.changeStat(stat, otherPokemon.getStatModFromEnum(stat));
            this.stages[index] = otherPokemon.getStage(stat);
            otherPokemon.changeStat(stat, tempStat);
            otherPokemon.setStage(stat, tempStage);
         }
      }

   }

   public void reverseStats() {
      if (!this.pixelmon.bc.simulateMode) {
         StatsType[] var1 = new StatsType[]{StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed, StatsType.Accuracy, StatsType.Evasion};
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            StatsType stat = var1[var3];
            int statIndex = stat.getStatIndex();
            this.stages[statIndex] = -this.stages[statIndex];
            if (stat != StatsType.Accuracy && stat != StatsType.Evasion) {
               int newValue = this.GetStat((double)this.stages[statIndex]);
               this.changeStat(stat, newValue);
            }
         }
      }

   }

   public void resetStat(int index) {
      if (!this.pixelmon.bc.simulateMode) {
         this.changeStat(this.getStageEnum(index), 100);
         this.stages[index] = 0;
      }

   }

   public void resetStat(StatsType stat) {
      if (!this.pixelmon.bc.simulateMode) {
         this.changeStat(stat, 100);
         this.stages[stat.getStatIndex()] = 0;
      }

   }

   public void clearBattleStats() {
      this.clearBattleStats(false);
   }

   public void clearBattleStats(boolean init) {
      if (init || !this.pixelmon.bc.simulateMode) {
         this.attackModifier = this.defenceModifier = this.specialAttackModifier = this.specialDefenceModifier = this.speedModifier = this.accuracy = this.evasion = 100;
         this.critStage = 0;
         this.focused = false;
         this.stages = new int[7];
      }

   }

   public void clearBattleStatsNoCrit() {
      if (!this.pixelmon.bc.simulateMode) {
         this.attackModifier = this.defenceModifier = this.specialAttackModifier = this.specialDefenceModifier = this.speedModifier = this.accuracy = this.evasion = 100;
         this.stages = new int[7];
      }

   }

   public void resetLoweredBattleStats() {
      if (!this.pixelmon.bc.simulateMode) {
         for(int i = 0; i < 6; ++i) {
            if (this.stages[i] < 0) {
               this.stages[i] = 0;
            }
         }
      }

   }

   public void copyStats(BattleStats battleStats) {
      if (this.pixelmon == null || !this.pixelmon.bc.simulateMode) {
         this.speedModifier = battleStats.speedModifier;
         this.attackModifier = battleStats.attackModifier;
         this.defenceModifier = battleStats.defenceModifier;
         this.specialAttackModifier = battleStats.specialAttackModifier;
         this.specialDefenceModifier = battleStats.specialDefenceModifier;
         this.evasion = battleStats.evasion;
         this.accuracy = battleStats.accuracy;
         System.arraycopy(battleStats.stages, 0, this.stages, 0, this.stages.length);
      }

   }

   public boolean isStatModified() {
      int[] var1 = this.stages;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int stage = var1[var3];
         if (stage != 0) {
            return true;
         }
      }

      return false;
   }

   public int getSumIncreases() {
      int increases = 0;
      int[] var2 = this.stages;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int stage = var2[var4];
         if (stage > 0) {
            increases += stage;
         }
      }

      return increases;
   }

   public int getSumStages() {
      int stageMods = 0;
      int[] var2 = this.stages;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int stage = var2[var4];
         stageMods += stage;
      }

      return stageMods;
   }

   public boolean statCanBeRaised() {
      int[] var1 = this.stages;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int stage = var1[var3];
         if (stage < 6) {
            return true;
         }
      }

      return false;
   }

   public boolean statCanBeLowered() {
      int[] var1 = this.stages;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int stage = var1[var3];
         if (stage > -6) {
            return true;
         }
      }

      return false;
   }

   public boolean statCanBeRaised(StatsType stat) {
      return this.stages[stat.getStatIndex()] < 6;
   }

   public boolean statCanBeLowered(StatsType stat) {
      return this.stages[stat.getStatIndex()] > -6;
   }

   public ArrayList getPossibleStatIncreases() {
      ArrayList possibleStats = new ArrayList();

      for(int i = 0; i < this.stages.length; ++i) {
         if (this.stages[i] < 6) {
            possibleStats.add(i);
         }
      }

      return possibleStats;
   }

   public boolean raiseRandomStat(int amount) {
      if (!this.statCanBeRaised()) {
         return false;
      } else if (this.pixelmon.bc.simulateMode) {
         return true;
      } else {
         ArrayList possibleStats = this.getPossibleStatIncreases();
         StatsType stat = this.getStageEnum((Integer)possibleStats.get(RandomHelper.getRandomNumberBetween(0, possibleStats.size() - 1)));
         return this.modifyStat(amount, stat);
      }
   }

   public int[] getBattleStats() {
      int[] stats = new int[7];
      stats[StatsType.Attack.getStatIndex()] = this.attackStat;
      stats[StatsType.Defence.getStatIndex()] = this.defenceStat;
      stats[StatsType.SpecialAttack.getStatIndex()] = this.specialAttackStat;
      stats[StatsType.SpecialDefence.getStatIndex()] = this.specialDefenceStat;
      stats[StatsType.Speed.getStatIndex()] = this.speedStat;
      stats[StatsType.Accuracy.getStatIndex()] = this.accuracy;
      stats[StatsType.Evasion.getStatIndex()] = this.evasion;
      return stats;
   }

   public int[] getBaseBattleStats() {
      int[] stats = new int[7];
      Stats statContainer = this.pixelmon.getStats();
      stats[StatsType.Attack.getStatIndex()] = statContainer.attack;
      stats[StatsType.Defence.getStatIndex()] = statContainer.defence;
      stats[StatsType.SpecialAttack.getStatIndex()] = statContainer.specialAttack;
      stats[StatsType.SpecialDefence.getStatIndex()] = statContainer.specialDefence;
      stats[StatsType.Speed.getStatIndex()] = statContainer.speed;
      stats[StatsType.Accuracy.getStatIndex()] = 100;
      stats[StatsType.Evasion.getStatIndex()] = 100;

      Iterator var3;
      StatusBase status;
      for(var3 = this.pixelmon.getStatuses().iterator(); var3.hasNext(); stats = status.modifyBaseStats(this.pixelmon, stats)) {
         status = (StatusBase)var3.next();
      }

      GlobalStatusBase status;
      for(var3 = this.pixelmon.bc.globalStatusController.getGlobalStatuses().iterator(); var3.hasNext(); stats = status.modifyBaseStats(this.pixelmon, stats)) {
         status = (GlobalStatusBase)var3.next();
      }

      return stats;
   }

   public void setStatsForTurn(int[] stats) {
      this.attackStat = stats[StatsType.Attack.getStatIndex()];
      this.defenceStat = stats[StatsType.Defence.getStatIndex()];
      this.specialAttackStat = stats[StatsType.SpecialAttack.getStatIndex()];
      this.specialDefenceStat = stats[StatsType.SpecialDefence.getStatIndex()];
      this.speedStat = stats[StatsType.Speed.getStatIndex()];
      this.evasion = stats[StatsType.Evasion.getStatIndex()];
      this.accuracy = stats[StatsType.Accuracy.getStatIndex()];
   }

   public String getStatStringLang(StatsType stat) {
      switch (stat) {
         case Accuracy:
            return "accuracy";
         case Evasion:
            return "evasion";
         case Attack:
            return "attack";
         case Defence:
            return "defense";
         case SpecialAttack:
            return "spatk";
         case SpecialDefence:
            return "spdef";
         case Speed:
            return "speed";
         default:
            return "";
      }
   }

   public void getStatFailureMessage(StatsType stat, boolean increase) {
      String failtext = "pixelmon.effect." + this.getStatStringLang(stat) + "too";
      if (increase) {
         failtext = failtext + "high";
      } else {
         failtext = failtext + "low";
      }

      this.pixelmon.bc.sendToAll(failtext, this.pixelmon.getNickname());
   }

   public int getStatWithMod(StatsType stat) {
      switch (stat) {
         case Accuracy:
            return this.accuracy;
         case Evasion:
            return this.evasion;
         case Attack:
            return Math.max(1, (int)((double)(this.attackStat * this.attackModifier) * 0.01));
         case Defence:
            return Math.max(1, (int)((double)(this.defenceStat * this.defenceModifier) * 0.01));
         case SpecialAttack:
            return Math.max(1, (int)((double)(this.specialAttackStat * this.specialAttackModifier) * 0.01));
         case SpecialDefence:
            return Math.max(1, (int)((double)(this.specialDefenceStat * this.specialDefenceModifier) * 0.01));
         case Speed:
            return Math.max(1, (int)((double)(this.speedStat * this.speedModifier) * 0.01));
         default:
            return 1;
      }
   }

   public void setLoweredThisTurn(boolean loweredThisTurn) {
      this.loweredThisTurn = loweredThisTurn;
   }

   public boolean isLoweredThisTurn() {
      return this.loweredThisTurn;
   }

   public boolean isRaisedThisTurn() {
      return this.raisedThisTurn;
   }

   public void setRaisedThisTurn(boolean raisedThisTurn) {
      this.raisedThisTurn = raisedThisTurn;
   }
}
