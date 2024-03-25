package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GenericModelHolder extends ModelHolder {
   private Class type;
   private ResourceLocation resource;
   private Object[] parameters;

   public GenericModelHolder(Class clazz, ResourceLocation resource) {
      this.type = clazz;
      this.resource = resource;
   }

   public GenericModelHolder(Class clazz, Object... parameters) {
      this(clazz, (ResourceLocation)null);
      this.parameters = parameters;
   }

   public GenericModelHolder(Class clazz) {
      this(clazz, (ResourceLocation)null);
   }

   public GenericModelHolder(ResourceLocation model) {
      this(GenericSmdModel.class, model);
   }

   public GenericModelHolder(String modelPath) {
      this(new ResourceLocation("pixelmon", "models/" + modelPath));
   }

   public void render() {
      this.getModel().func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
   }

   public void render(float scale) {
      this.getModel().func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, scale);
   }

   protected ModelBase loadModel() {
      if (this.type == GenericSmdModel.class) {
         return new GenericSmdModel(this.resource, false);
      } else {
         try {
            return this.parameters != null ? (ModelBase)this.type.getConstructors()[0].newInstance(this.parameters) : (ModelBase)this.type.getConstructor().newInstance();
         } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException var2) {
            var2.printStackTrace();
            return null;
         }
      }
   }
}
