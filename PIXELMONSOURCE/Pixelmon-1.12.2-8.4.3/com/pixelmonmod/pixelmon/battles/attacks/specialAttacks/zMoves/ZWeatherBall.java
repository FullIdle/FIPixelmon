package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.zMoves;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.ZMove;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.WeatherBall;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Collections;
import net.minecraft.util.text.TextComponentTranslation;

public class ZWeatherBall extends WeatherBall {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.attack.getActualMove().isAttack("Weather Ball")) {
         return AttackResult.proceed;
      } else {
         String attackName;
         if (user.attack.getType() == EnumType.Fire) {
            attackName = "Inferno Overdrive";
         } else if (user.attack.getType() == EnumType.Water) {
            attackName = "Hydro Vortex";
         } else if (user.attack.getType() == EnumType.Rock) {
            attackName = "Continental Crush";
         } else {
            if (user.attack.getType() != EnumType.Ice) {
               return AttackResult.proceed;
            }

            attackName = "Subzero Slammer";
         }

         Attack calledAttack = new Attack("Weather Ball");
         user.skipZConvert = true;
         user.zMove = new ZMove((String)null, attackName, 160, Collections.emptyList(), Collections.emptyList());
         user.bc.sendToAll("pixelmon.effect.zbreakneck_blitz", new TextComponentTranslation("attack." + attackName.toLowerCase().replace(" ", "_") + ".name", new Object[0]));
         user.attack.moveResult.damage = calledAttack.moveResult.damage;
         user.attack.moveResult.fullDamage = calledAttack.moveResult.fullDamage;
         user.attack.moveResult.accuracy = calledAttack.moveResult.accuracy;
         return AttackResult.ignore;
      }
   }
}
