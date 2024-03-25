package com.pixelmonmod.pixelmon.battles.rules.teamselection;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class TeamSelectPokemon implements IEncodeable {
   public PokemonForm pokemon;
   public String customTexture = "";
   public int pokeBall;
   public boolean isShiny;
   public boolean isEgg;
   public int eggCycles;

   public TeamSelectPokemon(Pokemon pokemon) {
      this.pokemon = new PokemonForm(pokemon.getSpecies(), pokemon.getForm(), pokemon.getGender());
      this.customTexture = pokemon.getCustomTexture();
      this.pokeBall = pokemon.getCaughtBall().getIndex();
      this.isShiny = pokemon.isShiny();
      this.isEgg = pokemon.isEgg();
      this.eggCycles = pokemon.getEggCycles();
   }

   public TeamSelectPokemon(ByteBuf buffer) {
      this.decodeInto(buffer);
   }

   public void encodeInto(ByteBuf buffer) {
      this.pokemon.encodeInto(buffer);
      ByteBufUtils.writeUTF8String(buffer, this.customTexture);
      buffer.writeInt(this.pokeBall);
      buffer.writeBoolean(this.isShiny);
      buffer.writeBoolean(this.isEgg);
      buffer.writeInt(this.eggCycles);
   }

   public void decodeInto(ByteBuf buffer) {
      this.pokemon = new PokemonForm(buffer);
      this.customTexture = ByteBufUtils.readUTF8String(buffer);
      this.pokeBall = buffer.readInt();
      this.isShiny = buffer.readBoolean();
      this.isEgg = buffer.readBoolean();
      this.eggCycles = buffer.readInt();
   }
}
