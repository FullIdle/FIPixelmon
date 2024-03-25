package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LostToTrainerEvent extends Event {
   public final EntityPlayerMP player;
   public final NPCTrainer trainer;

   public LostToTrainerEvent(EntityPlayerMP player, NPCTrainer trainer) {
      this.player = player;
      this.trainer = trainer;
   }
}
