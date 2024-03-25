package com.pixelmonmod.pixelmon.util;

import net.minecraft.entity.player.EntityPlayerMP;

public class AirSaver {
   private EntityPlayerMP player;
   private int startAir;

   public AirSaver(EntityPlayerMP player) {
      this.player = player;
      this.startAir = Math.max(1, player.func_70086_ai());
   }

   public void tick() {
      this.player.func_70050_g(this.startAir);
   }
}
