package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ReceivedModListEvent extends Event {
   public final EntityPlayerMP player;
   public final String[] modIds;

   public ReceivedModListEvent(EntityPlayerMP player, String[] modIds) {
      this.player = player;
      this.modIds = modIds;
   }
}
