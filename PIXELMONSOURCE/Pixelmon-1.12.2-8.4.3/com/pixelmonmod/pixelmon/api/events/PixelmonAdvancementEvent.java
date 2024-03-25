package com.pixelmonmod.pixelmon.api.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonAdvancementEvent extends Event {
   public final Advancement advancement;
   public final EntityPlayerMP player;

   public PixelmonAdvancementEvent(EntityPlayerMP player, Advancement advancement) {
      this.player = player;
      this.advancement = advancement;
   }
}
