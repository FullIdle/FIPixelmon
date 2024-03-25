package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EggHatchEvent extends Event {
   private final EntityPlayerMP player;
   private final PokemonStorage storage;
   /** @deprecated */
   @Deprecated
   public final Pokemon pokemon;

   protected EggHatchEvent(EntityPlayerMP player, PokemonStorage storage, Pokemon pokemon) {
      this.player = player;
      this.storage = storage;
      this.pokemon = pokemon;
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public PokemonStorage getStorage() {
      return this.storage;
   }

   public Pokemon getPokemon() {
      return this.pokemon;
   }

   public static class Post extends EggHatchEvent {
      public Post(EntityPlayerMP player, PokemonStorage storage, Pokemon pokemon) {
         super(player, storage, pokemon);
      }
   }

   public static class Pre extends EggHatchEvent {
      private ITextComponent message;

      public Pre(EntityPlayerMP player, PokemonStorage storage, Pokemon pokemon, ITextComponent message) {
         super(player, storage, pokemon);
         this.message = message;
      }

      public ITextComponent getMessage() {
         return this.message;
      }

      public void setMessage(ITextComponent message) {
         this.message = message;
      }
   }
}
