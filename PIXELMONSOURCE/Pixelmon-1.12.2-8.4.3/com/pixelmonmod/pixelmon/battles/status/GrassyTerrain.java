package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class GrassyTerrain extends Terrain {
   private static final transient String[] groundMoves = new String[]{"Bulldoze", "Earthquake", "Magnitude"};

   public GrassyTerrain() {
      super(StatusType.GrassyTerrain, "pixelmon.status.grassyterrain", "pixelmon.status.grassyterrainend");
   }

   public Terrain getNewInstance() {
      return new GrassyTerrain();
   }

   @Nonnull
   public EnumTerrain getTerrainType() {
      return EnumTerrain.GRASSY;
   }

   public EnumType getTypingForTerrain() {
      return EnumType.Grass;
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (this.affectsPokemon(user) && a.getType() == EnumType.Grass) {
         power = (int)((double)power * 1.3);
      }

      if (a.isAttack(groundMoves)) {
         power = (int)((double)power * 0.5);
      }

      return new int[]{power, accuracy};
   }

   public void applyRepeatedEffect(GlobalStatusController gsc) {
      super.applyRepeatedEffect(gsc);
      if (gsc.hasStatus(this.type)) {
         Iterator var2 = gsc.bc.getDefaultTurnOrder().iterator();

         while(var2.hasNext()) {
            PixelmonWrapper p = (PixelmonWrapper)var2.next();
            if (!p.hasFullHealth() && !p.isFainted() && !p.isAirborne() && !p.hasStatus(StatusType.HealBlock)) {
               p.healByPercent(6.25F);
               p.bc.sendToAll("pixelmon.effect.restorehealth", p.getNickname());
            }
         }
      }

   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      List moveset = user.getBattleAI().getMoveset(target);
      if (this.affectsPokemon(target)) {
         if (Attack.hasOffensiveAttackType(moveset, EnumType.Grass)) {
            ++benefits;
         }

         ++benefits;
      }

      if (Attack.hasAttack(moveset, groundMoves)) {
         --benefits;
      }

      return benefits;
   }
}
