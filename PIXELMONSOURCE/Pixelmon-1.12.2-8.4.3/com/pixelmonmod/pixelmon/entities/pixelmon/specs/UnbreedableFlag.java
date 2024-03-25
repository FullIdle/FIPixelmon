package com.pixelmonmod.pixelmon.entities.pixelmon.specs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UnbreedableFlag {
   public static PokemonSpec UNBREEDABLE;

   public static void init() {
      Pixelmon.EVENT_BUS.register(new EventHandler());
      UNBREEDABLE = new PokemonSpec("unbreedable");
   }

   public static class EventHandler {
      @SubscribeEvent
      public void onBreedAddPokemon(BreedEvent.AddPokemon event) {
         if (UnbreedableFlag.UNBREEDABLE.matches(event.pokemon)) {
            event.player.func_71053_j();
            event.setCanceled(true);
            ChatHandler.sendChat(event.player, "pixelmon.ranch.cannotbreed", event.pokemon.getSpecies().name);
         }

      }
   }
}
