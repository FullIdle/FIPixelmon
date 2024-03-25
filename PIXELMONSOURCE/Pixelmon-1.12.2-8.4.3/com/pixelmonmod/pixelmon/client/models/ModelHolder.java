package com.pixelmonmod.pixelmon.client.models;

import com.google.common.collect.Sets;
import java.time.Instant;
import java.util.Set;
import net.minecraft.client.model.ModelBase;

public abstract class ModelHolder {
   static Set loadedHolders = Sets.newConcurrentHashSet();
   long lastAccess;
   protected ModelBase model = null;

   public ModelBase getModel() {
      this.lastAccess = Instant.now().getEpochSecond();
      if (this.model == null) {
         this.model = this.loadModel();
         loadedHolders.add(this);
      }

      return this.model;
   }

   public void clear() {
      loadedHolders.remove(this);
      this.model = null;
   }

   protected abstract ModelBase loadModel();
}
