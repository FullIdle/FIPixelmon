package com.pixelmonmod.pixelmon.api.events.dialogue;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DialogueEndedEvent extends Event {
   public final EntityPlayerMP player;

   public DialogueEndedEvent(EntityPlayerMP player) {
      this.player = player;
   }
}
