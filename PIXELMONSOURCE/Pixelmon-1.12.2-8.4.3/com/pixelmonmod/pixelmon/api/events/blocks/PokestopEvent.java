package com.pixelmonmod.pixelmon.api.events.blocks;

import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class PokestopEvent extends Event {
   private final EntityPokestop pokestop;

   public PokestopEvent(EntityPokestop pokestop) {
      this.pokestop = pokestop;
   }

   public EntityPokestop getPokestop() {
      return this.pokestop;
   }

   public static class Collide extends PokestopEvent {
      private final Entity entity;

      public Collide(EntityPokestop pokestop, Entity entity) {
         super(pokestop);
         this.entity = entity;
      }

      public Entity getPlayer() {
         return this.entity;
      }
   }

   public static class Interact extends PokestopEvent {
      private final EntityPlayer player;
      private final boolean rightClick;

      public Interact(EntityPokestop pokestop, EntityPlayer player, boolean rightClick) {
         super(pokestop);
         this.player = player;
         this.rightClick = rightClick;
      }

      public EntityPlayer getPlayer() {
         return this.player;
      }

      public boolean wasRightClick() {
         return this.rightClick;
      }

      public boolean wasLeftClick() {
         return !this.rightClick;
      }
   }
}
