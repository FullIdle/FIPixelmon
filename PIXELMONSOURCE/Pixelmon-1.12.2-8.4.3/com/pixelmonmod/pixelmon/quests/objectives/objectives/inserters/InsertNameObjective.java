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
import com.pixelmonmod.pixelmon.quests.util.NPCNames;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class InsertNameObjective implements IObjective {
   public String identifier() {
      return "NAME_INSERTER";
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), this::parseGender));
   }

   private byte parseGender(String in) {
      if (!in.equalsIgnoreCase("0") && !in.equalsIgnoreCase("m") && !in.equalsIgnoreCase("male")) {
         return (byte)(!in.equalsIgnoreCase("1") && !in.equalsIgnoreCase("f") && !in.equalsIgnoreCase("female") ? 2 : 1);
      } else {
         return 0;
      }
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String key = (String)arguments.value(0, progress);
      String testStr = progress.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         return false;
      } else {
         byte gender = (Byte)arguments.value(1, progress);
         progress.setData(key, NPCNames.getName(gender));
         progress.sendTo(data.getPlayer());
         return true;
      }
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("gender", false, false, ArgumentType.TEXT, new String[0])});
   }
}
