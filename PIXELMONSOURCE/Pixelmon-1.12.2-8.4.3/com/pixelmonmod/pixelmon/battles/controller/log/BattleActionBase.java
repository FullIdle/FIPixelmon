package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public abstract class BattleActionBase {
   public int turn;
   public int pokemonPosition;
   public PixelmonWrapper pokemon;

   public BattleActionBase(int turn, int pokemonPosition, PixelmonWrapper pokemon) {
      this.turn = turn;
      this.pokemonPosition = pokemonPosition;
      this.pokemon = pokemon;
   }

   public abstract ActionType actionType();

   public PixelmonWrapper getPixelmon() {
      return this.pokemon;
   }

   public BattleParticipant getParticipant() {
      return this.pokemon.getParticipant();
   }

   public static enum ActionType {
      ATTACK,
      SWITCH,
      BAG,
      FLEE;
   }
}
