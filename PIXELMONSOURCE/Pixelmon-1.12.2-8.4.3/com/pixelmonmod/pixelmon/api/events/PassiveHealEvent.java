package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PassiveHealEvent extends Event {
   private final EntityPlayerMP player;
   public Pokemon pokemon;

   public PassiveHealEvent(EntityPlayerMP player, Pokemon pokemon) {
      this.player = player;
      this.pokemon = pokemon;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public static class Post extends PassiveHealEvent {
      private final boolean revive;
      private final boolean status;
      private final boolean heal;

      public Post(EntityPlayerMP player, Pokemon pokemon, boolean revive, boolean status, boolean heal) {
         super(player, pokemon);
         this.revive = revive;
         this.status = status;
         this.heal = heal;
      }

      public boolean wasRevived() {
         return this.revive;
      }

      public boolean wasCuredOfStatus() {
         return this.status;
      }

      public boolean wasHealed() {
         return this.heal;
      }
   }

   @Cancelable
   public static class Pre extends PassiveHealEvent {
      private boolean revive;
      private boolean status;
      private boolean heal;

      public Pre(EntityPlayerMP player, Pokemon pokemon, boolean revive, boolean status, boolean heal) {
         super(player, pokemon);
         this.revive = revive;
         this.status = status;
         this.heal = heal;
      }

      public boolean willRevive() {
         return this.revive;
      }

      public void setWillRevive(boolean revive) {
         this.revive = revive;
      }

      public boolean willCureStatus() {
         return this.status;
      }

      public void setWillCureStatus(boolean status) {
         this.status = status;
      }

      public boolean willHeal() {
         return this.heal;
      }

      public void setWillHeal(boolean heal) {
         this.heal = heal;
      }
   }
}
