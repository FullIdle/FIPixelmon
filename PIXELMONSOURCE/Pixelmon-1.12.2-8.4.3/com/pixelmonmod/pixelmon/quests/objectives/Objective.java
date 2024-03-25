package com.pixelmonmod.pixelmon.quests.objectives;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.quests.QuestObjectiveEvent;
import com.pixelmonmod.pixelmon.api.events.quests.QuestStageEvent;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.actions.Action;
import com.pixelmonmod.pixelmon.quests.actions.ExecutorMode;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.NPCObjective;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.ArrayList;
import java.util.Iterator;

public class Objective {
   private final IObjective objective;
   private final Arguments data;
   private final int id;

   public Objective(String[] args, Quest quest, Stage stage, int id) throws InvalidQuestArgsException {
      this.id = id;
      this.objective = QuestRegistry.getInstance().getObjective(args[0]);
      this.data = this.objective.parse(quest, stage, new ArgsIn(args, QuestElementType.OBJECTIVE));
      if (this.data == null) {
         int i = 0;
         ++i;
      }

      Iterator var8 = this.data.iterator();

      while(var8.hasNext()) {
         Object object = var8.next();
         if (object instanceof String) {
            String var7 = (String)object;
         }
      }

   }

   public int getID() {
      return this.id;
   }

   public IObjective getInternalObjective() {
      return this.objective;
   }

   public Arguments getData() {
      return this.data;
   }

   public boolean test(Stage stageIn, QuestData dataIn, QuestProgress progressIn, Context argsIn) throws InvalidQuestArgsException {
      return this.data.isAllSet(progressIn) && this.objective.test(stageIn, dataIn, progressIn, this, this.data, argsIn);
   }

   public int getQuantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return this.objective.quantity(stage, data, progress, objective, arguments);
   }

   public ArrayList mark(Stage stage, QuestProgress progress, int index, Object... args) {
      return this.objective.mark(stage, progress, this, index, this.data, new Context(args));
   }

   public boolean receive(String identifier, Stage stage, QuestData data, QuestProgress progress, int index, Object... args) throws InvalidQuestArgsException {
      if (!progress.isObjectiveComplete(index) && this.objective.identifier().equalsIgnoreCase(identifier) && this.test(stage, data, progress, new Context(args))) {
         progress.completeObjective(index);
         if (progress.isObjectiveComplete(index)) {
            Pixelmon.EVENT_BUS.post(new QuestObjectiveEvent.Complete(data.getPlayer(), progress, stage, this, index));
            Iterator var7 = stage.getParsedActions().iterator();

            while(var7.hasNext()) {
               Action action = (Action)var7.next();
               boolean hasIndex = false;
               boolean canExecute = true;
               int[] var11;
               int var12;
               int var13;
               int executor;
               if (action.getMode() == ExecutorMode.AND) {
                  var11 = action.getExecutors();
                  var12 = var11.length;

                  for(var13 = 0; var13 < var12; ++var13) {
                     executor = var11[var13];
                     if (executor == index) {
                        hasIndex = true;
                     }

                     if (!progress.isObjectiveComplete(executor)) {
                        canExecute = false;
                     }
                  }
               } else {
                  var11 = action.getExecutors();
                  var12 = var11.length;

                  for(var13 = 0; var13 < var12; ++var13) {
                     executor = var11[var13];
                     if (executor == index) {
                        hasIndex = true;
                     } else if (progress.isObjectiveComplete(executor)) {
                        canExecute = false;
                     }
                  }
               }

               canExecute = canExecute && hasIndex;
               if (canExecute) {
                  action.execute(progress.getQuest(), stage, data, progress);
               }
            }

            if (progress.canProgress()) {
               if (stage.getNextStage() < 0) {
                  progress.complete(data.getPlayer());
               } else {
                  Pixelmon.EVENT_BUS.post(new QuestStageEvent.Complete(data.getPlayer(), progress, stage));
                  progress.moveStage(data, stage, stage.getNextStage());
               }

               progress.sendTo(data.getPlayer());
               return true;
            }
         }

         if (stage.getStage() == progress.getStage() && this.objective instanceof NPCObjective) {
            progress.resetObjective(index);
         }

         progress.sendTo(data.getPlayer());
         return true;
      } else {
         return false;
      }
   }
}
