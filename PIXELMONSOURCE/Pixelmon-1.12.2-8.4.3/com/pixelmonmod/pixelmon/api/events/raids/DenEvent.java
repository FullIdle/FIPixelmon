package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.entities.EntityDen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class DenEvent extends Event {
   private final EntityDen den;

   public DenEvent(EntityDen den) {
      this.den = den;
   }

   public EntityDen getDen() {
      return this.den;
   }

   public static class Collide extends DenEvent {
      private final Entity entity;

      public Collide(EntityDen den, Entity entity) {
         super(den);
         this.entity = entity;
      }

      public Entity getPlayer() {
         return this.entity;
      }
   }

   @Cancelable
   public static class Interact extends DenEvent {
      private final EntityPlayer player;
      private final boolean rightClick;

      public Interact(EntityDen den, EntityPlayer player, boolean rightClick) {
         super(den);
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
