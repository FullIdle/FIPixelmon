package com.pixelmonmod.pixelmon.storage.schedulers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveScheduler;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class ReforgedStorageAsyncScheduler implements IStorageSaveScheduler {
   protected ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
   protected Deque saveList = new ConcurrentLinkedDeque();
   protected AtomicBoolean saving = new AtomicBoolean(false);

   public ReforgedStorageAsyncScheduler() {
      Pixelmon.LOGGER.info("Started the async storage scheduler.");
      this.executor.scheduleAtFixedRate(() -> {
         try {
            if (this.saving.compareAndSet(false, true)) {
               this.queueChangedStorages();
               this.flush();
            } else {
               Pixelmon.LOGGER.warn("Async saving reached scheduled save but something has our save lock.");
            }
         } catch (Throwable var5) {
            Pixelmon.LOGGER.warn("Async saving encountered a major error.");
            var5.printStackTrace();
         } finally {
            this.saving.set(false);
         }

      }, (long)PixelmonConfig.asyncInterval, (long)PixelmonConfig.asyncInterval, TimeUnit.SECONDS);
   }

   public void onServerStopping(FMLServerStoppingEvent event) {
      if (this.saving.compareAndSet(false, true)) {
         this.queueChangedStorages();
         this.flush();
         this.saving.set(false);
      } else {
         try {
            Thread.sleep(4000L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         this.queueChangedStorages();
         this.flush();
         this.saving.set(false);
      }

   }

   public void queueChangedStorages() {
      Iterator var1 = Pixelmon.storageManager.getAllCachedStorages().iterator();

      while(var1.hasNext()) {
         PokemonStorage storage = (PokemonStorage)var1.next();
         if (!this.saveList.contains(storage) && storage.getShouldSave()) {
            this.saveList.add(storage);
         }
      }

   }

   public void flush() {
      while(!this.saveList.isEmpty()) {
         PokemonStorage storage = (PokemonStorage)this.saveList.removeFirst();

         try {
            this.save(storage);
            Pixelmon.storageManager.onStorageSaved(storage);
         } catch (Throwable var3) {
            Pixelmon.LOGGER.error("Couldn't async save storage of type " + storage.getClass().getSimpleName() + " and UUID: " + storage.uuid);
         }
      }

   }
}
