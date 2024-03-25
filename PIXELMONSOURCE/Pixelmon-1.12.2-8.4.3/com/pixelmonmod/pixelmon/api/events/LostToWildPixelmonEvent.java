package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LostToWildPixelmonEvent extends Event {
   public final EntityPlayerMP player;
   public final WildPixelmonParticipant wpp;

   public LostToWildPixelmonEvent(EntityPlayerMP player, WildPixelmonParticipant wpp) {
      this.player = player;
      this.wpp = wpp;
   }
}
