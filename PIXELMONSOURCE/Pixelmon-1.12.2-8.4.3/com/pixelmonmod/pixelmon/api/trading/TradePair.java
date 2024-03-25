package com.pixelmonmod.pixelmon.api.trading;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;

public class TradePair {
   public PokemonSpec offer;
   public PokemonSpec exchange;
   public String description;

   public TradePair(PokemonSpec offer, PokemonSpec exchangefor) {
      this.offer = offer;
      this.exchange = exchangefor;
   }

   public TradePair(PokemonSpec offer, PokemonSpec exchangeFor, String description) {
      this(offer, exchangeFor);
      this.description = description;
   }
}
