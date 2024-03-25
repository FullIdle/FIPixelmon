package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PokemonListPacket implements IMessage {
   public PokemonForm[] pokemonList;
   public int[] pokemonListIndex;

   public PokemonListPacket() {
   }

   public PokemonListPacket(EnumSpecies... pokemon) {
      this(pokemon, new int[pokemon.length]);
   }

   public PokemonListPacket(EnumSpecies[] pokemon, int[] forms) {
      PokemonForm[] pokemonForms = new PokemonForm[pokemon.length];

      for(int i = 0; i < pokemon.length; ++i) {
         pokemonForms[i] = new PokemonForm(pokemon[i], forms[i]);
      }

      this.initialize(pokemonForms);
   }

   public PokemonListPacket(PokemonForm[] pokemon) {
      this.initialize(pokemon);
   }

   private void initialize(PokemonForm[] pokemon) {
      this.pokemonList = pokemon;
      this.pokemonListIndex = new int[pokemon.length];

      for(int i = 0; i < this.pokemonList.length; ++i) {
         if (this.pokemonList[i] != null) {
            int id = this.pokemonList[i].pokemon.getNationalPokedexInteger();
            this.pokemonListIndex[i] = PixelmonConfig.isGenerationEnabled(this.pokemonList[i].pokemon.getGeneration()) ? id : -2;
         } else {
            this.pokemonListIndex[i] = -1;
         }
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonList = new PokemonForm[buffer.readShort()];
      this.pokemonListIndex = new int[this.pokemonList.length];

      for(int i = 0; i < this.pokemonList.length; ++i) {
         int index = buffer.readInt();
         this.pokemonListIndex[i] = index;
         if (index >= 0) {
            EnumSpecies pokemon = EnumSpecies.getFromDex(index);
            this.pokemonList[i] = new PokemonForm(pokemon, buffer.readShort());
         }
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.pokemonList.length);

      for(int i = 0; i < this.pokemonListIndex.length; ++i) {
         buffer.writeInt(this.pokemonListIndex[i]);
         if (this.pokemonListIndex[i] >= 0) {
            buffer.writeShort(this.pokemonList[i].form);
         }
      }

   }
}
