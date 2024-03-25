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

public class CaptureLoveBall extends CaptureBase {
   public CaptureLoveBall() {
      super(EnumPokeballs.LoveBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      double ballBonus = type.getBallBonus();
      if (mode == EnumPokeBallMode.battle) {
         BattleControllerBase bc = BattleRegistry.getBattle(thrower);
         if (bc == null) {
            return ballBonus;
         }

         Iterator var8 = bc.participants.iterator();

         while(var8.hasNext()) {
            BattleParticipant p = (BattleParticipant)var8.next();
            if (p.getEntity() == thrower) {
               Iterator var10 = p.controlledPokemon.iterator();

               PixelmonWrapper pw;
               do {
                  if (!var10.hasNext()) {
                     return ballBonus;
                  }

                  pw = (PixelmonWrapper)var10.next();
               } while(!p2.getSpecies().getPokemonName().equals(pw.getPokemonName()) || !p2.getGender().isCompatible(pw.getGender()));

               return 8.0;
            }
         }
      }

      return ballBonus;
   }
}
