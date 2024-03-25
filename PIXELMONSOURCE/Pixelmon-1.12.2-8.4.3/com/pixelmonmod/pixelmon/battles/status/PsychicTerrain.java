package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Prankster;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import java.util.Iterator;
import javax.annotation.Nonnull;

public class PsychicTerrain extends Terrain {
   public PsychicTerrain() {
      super(StatusType.PsychicTerrain, "pixelmon.status.psychicterrain", "pixelmon.status.psychicterrainend");
   }

   public Terrain getNewInstance() {
      return new PsychicTerrain();
   }

   @Nonnull
   public EnumTerrain getTerrainType() {
      return EnumTerrain.PSYCHIC;
   }

   public EnumType getTypingForTerrain() {
      return EnumType.Psychic;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (this.affectsPokemon(pokemon) && Math.round(user.priority) > 0) {
         if (user.attack.isAttack("Perish Song", "Flower Shield", "Rototiller", "Pursuit")) {
            return false;
         } else if (!user.attack.getMove().getTargetingInfo().hitsAll && !user.attack.isAttack(EntryHazard.ENTRY_HAZARDS)) {
            boolean targetsOwnTeam = true;
            Iterator var4 = user.targets.iterator();

            while(var4.hasNext()) {
               PixelmonWrapper target = (PixelmonWrapper)var4.next();
               if (user.isOpponent(target)) {
                  targetsOwnTeam = false;
               }
            }

            if (targetsOwnTeam) {
               return false;
            } else {
               user.bc.sendToAll("pixelmon.battletext.movefailed");
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (this.affectsPokemon(user) && a.getType() == EnumType.Psychic) {
         power = (int)((double)power * 1.3);
      }

      return new int[]{power, accuracy};
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      Iterator var4;
      Attack a;
      if (this.affectsPokemon(user)) {
         if (Attack.hasOffensiveAttackType(user.getMoveset(), EnumType.Psychic)) {
            ++benefits;
         }

         var4 = target.getMoveset().iterator();

         label61:
         while(true) {
            do {
               do {
                  if (!var4.hasNext()) {
                     break label61;
                  }

                  a = (Attack)var4.next();
               } while(a == null);
            } while(a.getMove().getPriority(target) <= 0 && (a.getAttackCategory() != AttackCategory.STATUS || !(target.getAbility() instanceof Prankster)));

            ++benefits;
         }
      }

      if (this.affectsPokemon(target)) {
         var4 = user.getMoveset().iterator();

         while(true) {
            do {
               do {
                  if (!var4.hasNext()) {
                     return benefits;
                  }

                  a = (Attack)var4.next();
               } while(a == null);
            } while(a.getMove().getPriority(user) <= 0 && (a.getAttackCategory() != AttackCategory.STATUS || !(user.getAbility() instanceof Prankster)));

            --benefits;
         }
      } else {
         return benefits;
      }
   }
}
