package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PassivePoisonEvent extends Event {
   public static class Post extends PassivePoisonEvent {
      private final EntityPlayerMP player;
      private final Pokemon pokemon;

      public Post(EntityPlayerMP player, Pokemon pokemon) {
         this.player = player;
         this.pokemon = pokemon;
      }

      public EntityPlayerMP getPlayer() {
         return this.player;
      }

      public Pokemon getPokemon() {
         return this.pokemon;
      }
   }

   @Cancelable
   public static class Pre extends PassivePoisonEvent {
      private final EntityPlayerMP player;
      private final Pokemon pokemon;

      public Pre(EntityPlayerMP player, Pokemon pokemon) {
         this.player = player;
         this.pokemon = pokemon;
      }

      public EntityPlayerMP getPlayer() {
         return this.player;
      }

      public Pokemon getPokemon() {
         return this.pokemon;
      }
   }
}
