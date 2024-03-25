package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PokemonFactory {
   public abstract Pokemon create(UUID var1);

   public Pokemon createDefault(UUID pokemonUUID) {
      return new Pokemon(pokemonUUID);
   }

   public Pokemon create(EnumSpecies species) {
      PokemonSpec spec = new PokemonSpec(new String[0]);
      spec.name = species.name;
      return this.create(spec);
   }

   public Pokemon create(PokemonSpec spec) {
      return spec.create();
   }

   public Pokemon create(NBTTagCompound nbt) {
      Pokemon pokemon = this.create(nbt.func_186857_a("UUID"));
      pokemon.readFromNBT(nbt);
      return pokemon;
   }
}
