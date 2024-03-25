package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class DrySkin extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.bc != null) {
         Weather weather = pokemon.bc.globalStatusController.getWeather();
         if (weather instanceof Rainy && !pokemon.hasFullHealth()) {
            pokemon.bc.sendToAll("pixelmon.abilities.dryskinrain", pokemon.getNickname());
            pokemon.healByPercent(12.5F);
         }

         if (weather instanceof Sunny && pokemon.isAlive()) {
            pokemon.bc.sendToAll("pixelmon.abilities.dryskinsun", pokemon.getNickname());
            pokemon.doBattleDamage(pokemon, (float)pokemon.getPercentMaxHealth(12.5F), DamageTypeEnum.ABILITY);
         }

      }
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType() == EnumType.Water) {
         if (pokemon.hasFullHealth()) {
            user.bc.sendToAll("pixelmon.abilities.dryskinnegate", pokemon.getNickname());
         } else {
            int healAmount = pokemon.getPercentMaxHealth(25.0F);
            pokemon.healEntityBy(healAmount);
            user.bc.sendToAll("pixelmon.abilities.dryskinrain", pokemon.getNickname());
            MoveResults var10000 = a.moveResult;
            var10000.weightMod -= pokemon.getHealPercent((float)healAmount);
         }

         return false;
      } else {
         return true;
      }
   }

   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      return a.getType() == EnumType.Fire ? (int)((double)damage * 1.25) : damage;
   }
}
