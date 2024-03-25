package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.projectiles.EntityHook;
import com.pixelmonmod.pixelmon.enums.items.EnumRodType;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class FishingEvent extends Event {
   public final EntityPlayerMP player;
   public final EntityHook fishHook;

   protected FishingEvent(EntityPlayerMP player, EntityHook fishHook) {
      this.player = player;
      this.fishHook = fishHook;
   }

   public EnumRodType getRodType() {
      return this.fishHook.rodType;
   }

   public static class Reel extends FishingEvent {
      public final Optional optEntity;

      public Reel(EntityPlayerMP player, EntityHook fishHook, Entity entity) {
         super(player, fishHook);
         this.optEntity = entity == null ? Optional.empty() : Optional.of(entity);
      }

      public boolean isPokemon() {
         return this.optEntity.isPresent() && this.optEntity.get() instanceof EntityPixelmon;
      }
   }

   @Cancelable
   public static class Catch extends FishingEvent {
      public final SpawnAction plannedSpawn;
      private int ticksTillEscape;
      private int displayedMarks;

      public Catch(EntityPlayerMP player, EntityHook fishHook, SpawnAction plannedSpawn, int ticksTillEscape, int displayedMarks) {
         super(player, fishHook);
         this.plannedSpawn = plannedSpawn;
         this.ticksTillEscape = ticksTillEscape;
         this.displayedMarks = displayedMarks;
      }

      public int getDisplayedMarks() {
         return this.displayedMarks;
      }

      public void setDisplayedMarks(int displayedMarks) {
         if (displayedMarks < 0) {
            displayedMarks = 0;
         }

         this.displayedMarks = displayedMarks;
      }

      public int getTicksTillEscape() {
         return this.ticksTillEscape;
      }

      public void setTicksTillEscape(int ticksTillEscape) {
         if (ticksTillEscape < 0 && ticksTillEscape != -1) {
            ticksTillEscape = 0;
         }

         this.ticksTillEscape = ticksTillEscape;
      }
   }

   public static class Cast extends FishingEvent {
      private int ticksUntilCatch;
      private float chanceOfNothing;

      public Cast(EntityPlayerMP player, EntityHook fishHook, int ticksUntilCatch, float chanceOfNothing) {
         super(player, fishHook);
         this.ticksUntilCatch = ticksUntilCatch;
         this.chanceOfNothing = chanceOfNothing;
      }

      public int getTicksUntilCatch() {
         return this.ticksUntilCatch;
      }

      public void setTicksUntilCatch(int ticksUntilCatch) {
         if (ticksUntilCatch <= 0) {
            ticksUntilCatch = 1;
         }

         this.ticksUntilCatch = ticksUntilCatch;
      }

      public float getChanceOfNothing() {
         return this.chanceOfNothing;
      }

      public void setChanceOfNothing(float chanceOfNothing) {
         this.chanceOfNothing = MathHelper.func_76131_a(chanceOfNothing, 0.0F, 1.0F);
      }
   }
}
