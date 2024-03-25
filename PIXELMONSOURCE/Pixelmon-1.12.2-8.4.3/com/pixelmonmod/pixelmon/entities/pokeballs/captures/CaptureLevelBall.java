package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureLevelBall extends CaptureBase {
   public CaptureLevelBall() {
      super(EnumPokeballs.LevelBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      double ballBonus = this.pokeball.getBallBonus();
      if (mode == EnumPokeBallMode.battle) {
         BattleControllerBase bc = BattleRegistry.getBattle(thrower);
         if (bc == null) {
            return ballBonus;
         } else {
            int ownerPokemonLevel = 0;
            Iterator var9 = bc.participants.iterator();

            while(true) {
               BattleParticipant p;
               do {
                  if (!var9.hasNext()) {
                     int targetLevel = p2.getLevel();
                     if (ownerPokemonLevel > 4 * targetLevel) {
                        ballBonus = 8.0;
                        return ballBonus;
                     } else {
                        if (ownerPokemonLevel > 2 * targetLevel) {
                           ballBonus = 4.0;
                        } else if (ownerPokemonLevel > targetLevel) {
                           ballBonus = 2.0;
                           return ballBonus;
                        }

                        return ballBonus;
                     }
                  }

                  p = (BattleParticipant)var9.next();
               } while(p.getEntity() != thrower);

               PixelmonWrapper pw;
               for(Iterator var11 = p.controlledPokemon.iterator(); var11.hasNext(); ownerPokemonLevel = Math.max(ownerPokemonLevel, pw.getLevelNum())) {
                  pw = (PixelmonWrapper)var11.next();
               }
            }
         }
      } else {
         return ballBonus;
      }
   }
}
