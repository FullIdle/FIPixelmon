package com.pixelmonmod.pixelmon.quests.actions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.quests.QuestActionEvent;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Executor;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class Action {
   private final Executor executor;
   private final IAction action;
   private final Arguments data;
   private final int id;

   public Action(String[] args, Quest quest, Stage stage, int id) throws InvalidQuestArgsException {
      this.id = id;
      String executorString = args[0];
      ExecutorMode mode;
      if (executorString.contains("+")) {
         mode = ExecutorMode.AND;
      } else {
         mode = ExecutorMode.OR;
      }

      executorString = executorString.replace("+", "");
      String[] executors = executorString.split(",");
      int[] parsedExecutors = new int[executors.length];
      int i = 0;
      String[] var10 = executors;
      int var11 = executors.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         String executor = var10[var12];
         parsedExecutors[i++] = Integer.parseInt(executor);
      }

      this.executor = new Executor(mode, parsedExecutors);
      this.action = QuestRegistry.getInstance().getAction(args[1]);
      this.data = this.action.parse(quest, stage, new ArgsIn(args, QuestElementType.ACTION));
   }

   public int getID() {
      return this.id;
   }

   public ExecutorMode getMode() {
      return this.executor.getMode();
   }

   public int[] getExecutors() {
      return this.executor.getExecutors();
   }

   public void execute(Quest questIn, Stage stageIn, QuestData dataIn, QuestProgress progressIn) throws InvalidQuestArgsException {
      if (!Pixelmon.EVENT_BUS.post(new QuestActionEvent(dataIn.getPlayer(), progressIn, stageIn, this))) {
         this.action.execute(questIn, stageIn, dataIn, progressIn, this.data);
      }

   }
}
