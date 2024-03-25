package com.pixelmonmod.pixelmon.storage.playerData;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveData {
   void writeToNBT(NBTTagCompound var1);

   void readFromNBT(NBTTagCompound var1);
}
