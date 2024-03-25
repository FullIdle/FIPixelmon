package com.pixelmonmod.pixelmon.entities.pokeballs.captures;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CaptureRepeatBall extends CaptureBase {
   public CaptureRepeatBall() {
      super(EnumPokeballs.RepeatBall);
   }

   public double getBallBonus(EnumPokeballs type, EntityPlayer thrower, Pokemon p2, EnumPokeBallMode mode) {
      PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)thrower);
      return storage.pokedex.hasCaught(p2.getSpecies().getNationalPokedexInteger()) ? 3.5 : 1.0;
   }
}
