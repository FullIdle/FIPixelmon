package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PokeBallRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"ball", "ba"});
   private EnumPokeballs ball;

   public PokeBallRequirement() {
      super(KEYS);
   }

   public PokeBallRequirement(EnumPokeballs ball) {
      this();
      this.ball = ball;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         return args.length == 0 ? Collections.singletonList(this.createInstance(EnumPokeballs.PokeBall)) : Collections.singletonList(this.createInstance(EnumPokeballs.getPokeballFromString(args[1])));
      }
   }

   public Requirement createInstance(EnumPokeballs value) {
      return new PokeBallRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return Objects.equals(pixelmon.getCaughtBall(), this.ball);
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.setCaughtBall(this.ball);
   }

   public EnumPokeballs getValue() {
      return this.ball;
   }
}
