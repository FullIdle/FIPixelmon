package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PokeballEffectEvent extends Event {
   public final EntityPokeBall pokeBall;

   private PokeballEffectEvent(EntityPokeBall pokeBall) {
      this.pokeBall = pokeBall;
   }

   // $FF: synthetic method
   PokeballEffectEvent(EntityPokeBall x0, Object x1) {
      this(x0);
   }

   @Cancelable
   public static class StartCaptureEffect extends PokeballEffectEvent {
      public StartCaptureEffect(EntityPokeBall pokeBall) {
         super(pokeBall, null);
      }
   }

   @Cancelable
   public static class SuccessfullCaptureEffect extends PokeballEffectEvent {
      public SuccessfullCaptureEffect(EntityPokeBall pokeBall) {
         super(pokeBall, null);
      }
   }

   @Cancelable
   public static class SentOutEffect extends PokeballEffectEvent {
      public SentOutEffect(EntityPokeBall pokeBall) {
         super(pokeBall, null);
      }
   }
}
