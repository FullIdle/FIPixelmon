package com.pixelmonmod.tcg.api.card.spec;

import java.util.Optional;
import net.minecraft.nbt.NBTTagCompound;

public class EmptySpecification implements Specification {
   public static EmptySpecification INSTANCE = new EmptySpecification();

   private EmptySpecification() {
   }

   public boolean matches(Object o) {
      return true;
   }

   public void apply(Object o) {
   }

   public Object create() {
      return null;
   }

   public Object create(boolean shallow) {
      return null;
   }

   public NBTTagCompound write(NBTTagCompound nbt) {
      return nbt;
   }

   public Optional getValue(Class clazz) {
      return Optional.empty();
   }
}
