package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class FinishQuestEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;

   public FinishQuestEvent(EntityPlayerMP player, QuestProgress progress) {
      this.player = player;
      this.progress = progress;
   }

   public static class Fail extends FinishQuestEvent {
      public Fail(EntityPlayerMP player, QuestProgress progress) {
         super(player, progress);
      }
   }

   public static class Complete extends FinishQuestEvent {
      public Complete(EntityPlayerMP player, QuestProgress progress) {
         super(player, progress);
      }
   }
}
