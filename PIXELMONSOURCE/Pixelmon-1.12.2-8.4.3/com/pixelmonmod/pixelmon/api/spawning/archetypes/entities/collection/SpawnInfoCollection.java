package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.collection;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.translation.I18n;

public class SpawnInfoCollection extends SpawnInfo {
   public static final String TYPE_ID_COLLECTION = "collection";
   public String collectionLabel = "spawning.collections.default";
   public ArrayList collection = new ArrayList();

   public SpawnInfoCollection() {
      super("collection");
   }

   public void onExport() {
      super.onExport();
      Iterator var1 = this.collection.iterator();

      while(var1.hasNext()) {
         SpawnInfo spawnInfo = (SpawnInfo)var1.next();
         spawnInfo.onExport();
      }

   }

   public void onImport() {
      super.onImport();
      Iterator var1 = this.collection.iterator();

      while(var1.hasNext()) {
         SpawnInfo spawnInfo = (SpawnInfo)var1.next();
         spawnInfo.onImport();
      }

   }

   public boolean fits(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      if (!super.fits(spawner, spawnLocation)) {
         return false;
      } else {
         Iterator var3 = this.collection.iterator();

         SpawnInfo spawnInfo;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            spawnInfo = (SpawnInfo)var3.next();
         } while(!spawnInfo.fits(spawner, spawnLocation));

         return true;
      }
   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      ArrayList suitable = new ArrayList();
      this.collection.forEach((spawnInfo) -> {
         if (spawnInfo.fits(spawner, spawnLocation)) {
            suitable.add(spawnInfo);
         }

      });
      SpawnInfo selection = spawner.selectionAlgorithm.choose(spawner, spawnLocation, suitable);
      return selection != null ? selection.construct(spawner, spawnLocation) : null;
   }

   public String toString() {
      return I18n.func_74838_a(this.collectionLabel);
   }
}
