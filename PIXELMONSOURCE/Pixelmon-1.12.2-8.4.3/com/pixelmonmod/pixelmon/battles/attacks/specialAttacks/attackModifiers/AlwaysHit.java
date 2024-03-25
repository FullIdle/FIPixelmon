package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.NoGuard;

public class AlwaysHit extends AttackModifierBase {
   public StatusType weather = null;

   public AlwaysHit(Value... values) {
      if (values[0] != null) {
         if (values[0].stringValue.toLowerCase().contains("rain")) {
            this.weather = StatusType.Rainy;
         } else if (values[0].stringValue.toLowerCase().contains("hail")) {
            this.weather = StatusType.Hail;
         } else if (values[0].stringValue.toLowerCase().contains("sandstorm")) {
            this.weather = StatusType.Sandstorm;
         } else if (values[0].stringValue.toLowerCase().contains("sun")) {
            this.weather = StatusType.Sunny;
         }
      }

   }

   public boolean cantMiss(PixelmonWrapper user) {
      Weather weather = user.bc.globalStatusController.getWeather();
      if (weather == null) {
         return false;
      } else if (this.weather == null) {
         return true;
      } else if (weather.type == this.weather) {
         return true;
      } else if (weather instanceof Sunny && user.attack.isAttack("Thunder", "Hurricane") && !user.getBattleAbility().isAbility(NoGuard.class)) {
         user.attack.moveAccuracy = 50;
         return false;
      } else {
         return false;
      }
   }
}
