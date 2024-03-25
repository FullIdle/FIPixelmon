package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Forewarn extends AbilityBase {
   public static HashMap exceptionMoves = new HashMap();

   public Forewarn() {
      if (exceptionMoves.isEmpty()) {
         exceptionMoves.put("Crush Grip", 80);
         exceptionMoves.put("Dragon Rage", 80);
         exceptionMoves.put("Endeavor", 80);
         exceptionMoves.put("Flail", 80);
         exceptionMoves.put("Frustration", 80);
         exceptionMoves.put("Grass Knot", 80);
         exceptionMoves.put("Gyro Ball", 80);
         exceptionMoves.put("Hidden Power", 80);
         exceptionMoves.put("Low Kick", 80);
         exceptionMoves.put("Natural Gift", 80);
         exceptionMoves.put("Night Shade", 80);
         exceptionMoves.put("Psywave", 80);
         exceptionMoves.put("Return", 80);
         exceptionMoves.put("Reversal", 80);
         exceptionMoves.put("Seismic Toss", 80);
         exceptionMoves.put("Sonic Boom", 80);
         exceptionMoves.put("Trump Card", 80);
         exceptionMoves.put("Wring Out", 80);
         exceptionMoves.put("Counter", 120);
         exceptionMoves.put("Metal Burst", 120);
         exceptionMoves.put("Mirror Coat", 120);
         exceptionMoves.put("Eruption", 150);
         exceptionMoves.put("Water Spout", 150);
         exceptionMoves.put("Fissure", 160);
         exceptionMoves.put("Guillotine", 160);
         exceptionMoves.put("Horn Drill", 160);
         exceptionMoves.put("Sheer Cold", 160);
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      ArrayList strongestAttacks = new ArrayList();
      int highestPower = 0;
      ArrayList opponents = newPokemon.bc.getOpponentPokemon(newPokemon.getParticipant());
      Iterator var5 = opponents.iterator();

      while(var5.hasNext()) {
         PixelmonWrapper opponent = (PixelmonWrapper)var5.next();
         Moveset moveset = opponent.getMoveset();
         Attack[] var8 = moveset.attacks;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Attack a = var8[var10];
            if (a != null) {
               int basePower = this.getAttackBasePower(a);
               if (basePower > highestPower) {
                  strongestAttacks.clear();
                  highestPower = basePower;
               }

               if (basePower == highestPower) {
                  strongestAttacks.add(a);
               }
            }
         }
      }

      int r = RandomHelper.getRandomNumberBetween(0, strongestAttacks.size() - 1);
      Attack warnedAttack = (Attack)strongestAttacks.get(r);
      newPokemon.bc.sendToAll("pixelmon.abilities.forewarn", newPokemon.getNickname(), warnedAttack.getMove().getTranslatedName());
   }

   private int getAttackBasePower(Attack a) {
      if (a == null) {
         return 0;
      } else {
         String attackName = a.getMove().getAttackName();
         if (exceptionMoves.containsKey(attackName)) {
            return (Integer)exceptionMoves.get(attackName);
         } else {
            int basePower = a.getMove().getBasePower();
            if (basePower <= 0) {
               basePower = 1;
            }

            return basePower;
         }
      }
   }
}
