package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import java.util.Objects;
import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PokedexEvent extends Event {
   public static final String CAPTURE = "capture";
   public static final String TRADE_PLAYER = "tradePlayer";
   public static final String TRADE_NPC = "tradeNPC";
   public static final String EVOLUTION = "evolution";
   public static final String EGG = "egg";
   public static final String STORAGE_MOVEMENT = "storageMovement";
   public static final String COMMAND_GIVEN = "commandGiven";
   public static final String POKEDEX_KEY = "pokedexKey";
   public final UUID uuid;
   public final Pokemon pokemon;
   public final EnumPokedexRegisterStatus oldStatus;
   public final EnumPokedexRegisterStatus newStatus;
   public final String cause;

   public PokedexEvent(UUID uuid, Pokemon pokemon, EnumPokedexRegisterStatus newStatus, String cause) {
      this.uuid = uuid;
      this.pokemon = pokemon;
      this.oldStatus = Pixelmon.storageManager.getParty(uuid).pokedex.get(pokemon.getSpecies().getNationalPokedexInteger());
      this.newStatus = newStatus;
      this.cause = cause;
   }

   public EnumSpecies getSpecies() {
      return this.pokemon.getSpecies();
   }

   public boolean isCausedByCapture() {
      return Objects.equals(this.cause, "capture");
   }

   public boolean isCausedByPlayerTrade() {
      return Objects.equals(this.cause, "tradePlayer");
   }

   public boolean isCausedByNPCTrade() {
      return Objects.equals(this.cause, "tradeNPC");
   }

   public boolean isCausedByEvolution() {
      return Objects.equals(this.cause, "evolution");
   }

   public boolean isCausedByEggHatching() {
      return Objects.equals(this.cause, "egg");
   }

   public boolean isCausedByStorageMovement() {
      return Objects.equals(this.cause, "storageMovement");
   }

   public boolean isCausedBySidemod() {
      return !this.isCausedByCapture() && !this.isCausedByEggHatching() && !this.isCausedByNPCTrade() && !this.isCausedByPlayerTrade() && !this.isCausedByEvolution() && !this.isCausedByStorageMovement();
   }

   public boolean isBeingSeenByPokedexKey() {
      return Objects.equals(this.cause, "pokedexKey");
   }
}
