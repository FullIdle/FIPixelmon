package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationStationary;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackStatChange;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.OHKO;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Competitive;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Contrary;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Defiant;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Unaware;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BattleStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StatsEffectTeam extends StatsEffect {
   public StatsEffectTeam() {
   }

   public StatsEffectTeam(StatsType type, int value, boolean isUser) {
      super(type, value, isUser);
   }

   public AttackResult applyStatEffect(PixelmonWrapper user, PixelmonWrapper target, AttackBase a) {
      List affectedList = this.isUser ? user.getTeamPokemon() : target.getTeamPokemon();
      Iterator var5 = affectedList.iterator();

      while(true) {
         while(true) {
            PixelmonWrapper affected;
            do {
               do {
                  if (!var5.hasNext()) {
                     return AttackResult.succeeded;
                  }

                  affected = (PixelmonWrapper)var5.next();
               } while(affected.isFainted());
            } while(!this.checkChance());

            if (affected.getBattleStats().modifyStat(this.amount, this.type, user, true)) {
               addStatChangeAnimation(user, affected, this.type, this.amount);
            } else if (a != null && a.getAttackCategory() != AttackCategory.STATUS) {
               addStatChangeAnimation(user, affected, this.type, this.amount);
            } else if (this.amount != 0) {
               boolean increase = this.amount > 0;
               if (affected.getBattleAbility(user) instanceof Contrary && (!user.attack.isZ || user.attack.isAttack("Extreme Evoboost"))) {
                  increase = !increase;
               }

               affected.getBattleStats().getStatFailureMessage(this.type, increase);
            }
         }
      }
   }

   public static void addStatChangeAnimation(PixelmonWrapper user, PixelmonWrapper target, StatsType stat, int stages) {
      if (!user.bc.simulateMode) {
         AttackAnimationStationary animation = new AttackAnimationStationary();
         animation.effects = new HashMap();
         animation.durationTicks = 20;
         AttackStatChange.StatChangeData effect = new AttackStatChange.StatChangeData(stat, stages);
         animation.effects.put(0, effect);
         animation.groundedEndPosition = true;
         animation.initialize(user, target, user.attack == null ? new Attack("Tackle") : user.attack);
         BattleControllerBase.currentAnimations.add(animation);
      }
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.applyStatEffect(user, target, (AttackBase)null);
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return this.isUser;
   }

   public StatsType getStatsType() {
      return this.type;
   }

   public boolean getUser() {
      return this.isUser;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      float chance = this.getChance();
      if (chance < 50.0F) {
         if (userChoice.isMiddleTier() || this.isUser) {
            userChoice.raiseWeight(chance / 100.0F);
         }

      } else {
         if (!this.isUser && this.amount < 0) {
            if (userChoice.hitsAlly()) {
               return;
            }

            Iterator var8 = userChoice.targets.iterator();

            label1804:
            while(true) {
               PixelmonWrapper target;
               do {
                  if (!var8.hasNext()) {
                     break label1804;
                  }

                  target = (PixelmonWrapper)var8.next();
               } while(!(target.getBattleAbility() instanceof Competitive) && !(target.getBattleAbility() instanceof Defiant));

               userChoice.weight -= 50.0F;
            }
         }

         BattleStats[] originalStats;
         BattleStats[] saveStats;
         int i;
         PixelmonWrapper opponent;
         ArrayList opponents;
         ArrayList bestChoicesBefore;
         Iterator var49;
         if (this.type != StatsType.Attack && this.type != StatsType.SpecialAttack && this.type != StatsType.Defence && this.type != StatsType.SpecialDefence) {
            Iterator var44;
            if (this.type == StatsType.Speed) {
               if (!this.isUser) {
                  bestOpponentChoices = MoveChoice.getAffectedChoices(userChoice, bestOpponentChoices);
               }

               if (MoveChoice.hasPriority(bestUserChoices, bestOpponentChoices)) {
                  return;
               }

               opponents = this.getAffectedOpponents(pw, userChoice);
               int numOutspeedingOpponentsBefore = 0;
               int numOutspeedingOpponentsAfter = 0;
               var44 = opponents.iterator();

               while(var44.hasNext()) {
                  PixelmonWrapper opponent = (PixelmonWrapper)var44.next();
                  if (pw.bc.getFirstMover(pw, opponent) == opponent) {
                     ++numOutspeedingOpponentsBefore;
                  }
               }

               if (numOutspeedingOpponentsBefore == 0) {
                  return;
               }

               pw.bc.sendMessages = false;
               pw.bc.simulateMode = false;
               originalStats = new BattleStats[userChoice.targets.size()];
               saveStats = new BattleStats[userChoice.targets.size()];
               if (this.isUser) {
                  originalStats[0] = pw.getBattleStats();
                  saveStats[0] = new BattleStats(originalStats[0]);
               } else {
                  for(i = 0; i < originalStats.length; ++i) {
                     originalStats[i] = ((PixelmonWrapper)userChoice.targets.get(i)).getBattleStats();
                     saveStats[i] = new BattleStats(originalStats[i]);
                  }
               }

               boolean var31 = false;

               try {
                  var31 = true;
                  if (this.isUser) {
                     pw.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                  } else {
                     var49 = userChoice.targets.iterator();

                     while(var49.hasNext()) {
                        opponent = (PixelmonWrapper)var49.next();
                        opponent.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                     }
                  }

                  CalcPriority.checkMoveSpeed(pw.bc);
                  var49 = opponents.iterator();

                  while(true) {
                     if (!var49.hasNext()) {
                        var31 = false;
                        break;
                     }

                     opponent = (PixelmonWrapper)var49.next();
                     if (pw.bc.getFirstMover(pw, opponent) == opponent) {
                        ++numOutspeedingOpponentsAfter;
                     }
                  }
               } finally {
                  if (var31) {
                     for(int i = 0; i < originalStats.length; ++i) {
                        originalStats[i].copyStats(saveStats[i]);
                     }

                  }
               }

               for(i = 0; i < originalStats.length; ++i) {
                  originalStats[i].copyStats(saveStats[i]);
               }

               pw.bc.simulateMode = true;
               pw.bc.sendMessages = true;
               if (numOutspeedingOpponentsAfter == 0) {
                  userChoice.raiseWeight(75.0F);
               }
            } else if (this.type == StatsType.Accuracy || this.type == StatsType.Evasion) {
               label1857: {
                  boolean affectsUser = this.isUser && this.type == StatsType.Accuracy || !this.isUser && this.type == StatsType.Evasion;
                  Iterator var38;
                  MoveChoice bestUserChoice;
                  EffectBase effect;
                  if (!affectsUser) {
                     var38 = bestOpponentChoices.iterator();

                     label1751:
                     while(true) {
                        do {
                           if (!var38.hasNext()) {
                              break label1751;
                           }

                           bestUserChoice = (MoveChoice)var38.next();
                        } while(!bestUserChoice.isOffensiveMove());

                        if (bestUserChoice.result.accuracy < 0) {
                           return;
                        }

                        var44 = bestUserChoice.attack.getMove().effects.iterator();

                        while(var44.hasNext()) {
                           effect = (EffectBase)var44.next();
                           if (effect instanceof OHKO) {
                              return;
                           }
                        }
                     }
                  } else {
                     var38 = bestUserChoices.iterator();

                     label1772:
                     while(true) {
                        do {
                           if (!var38.hasNext()) {
                              if (MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, bestUserChoices)) {
                                 return;
                              }
                              break label1772;
                           }

                           bestUserChoice = (MoveChoice)var38.next();
                        } while(!bestUserChoice.isOffensiveMove());

                        if (bestUserChoice.result.accuracy < 0 || bestUserChoice.result.accuracy == 100) {
                           return;
                        }

                        var44 = bestUserChoice.attack.getMove().effects.iterator();

                        while(var44.hasNext()) {
                           effect = (EffectBase)var44.next();
                           if (effect instanceof OHKO) {
                              return;
                           }
                        }
                     }
                  }

                  if (this.isUser && pw.getBattleAbility() instanceof Contrary) {
                     return;
                  }

                  bestChoicesBefore = this.getAffectedOpponents(pw, userChoice);
                  Iterator var42 = bestChoicesBefore.iterator();

                  AbilityBase opponentAbility;
                  do {
                     if (!var42.hasNext()) {
                        userChoice.raiseWeight((float)(20 * Math.abs(this.amount)));
                        break label1857;
                     }

                     PixelmonWrapper opponent = (PixelmonWrapper)var42.next();
                     opponentAbility = opponent.getBattleAbility();
                  } while(!(opponentAbility instanceof Unaware) && (this.isUser || !(opponentAbility instanceof Contrary)));

                  return;
               }
            }
         } else {
            pw.bc.sendMessages = false;
            if (this.isUser && (this.type == StatsType.Attack || this.type == StatsType.SpecialAttack) || !this.isUser && (this.type == StatsType.Defence || this.type == StatsType.SpecialDefence)) {
               pw.bc.simulateMode = false;
               BattleStats[] originalStats = new BattleStats[userChoice.targets.size()];
               originalStats = new BattleStats[userChoice.targets.size()];
               int i;
               if (this.isUser) {
                  originalStats[0] = pw.getBattleStats();
                  originalStats[0] = new BattleStats(originalStats[0]);
               } else {
                  for(i = 0; i < originalStats.length; ++i) {
                     originalStats[i] = ((PixelmonWrapper)userChoice.targets.get(i)).getBattleStats();
                     originalStats[i] = new BattleStats(originalStats[i]);
                  }
               }

               boolean var27 = false;

               try {
                  var27 = true;
                  if (this.isUser) {
                     pw.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                  } else {
                     Iterator var47 = userChoice.targets.iterator();

                     while(var47.hasNext()) {
                        PixelmonWrapper target = (PixelmonWrapper)var47.next();
                        target.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                     }
                  }

                  pw.bc.simulateMode = true;
                  bestChoicesBefore = pw.getBattleAI().getBestAttackChoices(pw);
                  pw.bc.simulateMode = false;
                  var27 = false;
               } finally {
                  if (var27) {
                     pw.bc.simulateMode = false;

                     for(int i = 0; i < originalStats.length; ++i) {
                        originalStats[i].copyStats(originalStats[i]);
                     }

                  }
               }

               pw.bc.simulateMode = false;

               for(i = 0; i < originalStats.length; ++i) {
                  originalStats[i].copyStats(originalStats[i]);
               }

               boolean weightNegative = true;
               if (this.isUser) {
                  EnumHeldItems heldItem = pw.getUsableHeldItem().getHeldItemType();
                  weightNegative = heldItem != EnumHeldItems.whiteHerb && heldItem != EnumHeldItems.ginemaBerry;
               }

               pw.getBattleAI().weightFromUserOptions(pw, userChoice, bestUserChoices, bestChoicesBefore, weightNegative);
               if (MoveChoice.canOutspeedAnd2HKO(bestOpponentChoices, pw, bestUserChoices) && !userChoice.isOffensiveMove()) {
                  userChoice.lowerTier(1);
               }
            } else if (this.isUser && (this.type == StatsType.Defence || this.type == StatsType.SpecialDefence) || !this.isUser && (this.type == StatsType.Attack || this.type == StatsType.SpecialAttack)) {
               opponents = this.getAffectedOpponents(pw, userChoice);
               bestChoicesBefore = MoveChoice.splitChoices(opponents, bestOpponentChoices);
               ArrayList bestChoicesAfter = new ArrayList(opponents.size());
               pw.bc.simulateMode = false;
               originalStats = new BattleStats[userChoice.targets.size()];
               saveStats = new BattleStats[userChoice.targets.size()];
               if (this.isUser) {
                  originalStats[0] = pw.getBattleStats();
                  saveStats[0] = new BattleStats(originalStats[0]);
               } else {
                  for(i = 0; i < originalStats.length; ++i) {
                     originalStats[i] = ((PixelmonWrapper)userChoice.targets.get(i)).getBattleStats();
                     saveStats[i] = new BattleStats(originalStats[i]);
                  }
               }

               boolean var23 = false;

               try {
                  var23 = true;
                  if (this.isUser) {
                     pw.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                  } else {
                     for(i = 0; i < saveStats.length; ++i) {
                        opponent = (PixelmonWrapper)userChoice.targets.get(i);
                        opponent.getBattleStats().modifyStat(this.amount, this.type, pw, true);
                     }
                  }

                  pw.bc.simulateMode = true;
                  var49 = opponents.iterator();

                  while(true) {
                     if (!var49.hasNext()) {
                        pw.bc.simulateMode = false;
                        var23 = false;
                        break;
                     }

                     opponent = (PixelmonWrapper)var49.next();
                     ArrayList after = pw.getBattleAI().getBestAttackChoices(opponent);
                     bestChoicesAfter.add(after);
                  }
               } finally {
                  if (var23) {
                     pw.bc.simulateMode = false;

                     for(int i = 0; i < saveStats.length; ++i) {
                        originalStats[i].copyStats(saveStats[i]);
                     }

                  }
               }

               pw.bc.simulateMode = false;

               for(i = 0; i < saveStats.length; ++i) {
                  originalStats[i].copyStats(saveStats[i]);
               }

               if (bestChoicesBefore.size() != bestChoicesAfter.size()) {
                  pw.bc.simulateMode = true;
                  pw.bc.sendMessages = true;
                  return;
               }

               pw.getBattleAI().weightFromOpponentOptions(pw, userChoice, bestChoicesBefore, bestChoicesAfter, false);
            }

            pw.bc.simulateMode = true;
            pw.bc.sendMessages = true;
         }

         if (this.isUser && this.amount > 0 && MoveChoice.hasSuccessfulAttackChoice(userChoices, "Baton Pass", "Stored Power")) {
            userChoice.raiseWeight((float)(10 * this.amount));
         }

         if (!this.isUser && userChoice.weight > 100.0F) {
            userChoice.weight = 100.0F;
         }

      }
   }

   private ArrayList getAffectedOpponents(PixelmonWrapper pw, MoveChoice choice) {
      return this.isUser ? pw.bc.getOpponentPokemon(pw) : choice.targets;
   }
}
