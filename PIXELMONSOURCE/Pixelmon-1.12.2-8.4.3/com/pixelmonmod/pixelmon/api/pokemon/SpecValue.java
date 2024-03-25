package com.pixelmonmod.pixelmon.api.pokemon;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SpecValue {
   public String key;
   public Object value;

   public SpecValue(String key, Object value) {
      this.key = key;
      this.value = value;
   }

   public abstract Class getValueClass();

   public abstract void apply(EntityPixelmon var1);

   /** @deprecated */
   @Deprecated
   public void apply(NBTTagCompound nbt) {
   }

   public abstract void apply(Pokemon var1);

   public abstract boolean matches(EntityPixelmon var1);

   /** @deprecated */
   @Deprecated
   public boolean matches(NBTTagCompound nbt) {
      return false;
   }

   public abstract boolean matches(Pokemon var1);

   public abstract SpecValue clone();
}
