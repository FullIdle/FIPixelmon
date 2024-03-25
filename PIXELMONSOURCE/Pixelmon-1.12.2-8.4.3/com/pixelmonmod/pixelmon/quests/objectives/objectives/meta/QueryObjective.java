package com.pixelmonmod.pixelmon.quests.objectives.objectives.meta;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.objectives.IObjective;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class QueryObjective implements IObjective {
   public String identifier() {
      return "QUERY";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("quest_filename", false, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) throws InvalidQuestArgsException {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s.replace("_", " ");
      }));
   }

   public boolean test(Stage stageIn, QuestData data, QuestProgress progressIn, Objective objectiveIn, Arguments arguments, Context context) throws InvalidQuestArgsException {
      String questNameA = (String)arguments.value(0, progressIn);
      String questNameB = null;
      if (!questNameA.endsWith(".json")) {
         questNameB = questNameA + ".json";
      }

      QuestProgress otherProgress = data.getProgressForQuest(questNameB);
      if (otherProgress == null) {
         otherProgress = data.getProgressForQuest(questNameA);
      }

      if (otherProgress == null) {
         throw new InvalidQuestArgsException(this.identifier(), progressIn.getQuest(), stageIn);
      } else {
         return otherProgress.isComplete();
      }
   }
}
