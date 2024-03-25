package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class SetStageAction implements IAction {
   public String identifier() {
      return "SET_STAGE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("stage", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("quest_filename", true, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Short::parseShort), Argument.from(args.get(1), (s) -> {
         return s.replace("_", " ");
      }, (Object)null));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) throws InvalidQuestArgsException {
      short stageID = (Short)arguments.value(0, progress);
      String questNameA = (String)arguments.value(1, progress);
      if (questNameA == null) {
         progress.moveStage(data, stage, stageID);
         progress.sendTo(data.getPlayer());
      } else {
         String questNameB = null;
         if (!questNameA.endsWith(".json")) {
            questNameB = questNameA + ".json";
         }

         QuestProgress otherProgress = data.getProgressForQuest(questNameB);
         if (otherProgress == null) {
            otherProgress = data.getProgressForQuest(questNameA);
         }

         if (otherProgress != null) {
            otherProgress.moveStage(data, otherProgress.getCurrentStage(), stageID);
            otherProgress.sendTo(data.getPlayer());
         }
      }

   }
}
