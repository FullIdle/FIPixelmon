package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class QuestStageEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;
   public final Stage stage;

   public QuestStageEvent(EntityPlayerMP player, QuestProgress progress, Stage stage) {
      this.player = player;
      this.progress = progress;
      this.stage = stage;
   }

   public static class Set extends QuestStageEvent {
      public final boolean abnormal;

      public Set(EntityPlayerMP player, QuestProgress progress, Stage stage, boolean abnormal) {
         super(player, progress, stage);
         this.abnormal = abnormal;
      }
   }

   public static class Complete extends QuestStageEvent {
      public Complete(EntityPlayerMP player, QuestProgress progress, Stage stage) {
         super(player, progress, stage);
      }
   }
}
