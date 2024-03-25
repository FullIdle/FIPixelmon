package com.pixelmonmod.tcg.api.card.spec;

import java.util.Optional;
import net.minecraft.nbt.NBTTagCompound;

public interface Specification extends Cloneable {
   boolean matches(Object var1);

   void apply(Object var1);

   Object create();

   Object create(boolean var1);

   NBTTagCompound write(NBTTagCompound var1);

   Optional getValue(Class var1);
}
