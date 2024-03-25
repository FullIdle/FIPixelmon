package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.blocks.ranch.RanchBounds;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityBreeding;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

public abstract class BreedEvent extends Event {
   public final UUID owner;
   public final TileEntityRanchBlock ranch;

   private BreedEvent(UUID owner, TileEntityRanchBlock ranch) {
      this.owner = owner;
      this.ranch = ranch;
   }

   // $FF: synthetic method
   BreedEvent(UUID x0, TileEntityRanchBlock x1, Object x2) {
      this(x0, x1);
   }

   @Cancelable
   @HasResult
   public static class AddPokemon extends BreedEvent {
      public final EntityPlayerMP player;
      public final Pokemon pokemon;

      public AddPokemon(EntityPlayerMP owner, TileEntityRanchBlock ranch, Pokemon pokemon) {
         super(owner.func_110124_au(), ranch, null);
         this.player = owner;
         this.pokemon = pokemon;
      }
   }

   @Cancelable
   public static class CollectEgg extends BreedEvent {
      private Pokemon egg;

      public CollectEgg(UUID owner, TileEntityRanchBlock ranch, Pokemon egg) {
         super(owner, ranch, null);
         this.egg = egg;
      }

      public void setEgg(Pokemon egg) {
         if (egg != null) {
            this.egg = egg;
         }

      }

      public Pokemon getEgg() {
         return this.egg;
      }
   }

   @Cancelable
   public static class MakeEgg extends BreedEvent {
      private Pokemon egg;
      public final Pokemon parent1;
      public final Pokemon parent2;

      public MakeEgg(UUID owner, TileEntityRanchBlock ranch, Pokemon egg, Pokemon parent1, Pokemon parent2) {
         super(owner, ranch, null);
         this.egg = egg;
         this.parent1 = parent1;
         this.parent2 = parent2;
      }

      public Pokemon getEgg() {
         return this.egg;
      }

      public void setEgg(Pokemon egg) {
         if (egg != null) {
            this.egg = egg;
         }

      }
   }

   public static class BreedingLevelChanged extends BreedEvent {
      public final EntityBreeding pokemon;
      public final int oldLevel;
      private int newLevel;

      public BreedingLevelChanged(UUID owner, TileEntityRanchBlock ranch, EntityBreeding pokemon, int oldLevel, int newLevel) {
         super(owner, ranch, null);
         this.pokemon = pokemon;
         this.oldLevel = oldLevel;
         this.newLevel = newLevel;
      }

      public short getNewLevel() {
         return (short)this.newLevel;
      }

      public void setNewLevel(int newLevel) {
         if (newLevel < 0) {
            newLevel = 0;
         } else if (newLevel > 5) {
            newLevel = 5;
         }

         this.newLevel = newLevel;
      }
   }

   public static class EnvironmentStrength extends BreedEvent {
      public final EntityBreeding pokemon;
      public final RanchBounds ranchBounds;
      public float breedingStrength;

      public EnvironmentStrength(UUID owner, TileEntityRanchBlock ranch, EntityBreeding pokemon, RanchBounds ranchBounds, float breedingStrength) {
         super(owner, ranch, null);
         this.pokemon = pokemon;
         this.ranchBounds = ranchBounds;
         this.breedingStrength = breedingStrength;
      }
   }

   public static class BreedingTicks extends BreedEvent {
      public final EntityBreeding pokemon;
      private int breedingTicks;
      private final boolean ovalCharm;

      public BreedingTicks(UUID owner, TileEntityRanchBlock ranch, EntityBreeding pokemon, int breedingTicks, boolean ovalCharm) {
         super(owner, ranch, null);
         this.pokemon = pokemon;
         this.breedingTicks = breedingTicks;
         this.ovalCharm = ovalCharm;
      }

      public void setBreedingTicks(int newBreedingTicks) {
         this.breedingTicks = newBreedingTicks < 20 ? 20 : newBreedingTicks;
      }

      public int getBreedingTicks() {
         return this.breedingTicks;
      }

      public boolean isOvalCharm() {
         return this.ovalCharm;
      }
   }
}
