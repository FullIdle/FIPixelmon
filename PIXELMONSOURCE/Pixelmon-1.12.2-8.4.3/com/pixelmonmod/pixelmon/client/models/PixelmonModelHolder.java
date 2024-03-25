package com.pixelmonmod.pixelmon.client.models;

import com.pixelmonmod.pixelmon.Pixelmon;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.minecraft.client.model.ModelBase;

public class PixelmonModelHolder extends ModelHolder {
   private Class clazz;
   private PixelmonSmdFactory factory;
   private Future future;

   public PixelmonModelHolder(Class clazz) {
      this.clazz = clazz;
   }

   public PixelmonModelHolder(PixelmonSmdFactory factory) {
      this.factory = factory;
      this.clazz = PixelmonSmdFactory.Impl.class;
   }

   public ModelBase getModel() {
      this.lastAccess = Instant.now().getEpochSecond();
      if (this.model != null) {
         return this.model;
      } else {
         if (this.future == null) {
            this.future = ResourceLoader.addTask(this::loadModel);
         }

         if (this.future.isDone()) {
            try {
               this.model = (ModelBase)this.future.get();
               loadedHolders.add(this);
            } catch (ExecutionException | InterruptedException var2) {
               var2.printStackTrace();
            }

            this.future = null;
            return this.model;
         } else {
            return ResourceLoader.DUMMY;
         }
      }
   }

   protected ModelBase loadModel() {
      if (this.factory != null) {
         return this.factory.createModel();
      } else if (this.clazz != null) {
         try {
            Constructor constructor = this.clazz.getConstructors()[0];
            if (constructor.getParameterCount() == 0) {
               return (ModelBase)constructor.newInstance();
            }

            Pixelmon.LOGGER.error("No valid constructor found in " + this.clazz.getSimpleName());
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var2) {
            var2.printStackTrace();
         }

         return ResourceLoader.DUMMY;
      } else {
         return null;
      }
   }

   public void clear() {
      super.clear();
      this.future = null;
   }

   public Class getModelClass() {
      return this.clazz;
   }
}
