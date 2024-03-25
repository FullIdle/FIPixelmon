package com.pixelmonmod.pixelmon.api.events.quests;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class QuestObjectiveEvent extends Event {
   public final EntityPlayerMP player;
   public final QuestProgress progress;
   public final Stage stage;
   public final Objective objective;
   public final int objectiveIndex;

   public QuestObjectiveEvent(EntityPlayerMP player, QuestProgress progress, Stage stage, Objective objective, int objectiveIndex) {
      this.player = player;
      this.progress = progress;
      this.stage = stage;
      this.objective = objective;
      this.objectiveIndex = objectiveIndex;
   }

   public static class Complete extends QuestObjectiveEvent {
      public Complete(EntityPlayerMP player, QuestProgress progress, Stage stage, Objective objective, int objectiveIndex) {
         super(player, progress, stage, objective, objectiveIndex);
      }
   }

   @Cancelable
   public static class Progress extends QuestObjectiveEvent {
      public int completion;

      public Progress(EntityPlayerMP player, QuestProgress progress, Stage stage, Objective objective, int objectiveIndex, int completion) {
         super(player, progress, stage, objective, objectiveIndex);
         this.completion = completion;
      }
   }
}
