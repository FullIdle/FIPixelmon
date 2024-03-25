package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MoveChoice implements Comparable {
   public PixelmonWrapper user;
   public Attack attack;
   public ArrayList targets;
   public MoveResults result;
   public UUID switchPokemon;
   public int tier;
   public float weight;
   public static final int TIER_USELESS = 0;
   public static final int TIER_MINIMAL = 1;
   public static final int TIER_NORMAL = 2;
   public static final int TIER_KO = 3;

   public MoveChoice(PixelmonWrapper user, Attack attack, ArrayList targets) {
      this.user = user;
      this.attack = attack;
      this.targets = targets;
   }

   public MoveChoice(PixelmonWrapper user, UUID switchPokemon) {
      this.user = user;
      this.switchPokemon = switchPokemon;
   }

   public boolean isAttack() {
      return this.attack != null;
   }

   public boolean isStatusMove() {
      return this.isAttack() && this.attack.getAttackCategory() == AttackCategory.STATUS;
   }

   public boolean isOffensiveMove() {
      return this.isAttack() && this.attack.getAttackCategory() != AttackCategory.STATUS;
   }

   public boolean isSameType(MoveChoice other) {
      return this.isAttack() == other.isAttack();
   }

   public boolean isSimilarWeight(MoveChoice other) {
      if (this.isMiddleTier()) {
         return this.tier == other.tier && Math.abs(this.weight - other.weight) <= 20.0F;
      } else {
         return this.tier == other.tier && this.weight == other.weight;
      }
   }

   public void raiseWeight(float newWeight) {
      this.weight += newWeight;
      if (this.weight > 0.0F) {
         this.raiseTier(2);
      }

   }

   public void raiseWeightLimited(float newWeight) {
      this.raiseWeight(newWeight);
      this.weight = Math.min(this.weight, 100.0F);
   }

   public void setWeight(float newWeight) {
      this.weight = 0.0F;
      this.raiseWeight(newWeight);
   }

   public void raiseTier(int newTier) {
      this.tier = Math.max(this.tier, newTier);
   }

   public void lowerTier(int newTier) {
      this.tier = Math.min(this.tier, newTier);
   }

   public boolean isMiddleTier() {
      return this.tier == 1 || this.tier == 2;
   }

   public void resetWeight() {
      this.tier = 0;
      this.weight = 0.0F;
   }

   public boolean hitsAlly() {
      if (!this.isAttack()) {
         return false;
      } else {
         ArrayList allies = this.user.getTeamPokemonExcludeSelf();
         Iterator var2 = this.targets.iterator();

         PixelmonWrapper target;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            target = (PixelmonWrapper)var2.next();
         } while(!allies.contains(target));

         return true;
      }
   }

   public ArrayList createList() {
      ArrayList list = new ArrayList(1);
      list.add(this);
      return list;
   }

   public int compareTo(MoveChoice otherChoice) {
      if (otherChoice == null) {
         return 0;
      } else {
         return this.tier == otherChoice.tier ? (int)(this.weight - otherChoice.weight) : this.tier - otherChoice.tier;
      }
   }

   public String toString() {
      String s;
      if (this.isAttack()) {
         s = this.attack.getMove().getAttackName();
      } else {
         s = "Switch";
      }

      s = s + ", Tier " + this.tier + ", Weight " + this.weight;
      return s;
   }

   public static void checkBestChoice(MoveChoice choice, ArrayList bestChoices) {
      checkBestChoice(choice, bestChoices, false);
   }

   public static void checkBestChoice(MoveChoice choice, ArrayList bestChoices, boolean excludeStatus) {
      if (choice.tier != 0 && (!excludeStatus || !choice.isStatusMove())) {
         if (bestChoices.isEmpty()) {
            bestChoices.add(choice);
         } else {
            int compare = ((MoveChoice)bestChoices.get(0)).compareTo(choice);
            if (compare <= 0) {
               if (compare < 0) {
                  bestChoices.clear();
               }

               bestChoices.add(choice);
            }
         }

      }
   }

   public static float getMaxPriority(ArrayList choices) {
      float priority = 0.0F;
      boolean initialized = false;
      if (choices != null) {
         Iterator var3 = choices.iterator();

         while(var3.hasNext()) {
            MoveChoice userChoice = (MoveChoice)var3.next();
            if (userChoice.result != null) {
               priority = initialized ? Math.max(priority, userChoice.result.priority) : userChoice.result.priority;
               initialized = true;
            }
         }
      }

      return priority;
   }

   public static boolean canOutspeed(ArrayList opponentChoices, PixelmonWrapper pw, ArrayList userChoices) {
      float userPriority = getMaxPriority(userChoices);
      Iterator var4 = opponentChoices.iterator();

      MoveChoice opponentChoice;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         opponentChoice = (MoveChoice)var4.next();
      } while(!(opponentChoice.result.priority > userPriority) && (opponentChoice.result.priority != userPriority || pw.bc.getFirstMover(pw, opponentChoice.user) != opponentChoice.user));

      return true;
   }

   public static boolean canOHKO(ArrayList opponentChoices, PixelmonWrapper pw) {
      Iterator var2 = opponentChoices.iterator();

      MoveChoice opponentChoice;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         opponentChoice = (MoveChoice)var2.next();
      } while(!opponentChoice.isOffensiveMove() || !opponentChoice.targets.contains(pw) || opponentChoice.tier < 3);

      return true;
   }

   public static boolean canOutspeedAndOHKO(ArrayList opponentChoices, PixelmonWrapper pw, ArrayList userChoices) {
      float userPriority = getMaxPriority(userChoices);
      Iterator var4 = opponentChoices.iterator();

      MoveChoice opponentChoice;
      do {
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return false;
                  }

                  opponentChoice = (MoveChoice)var4.next();
               } while(!opponentChoice.isOffensiveMove());
            } while(!opponentChoice.targets.contains(pw));
         } while(opponentChoice.tier < 3);
      } while(!(opponentChoice.result.priority > userPriority) && (opponentChoice.result.priority != userPriority || pw.bc.getFirstMover(pw, opponentChoice.user) != opponentChoice.user));

      return true;
   }

   public static boolean canOutspeedAnd2HKO(ArrayList opponentChoices, PixelmonWrapper pw) {
      return canOutspeedAnd2HKO(opponentChoices, pw, (ArrayList)null);
   }

   public static boolean canOutspeedAnd2HKO(ArrayList opponentChoices, PixelmonWrapper pw, ArrayList userChoices) {
      return canOutspeedAndKO(2, opponentChoices, pw, userChoices);
   }

   public static boolean canOutspeedAndKO(int numTurns, ArrayList opponentChoices, PixelmonWrapper pw, ArrayList userChoices) {
      float userPriority = getMaxPriority(userChoices);
      int weightThreshold = 100 / numTurns;
      Iterator var6 = opponentChoices.iterator();

      MoveChoice opponentChoice;
      do {
         do {
            do {
               do {
                  do {
                     if (!var6.hasNext()) {
                        return false;
                     }

                     opponentChoice = (MoveChoice)var6.next();
                  } while(!opponentChoice.isOffensiveMove());
               } while(!opponentChoice.targets.contains(pw));

               if (opponentChoice.tier >= 3) {
                  return true;
               }
            } while(!(opponentChoice.weight >= (float)weightThreshold));
         } while(!opponentChoice.isMiddleTier());
      } while(!(opponentChoice.result.priority > userPriority) && (opponentChoice.result.priority != userPriority || pw.bc.getFirstMover(pw, opponentChoice.user) != opponentChoice.user));

      return true;
   }

   public static boolean canKOFromFull(ArrayList opponentChoices, PixelmonWrapper pw, int numTurns) {
      Iterator var3 = opponentChoices.iterator();

      MoveChoice opponentChoice;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         opponentChoice = (MoveChoice)var3.next();
      } while(!opponentChoice.isOffensiveMove() || !opponentChoice.targets.contains(pw) || opponentChoice.result.fullDamage * numTurns < pw.getMaxHealth());

      return true;
   }

   public static boolean hasSuccessfulAttackChoice(ArrayList choices, String... attackNames) {
      Iterator var2 = choices.iterator();

      MoveChoice choice;
      do {
         do {
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               choice = (MoveChoice)var2.next();
            } while(!choice.isAttack());
         } while(!choice.attack.isAttack(attackNames));
      } while(choice.result != null && choice.result.result == AttackResult.failed);

      return true;
   }

   public static ArrayList getAffectedChoices(MoveChoice choice, ArrayList opponentChoices) {
      ArrayList affectedChoices = new ArrayList(opponentChoices.size());
      if (choice.targets != null) {
         Iterator var3 = opponentChoices.iterator();

         while(var3.hasNext()) {
            MoveChoice opponentChoice = (MoveChoice)var3.next();
            if (opponentChoice.isAttack() && choice.targets.contains(opponentChoice.user)) {
               affectedChoices.add(opponentChoice);
            }
         }
      }

      return affectedChoices;
   }

   public static ArrayList getTargetedChoices(PixelmonWrapper pw, ArrayList opponentChoices) {
      ArrayList targetedChoices = new ArrayList(opponentChoices.size());
      Iterator var3 = opponentChoices.iterator();

      while(var3.hasNext()) {
         MoveChoice opponentChoice = (MoveChoice)var3.next();
         if (opponentChoice.targets != null && opponentChoice.targets.contains(pw)) {
            targetedChoices.add(opponentChoice);
         }
      }

      return targetedChoices;
   }

   public static boolean hasPriority(ArrayList... choices) {
      ArrayList[] var1 = choices;
      int var2 = choices.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ArrayList list = var1[var3];
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            MoveChoice choice = (MoveChoice)var5.next();
            if (choice.isAttack() && choice.attack.getMove().getPriority(choice.user) != 0) {
               return true;
            }
         }
      }

      return false;
   }

   public static ArrayList splitChoices(ArrayList pokemonList, ArrayList choices) {
      ArrayList splitList = new ArrayList(pokemonList.size());
      Iterator var3 = pokemonList.iterator();

      while(var3.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var3.next();
         ArrayList pokemonChoices = new ArrayList();
         Iterator var6 = choices.iterator();

         while(var6.hasNext()) {
            MoveChoice choice = (MoveChoice)var6.next();
            if (choice.user == pw) {
               pokemonChoices.add(choice);
            }
         }

         splitList.add(pokemonChoices);
      }

      return splitList;
   }

   public static ArrayList mergeChoices(ArrayList choices) {
      ArrayList mergeList = new ArrayList();
      Iterator var2 = choices.iterator();

      while(var2.hasNext()) {
         ArrayList choiceList = (ArrayList)var2.next();
         mergeList.addAll(choiceList);
      }

      return mergeList;
   }

   public static ArrayList createChoicesFromChoices(PixelmonWrapper pw, ArrayList choices, boolean includeAllies) {
      ArrayList newChoices = new ArrayList();
      Iterator var4 = choices.iterator();

      while(var4.hasNext()) {
         MoveChoice choice = (MoveChoice)var4.next();
         if (choice.isAttack()) {
            newChoices.addAll(choice.attack.createMoveChoices(pw, includeAllies));
         }
      }

      return newChoices;
   }

   public static float getMaxDamagePercent(PixelmonWrapper pw, ArrayList opponentChoices) {
      float damagePercent = 0.0F;
      Iterator var3 = opponentChoices.iterator();

      while(var3.hasNext()) {
         MoveChoice opponentChoice = (MoveChoice)var3.next();
         if (opponentChoice.isOffensiveMove() && opponentChoice.targets.contains(pw)) {
            if (opponentChoice.tier >= 3) {
               return 100.0F;
            }

            float effectivePercent = (float)(opponentChoice.result.damage / opponentChoice.targets.size());
            damagePercent = Math.max(damagePercent, effectivePercent);
         }
      }

      return damagePercent;
   }

   public static ArrayList createMoveChoicesFromList(ArrayList possibleAttacks, PixelmonWrapper pw) {
      ArrayList possibleChoices = new ArrayList();
      Iterator var3 = possibleAttacks.iterator();

      while(var3.hasNext()) {
         Attack possibleAttack = (Attack)var3.next();
         possibleAttack.createMoveChoices(pw, possibleChoices, false);
      }

      return possibleChoices;
   }

   public static boolean hasAttackCategory(ArrayList choices, AttackCategory category) {
      Iterator var2 = choices.iterator();

      MoveChoice choice;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         choice = (MoveChoice)var2.next();
      } while(!choice.isAttack() || choice.attack.getAttackCategory() != category);

      return true;
   }

   public static boolean hasOffensiveAttackType(ArrayList choices, EnumType type) {
      Iterator var2 = choices.iterator();

      MoveChoice choice;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         choice = (MoveChoice)var2.next();
      } while(!choice.isOffensiveMove() || choice.attack.getType() != type);

      return true;
   }

   public static boolean hasSpreadMove(ArrayList choices) {
      Iterator var1 = choices.iterator();

      MoveChoice choice;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         choice = (MoveChoice)var1.next();
      } while(!choice.isOffensiveMove() || !choice.attack.getMove().getTargetingInfo().hitsAll || !choice.attack.getMove().getTargetingInfo().hitsAdjacentFoe);

      return true;
   }

   public static float sumWeights(ArrayList choices) {
      float weightSum = 0.0F;

      MoveChoice choice;
      for(Iterator var2 = choices.iterator(); var2.hasNext(); weightSum += choice.weight) {
         choice = (MoveChoice)var2.next();
      }

      return weightSum;
   }

   public static boolean canBreakProtect(ArrayList opponents, ArrayList choices) {
      if (hasSuccessfulAttackChoice(choices, "Feint", "Hyperspace Fury", "Hyperspace Hole")) {
         return true;
      } else {
         Iterator var2 = opponents.iterator();

         PixelmonWrapper opponent;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            opponent = (PixelmonWrapper)var2.next();
         } while(!opponent.hasStatus(StatusType.Vanish));

         return true;
      }
   }

   public static boolean hasMove(ArrayList choices, String... moveNames) {
      Iterator var2 = choices.iterator();

      MoveChoice choice;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         choice = (MoveChoice)var2.next();
      } while(!choice.isAttack() || !choice.attack.isAttack(moveNames));

      return true;
   }
}
