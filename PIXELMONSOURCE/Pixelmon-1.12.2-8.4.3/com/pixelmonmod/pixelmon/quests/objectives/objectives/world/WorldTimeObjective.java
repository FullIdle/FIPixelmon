package com.pixelmonmod.pixelmon.quests.objectives.objectives.world;

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

public class WorldTimeObjective implements IObjective {
   public String identifier() {
      return "WORLD_TIME";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("time_ticks", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("time_range_ticks", true, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) throws InvalidQuestArgsException {
      return Arguments.create(Argument.from(args.get(0), Long::parseLong), Argument.from(args.get(1), Long::parseLong));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) throws InvalidQuestArgsException {
      long time = (Long)context.get(0);
      long arg1;
      if (arguments.usableSize() == 1) {
         arg1 = (Long)arguments.value(0, progress);
         return time == arg1;
      } else if (arguments.usableSize() != 2) {
         throw new InvalidQuestArgsException();
      } else {
         arg1 = (Long)arguments.value(0, progress);
         long arg2 = (Long)arguments.value(1, progress);
         return time >= arg1 && time <= arg2;
      }
   }
}
