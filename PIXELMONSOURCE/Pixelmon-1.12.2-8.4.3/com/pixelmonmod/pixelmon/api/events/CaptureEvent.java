package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class CaptureEvent extends Event {
   public final EntityPlayerMP player;
   protected EntityPixelmon pokemon;
   public final EntityPokeBall pokeball;

   private CaptureEvent(EntityPlayerMP player, EntityPixelmon pokemon, EntityPokeBall pokeball) {
      this.player = player;
      this.pokemon = pokemon;
      this.pokeball = pokeball;
   }

   public EntityPixelmon getPokemon() {
      return this.pokemon;
   }

   public void setPokemon(EntityPixelmon pokemon) {
      if (pokemon != null) {
         this.pokemon = pokemon;
      }
   }

   // $FF: synthetic method
   CaptureEvent(EntityPlayerMP x0, EntityPixelmon x1, EntityPokeBall x2, Object x3) {
      this(x0, x1, x2);
   }

   public static class FailedRaidCapture extends CaptureEvent {
      private final Pokemon raidPokemon;
      private final RaidData raid;

      public FailedRaidCapture(EntityPlayerMP player, Pokemon raidPokemon, RaidData raid) {
         super(player, (EntityPixelmon)null, (EntityPokeBall)null, null);
         this.raidPokemon = raidPokemon;
         this.raid = raid;
      }

      public Pokemon getRaidPokemon() {
         return this.raidPokemon;
      }

      public RaidData getRaid() {
         return this.raid;
      }
   }

   public static class FailedCapture extends CaptureEvent {
      public FailedCapture(EntityPlayerMP player, EntityPixelmon pokemon, EntityPokeBall pokeball) {
         super(player, pokemon, pokeball, null);
      }
   }

   @Cancelable
   public static class SuccessfulRaidCapture extends CaptureEvent {
      private final Pokemon raidPokemon;
      private final RaidData raid;

      public SuccessfulRaidCapture(EntityPlayerMP player, Pokemon raidPokemon, RaidData raid) {
         super(player, (EntityPixelmon)null, (EntityPokeBall)null, null);
         this.raidPokemon = raidPokemon;
         this.raid = raid;
      }

      public Pokemon getRaidPokemon() {
         return this.raidPokemon;
      }

      public RaidData getRaid() {
         return this.raid;
      }
   }

   @Cancelable
   public static class SuccessfulCapture extends CaptureEvent {
      public SuccessfulCapture(EntityPlayerMP player, EntityPixelmon pokemon, EntityPokeBall pokeball) {
         super(player, pokemon, pokeball, null);
      }
   }

   @Cancelable
   public static class StartRaidCapture extends CaptureEvent {
      private int catchRate;
      private double ballBonus;
      private final Pokemon raidPokemon;
      private final RaidData raid;

      public StartRaidCapture(EntityPlayerMP player, Pokemon raidPokemon, RaidData raid, int catchRate, double ballBonus) {
         super(player, (EntityPixelmon)null, (EntityPokeBall)null, null);
         this.catchRate = catchRate;
         this.ballBonus = ballBonus;
         this.raid = raid;
         this.raidPokemon = raidPokemon;
      }

      public void setCatchRate(int catchRate) {
         this.catchRate = Math.max(1, Math.min(255, catchRate));
      }

      public int getCatchRate() {
         return this.catchRate;
      }

      public void setBallBonus(double ballBonus) {
         this.ballBonus = Math.max(0.0, ballBonus);
      }

      public double getBallBonus() {
         return this.ballBonus;
      }

      public Pokemon getRaidPokemon() {
         return this.raidPokemon;
      }

      public RaidData getRaid() {
         return this.raid;
      }
   }

   @Cancelable
   public static class StartCapture extends CaptureEvent {
      private int catchRate;
      private double ballBonus;

      public StartCapture(EntityPlayerMP player, EntityPixelmon pokemon, EntityPokeBall pokeball, int catchRate, double ballBonus) {
         super(player, pokemon, pokeball, null);
         this.catchRate = catchRate;
         this.ballBonus = ballBonus;
      }

      public void setCatchRate(int catchRate) {
         this.catchRate = Math.max(1, Math.min(255, catchRate));
      }

      public int getCatchRate() {
         return this.catchRate;
      }

      public void setBallBonus(double ballBonus) {
         this.ballBonus = Math.max(0.0, ballBonus);
      }

      public double getBallBonus() {
         return this.ballBonus;
      }
   }
}
