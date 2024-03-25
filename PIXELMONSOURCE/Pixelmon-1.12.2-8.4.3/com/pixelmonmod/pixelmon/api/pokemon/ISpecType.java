package com.pixelmonmod.pixelmon.api.pokemon;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public interface ISpecType {
   List getKeys();

   SpecValue parse(@Nullable String var1);

   SpecValue readFromNBT(NBTTagCompound var1);

   void writeToNBT(NBTTagCompound var1, SpecValue var2);

   Class getSpecClass();

   String toParameterForm(SpecValue var1);
}
