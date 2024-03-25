package com.pixelmonmod.pixelmon.battles.status;

import net.minecraft.nbt.NBTTagCompound;

public class NoStatus extends StatusPersist {
   public static final transient NoStatus noStatus = new NoStatus();

   public NoStatus() {
      super(StatusType.None);
   }

   public StatusPersist restoreFromNBT(NBTTagCompound nbt) {
      return noStatus;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_82580_o("Status");
   }
}
