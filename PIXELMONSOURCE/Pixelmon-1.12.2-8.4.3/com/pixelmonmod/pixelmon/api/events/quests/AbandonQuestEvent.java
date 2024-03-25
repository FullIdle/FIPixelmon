package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AbandonQuestEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;

   public AbandonQuestEvent(EntityPlayerMP player, QuestProgress progress) {
      this.player = player;
      this.progress = progress;
   }
}
