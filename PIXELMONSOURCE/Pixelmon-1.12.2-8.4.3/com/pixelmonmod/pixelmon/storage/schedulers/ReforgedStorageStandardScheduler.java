package com.pixelmonmod.pixelmon.storage.schedulers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveScheduler;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import java.util.Iterator;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedStorageStandardScheduler implements IStorageSaveScheduler {
   public int lastSaveTick = 0;

   public void onServerStopping(FMLServerStoppingEvent event) {
      this.saveAll();
      this.lastSaveTick = 0;
   }

   @SubscribeEvent
   public void onWorldSave(WorldEvent.Save event) {
      int currentTick = event.getWorld().func_73046_m().func_71259_af();
      if (currentTick > this.lastSaveTick) {
         this.saveAll();
         this.lastSaveTick = currentTick;
      }

   }

   public void saveAll() {
      Iterator var1 = Pixelmon.storageManager.getAllCachedStorages().iterator();

      while(var1.hasNext()) {
         PokemonStorage storage = (PokemonStorage)var1.next();
         if (storage.getShouldSave()) {
            this.save(storage);
            Pixelmon.storageManager.onStorageSaved(storage);
         }
      }

   }
}
