package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public interface IStorageSaveScheduler {
   void onServerStopping(FMLServerStoppingEvent var1);

   default void save(PokemonStorage storage) {
      Pixelmon.storageManager.getSaveAdapter().save(storage);
      storage.setHasChanged(false);
   }
}
