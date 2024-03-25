package com.pixelmonmod.pixelmon.client.gui.battles;

import java.util.UUID;

public class EvoInfo {
   public UUID pokemonUUID;
   public String evolveInto;

   public EvoInfo(UUID pokemonUUID, String evolveInto) {
      this.pokemonUUID = pokemonUUID;
      this.evolveInto = evolveInto;
   }
}
