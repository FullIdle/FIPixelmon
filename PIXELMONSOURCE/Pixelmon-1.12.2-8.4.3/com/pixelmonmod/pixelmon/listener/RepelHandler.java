package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.config.PixelmonPotions;
import com.pixelmonmod.pixelmon.items.ItemRepel;
import net.minecraft.entity.player.EntityPlayerMP;

public class RepelHandler {
   public static boolean hasRepel(EntityPlayerMP player) {
      return player.func_70644_a(PixelmonPotions.repel);
   }

   public static void applyRepel(EntityPlayerMP player, ItemRepel.EnumRepel repel) {
      if (hasRepel(player)) {
         int time = player.func_70660_b(PixelmonPotions.repel).func_76459_b();
         player.func_184589_d(PixelmonPotions.repel);
         player.func_70690_d(PixelmonPotions.repel.getEffect((repel.ticks + time) / 20));
      } else {
         player.func_70690_d(PixelmonPotions.repel.getEffect(repel.ticks / 20));
      }

   }
}
