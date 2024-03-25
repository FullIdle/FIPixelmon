package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class DynamaxCandyEvent extends Event {
   public EntityPixelmon pixelmon;
   public EntityPlayerMP player;

   public DynamaxCandyEvent(EntityPlayerMP player, EntityPixelmon pixelmon) {
      this.player = player;
      this.pixelmon = pixelmon;
   }
}
