package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.DrySkin;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Hydration;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.IgnoreWeather;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RainDish;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SwiftSwim;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Iterator;
import java.util.List;

public class Rainy extends Weather {
   public Rainy() {
      this(5);
   }

   public Rainy(boolean extreme) {
      this(-1);
      this.extreme = extreme;
      this.langContinue = "pixelmon.status.extremelyheavyrain";
   }

   public Rainy(int turnsToGo) {
      super(StatusType.Rainy, turnsToGo, EnumHeldItems.dampRock, "pixelmon.effect.raining", "pixelmon.status.heavyrain", "pixelmon.status.rainstopped", false);
   }

   protected Weather getNewInstance(int turns) {
      return new Rainy(turns);
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Water) {
         power = (int)((double)power * 1.5);
      } else if (a.getType() == EnumType.Fire) {
         power = (int)((double)power * 0.5);
      }

      return new int[]{power, accuracy};
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      Attack attack = user.attack;
      if (this.extreme && Attack.dealsDamage(attack) && attack.getType() == EnumType.Fire) {
         Iterator var4 = pokemon.bc.getActivePokemon().iterator();

         PixelmonWrapper pw;
         do {
            if (!var4.hasNext()) {
               return true;
            }

            pw = (PixelmonWrapper)var4.next();
         } while(!(pw.getBattleAbility() instanceof IgnoreWeather));

         return false;
      } else {
         return false;
      }
   }

   protected int countBenefits(PixelmonWrapper user, PixelmonWrapper target) {
      int benefits = 0;
      AbilityBase ability = target.getBattleAbility();
      if (ability instanceof DrySkin || ability instanceof Hydration || ability instanceof RainDish || ability instanceof SwiftSwim) {
         ++benefits;
      }

      List moveset = user.getBattleAI().getMoveset(target);
      if (Attack.hasOffensiveAttackType(moveset, EnumType.Water) || Attack.hasAttack(moveset, "Weather Ball")) {
         ++benefits;
      }

      if (Attack.hasAttack(moveset, "Hurricane")) {
         ++benefits;
      }

      if (Attack.hasAttack(moveset, "Thunder")) {
         ++benefits;
      }

      if (Attack.hasOffensiveAttackType(moveset, EnumType.Fire)) {
         --benefits;
      }

      if (Attack.hasAttack(moveset, "SolarBeam")) {
         --benefits;
      }

      if (Attack.hasAttack(moveset, "Moonlight", "Morning Sun", "Synthesis")) {
         --benefits;
      }

      return benefits;
   }
}
