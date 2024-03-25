package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class MovesetEvent extends Event {
   public final Pokemon pokemon;
   public final Moveset moveset;

   protected MovesetEvent(Pokemon pokemon, Moveset moveset) {
      this.pokemon = pokemon;
      this.moveset = moveset;
   }

   public static class ForgotMoveEvent extends MovesetEvent {
      public final Attack forgottenAttack;

      public ForgotMoveEvent(Pokemon pokemon, Moveset moveset, Attack forgottenAttack) {
         super(pokemon, moveset);
         this.forgottenAttack = forgottenAttack;
      }
   }

   public static class LearntMoveEvent extends MovesetEvent {
      @Nullable
      public final Attack replacedAttack;
      public final Attack learntAttack;

      public LearntMoveEvent(Pokemon pokemon, Moveset moveset, Attack replacedAttack, Attack learntAttack) {
         super(pokemon, moveset);
         this.replacedAttack = replacedAttack;
         this.learntAttack = learntAttack;
      }
   }
}
