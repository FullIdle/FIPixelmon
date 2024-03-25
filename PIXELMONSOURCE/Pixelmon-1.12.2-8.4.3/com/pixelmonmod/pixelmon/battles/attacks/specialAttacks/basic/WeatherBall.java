package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;
import com.pixelmonmod.pixelmon.battles.status.Rainy;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class WeatherBall extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.attack.getActualMove().isAttack("Weather Ball")) {
         return AttackResult.proceed;
      } else {
         user.attack.overrideType(this.getOverrideType(user));
         if (user.attack.getType() != EnumType.Normal) {
            user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
         }

         return AttackResult.proceed;
      }
   }

   private EnumType getOverrideType(PixelmonWrapper user) {
      Weather weather = user.bc.globalStatusController.getWeather();
      EnumType type;
      if (weather instanceof Sunny) {
         type = EnumType.Fire;
      } else if (weather instanceof Rainy) {
         type = EnumType.Water;
      } else if (weather instanceof Sandstorm) {
         type = EnumType.Rock;
      } else if (weather instanceof Hail) {
         type = EnumType.Ice;
      } else {
         type = EnumType.Normal;
      }

      return type;
   }
}
