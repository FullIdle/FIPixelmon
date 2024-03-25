package com.pixelmonmod.pixelmon.api.events.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import javax.annotation.CheckForNull;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RandomizeRaidEvent extends Event {
   public final EntityDen den;

   public RandomizeRaidEvent(EntityDen den) {
      this.den = den;
   }

   @Cancelable
   public static class ChooseSpecies extends RandomizeRaidEvent {
      private RaidData raid;

      public ChooseSpecies(EntityDen den, RaidData raid) {
         super(den);
         this.raid = raid;
      }

      @CheckForNull
      public RaidData getRaid() {
         return this.raid;
      }

      public void setRaid(RaidData raid) {
         this.raid = raid;
      }

      public void clearRaid() {
         this.raid = null;
      }
   }

   @Cancelable
   public static class ChooseStarLevel extends RandomizeRaidEvent {
      private int stars;

      public ChooseStarLevel(EntityDen den, int stars) {
         super(den);
         this.stars = stars;
      }

      public int getStars() {
         return this.stars;
      }

      public void setStars(int stars) {
         if (stars >= 1 && stars <= 5) {
            this.stars = stars;
         } else {
            Pixelmon.LOGGER.warn("RandomizeRaidEvent.ChooseStarLevel: cannot set raid star level below 1 or above 5!");
         }

      }
   }

   @Cancelable
   public static class RollChance extends RandomizeRaidEvent {
      private float chance;

      public RollChance(EntityDen den, float chance) {
         super(den);
         this.chance = chance;
      }

      public float getChance() {
         return this.chance;
      }

      public void setChance(float chance) {
         this.chance = chance;
      }
   }
}
