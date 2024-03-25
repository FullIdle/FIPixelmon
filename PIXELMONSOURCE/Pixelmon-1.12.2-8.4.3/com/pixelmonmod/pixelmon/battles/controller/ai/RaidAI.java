package com.pixelmonmod.pixelmon.battles.controller.ai;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.raids.RaidGovernor;
import com.pixelmonmod.pixelmon.battles.status.ProtectVariation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RaidAI extends TacticalAI {
   private final RaidPixelmonParticipant rpp;
   private final RaidGovernor governor;

   public RaidAI(RaidGovernor governor, RaidPixelmonParticipant participant) {
      super(participant);
      this.rpp = participant;
      this.governor = governor;
   }

   protected ArrayList getAttackChoices(PixelmonWrapper pw) {
      ArrayList choices = new ArrayList();
      List moveset = this.governor.getMoves();
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

   public ArrayList getTopNAttackChoices(int count, PixelmonWrapper pw) {
      ArrayList choices = this.getWeightedAttackChoices(pw);
      choices.removeIf((choice) -> {
         return this.isProtect(choice.attack);
      });
      ArrayList bestChoices = this.getBestChoices(choices);
      return this.pickTopNBestChoices(count, choices, bestChoices);
   }

   protected boolean isProtect(Attack attack) {
      Iterator var2 = attack.getMove().effects.iterator();

      EffectBase effect;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         effect = (EffectBase)var2.next();
      } while(!(effect instanceof ProtectVariation));

      return true;
   }

   protected ArrayList pickTopNBestChoices(int count, ArrayList choices, ArrayList bestChoices) {
      ArrayList finalChoices = new ArrayList();

      for(int i = 0; i < count; ++i) {
         MoveChoice choice;
         if (bestChoices.isEmpty()) {
            if (choices.isEmpty()) {
               break;
            }

            choice = (MoveChoice)RandomHelper.getRandomElementFromList(choices);
            choices.remove(choice);
            bestChoices.remove(choice);
            finalChoices.add(choice);
         } else if (bestChoices.size() > 1 && ((MoveChoice)bestChoices.get(0)).isMiddleTier()) {
            float totalWeight = 0.0F;

            MoveChoice choice;
            for(Iterator var7 = bestChoices.iterator(); var7.hasNext(); totalWeight += choice.weight) {
               choice = (MoveChoice)var7.next();
            }

            float random = RandomHelper.getRandomNumberBetween(0.0F, totalWeight);
            float counter = 0.0F;
            boolean chosen = false;
            Iterator iterator = bestChoices.iterator();

            while(iterator.hasNext()) {
               MoveChoice choice = (MoveChoice)iterator.next();
               counter += choice.weight;
               if (counter >= random) {
                  choices.remove(choice);
                  iterator.remove();
                  finalChoices.add(choice);
                  chosen = true;
               }
            }

            if (!chosen) {
               MoveChoice choice = (MoveChoice)bestChoices.get(bestChoices.size() - 1);
               choices.remove(choice);
               bestChoices.remove(choice);
               finalChoices.add(choice);
            }
         } else {
            choice = (MoveChoice)RandomHelper.getRandomElementFromList(bestChoices);
            choices.remove(choice);
            bestChoices.remove(choice);
            finalChoices.add(choice);
         }
      }

      return finalChoices;
   }
}
