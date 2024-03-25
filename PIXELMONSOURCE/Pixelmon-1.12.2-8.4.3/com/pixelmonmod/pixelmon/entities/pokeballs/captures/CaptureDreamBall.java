package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureDreamBall extends CaptureBase {
   public CaptureDreamBall() {
      super(EnumPokeballs.DreamBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      double ballBonus = type.getBallBonus();
      if (mode == EnumPokeBallMode.battle) {
         BattleControllerBase bc = BattleRegistry.getBattle(thrower);
         if (bc == null) {
            return ballBonus;
         }

         EntityPixelmon ep = p2.getPixelmonIfExists();
         if (ep != null && ep.getPixelmonWrapper().hasStatus(StatusType.Sleep)) {
            return 4.0;
         }
      }

      return ballBonus;
   }
}
