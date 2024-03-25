package com.pixelmonmod.pixelmon.api.storage;

import java.util.UUID;
import javax.annotation.Nonnull;

public interface IStorageSaveAdapter {
   void save(PokemonStorage var1);

   @Nonnull
   PokemonStorage load(UUID var1, Class var2);
}
