package com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.objectives.IObjective;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class InsertConcatenationObjective implements IObjective {
   public String identifier() {
      return "CONCATENATE_INSERTER";
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), (s) -> {
         return s;
      }), Argument.from(args.get(2), (s) -> {
         return s;
      }), Argument.from(args.get(3), (s) -> {
         return s;
      }, " "));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String key = (String)arguments.value(0, progress);
      String testStr = progress.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         return false;
      } else {
         String v1 = progress.getData((String)arguments.value(1, progress));
         String v2 = progress.getData((String)arguments.value(2, progress));
         String spacer = (String)arguments.value(3, progress);
         if (v1 != null && v2 != null) {
            progress.setData(key, v1 + spacer + v2);
            progress.sendTo(data.getPlayer());
            return true;
         } else {
            return false;
         }
      }
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("other_key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("other_key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("spacer", true, false, ArgumentType.TEXT, new String[0])});
   }
}
