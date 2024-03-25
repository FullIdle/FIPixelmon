package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayer;

public class CaptureMoonBall extends CaptureBase {
   private static final EnumSpecies[] INCREASED_CATCH_RATE;

   public CaptureMoonBall() {
      super(EnumPokeballs.MoonBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      return p2.isPokemon(INCREASED_CATCH_RATE) ? 4.0 : 1.0;
   }

   static {
      INCREASED_CATCH_RATE = new EnumSpecies[]{EnumSpecies.Nidoranfemale, EnumSpecies.Nidorina, EnumSpecies.Nidoqueen, EnumSpecies.Nidoranmale, EnumSpecies.Nidorino, EnumSpecies.Nidoking, EnumSpecies.Cleffa, EnumSpecies.Clefairy, EnumSpecies.Clefable, EnumSpecies.Igglybuff, EnumSpecies.Jigglypuff, EnumSpecies.Wigglytuff, EnumSpecies.Skitty, EnumSpecies.Delcatty, EnumSpecies.Munna, EnumSpecies.Musharna};
   }
}
