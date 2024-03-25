package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class ElectricTerrain extends Terrain {
   public ElectricTerrain() {
      super(StatusType.ElectricTerrain, "pixelmon.status.electricterrain", "pixelmon.status.electricterrainend");
   }

   public Terrain getNewInstance() {
      return new ElectricTerrain();
   }

   @Nonnull
   public EnumTerrain getTerrainType() {
      return EnumTerrain.ELECTRIC;
   }

   public EnumType getTypingForTerrain() {
      return EnumType.Electric;
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (this.affectsPokemon(user) && a.getType() == EnumType.Electric) {
         power = (int)((double)power * 1.3);
      }

      return new int[]{power, accuracy};
   }

   public boolean stopsStatusChange(StatusType t, PixelmonWrapper target, PixelmonWrapper user) {
      if (this.affectsPokemon(target) && (t == StatusType.Sleep || t == StatusType.Yawn)) {
         if (user != target && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            target.bc.sendToAll("pixelmon.effect.effectfailed");
         }

         return true;
      } else {
         return false;
      }
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      if (this.affectsPokemon(target)) {
         List moveset = user.getBattleAI().getMoveset(target);
         if (Attack.hasOffensiveAttackType(moveset, EnumType.Electric)) {
            ++benefits;
         }

         if (target.hasStatus(StatusType.Yawn)) {
            ++benefits;
         }

         if (Attack.hasAttack(moveset, "Rest")) {
            --benefits;
         }

         Iterator var5 = moveset.iterator();

         while(true) {
            while(var5.hasNext()) {
               Attack move = (Attack)var5.next();
               Iterator var7 = move.getMove().effects.iterator();

               while(var7.hasNext()) {
                  EffectBase e = (EffectBase)var7.next();
                  if (e instanceof Sleep || e instanceof Yawn) {
                     --benefits;
                     break;
                  }
               }
            }

            return benefits;
         }
      } else {
         return benefits;
      }
   }
}
