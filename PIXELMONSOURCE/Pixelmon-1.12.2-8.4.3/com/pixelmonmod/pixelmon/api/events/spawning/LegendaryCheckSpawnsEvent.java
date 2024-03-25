package com.pixelmonmod.pixelmon.api.events.spawning;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LegendaryCheckSpawnsEvent extends Event {
   public final ICommandSender sender;
   public boolean shouldShowTime = true;
   public boolean shouldShowChance = true;

   public LegendaryCheckSpawnsEvent(ICommandSender sender) {
      this.sender = sender;
   }
}
