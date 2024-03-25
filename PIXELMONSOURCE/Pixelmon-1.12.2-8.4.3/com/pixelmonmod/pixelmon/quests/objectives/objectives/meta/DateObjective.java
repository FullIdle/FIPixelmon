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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;

public class DateObjective implements IObjective {
   private static final transient ZoneId ZONE_ID = ZoneId.systemDefault();

   public String identifier() {
      return "DATE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("start_date", false, false, ArgumentType.DATE, new String[0]), new QuestElementArgument("end_date", false, false, ArgumentType.DATE, new String[0])});
   }

   private LocalDate getDate(String in, int firstMonth) {
      int yearNow = LocalDate.now().getYear();
      int yearOffset = 0;
      String[] split = in.split("/");
      int day = Integer.parseInt(split[0]);
      int month = Integer.parseInt(split[1]);
      if (firstMonth > -1 && month < firstMonth) {
         yearOffset = 1;
      }

      return LocalDate.of(yearNow + yearOffset, month, day);
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) throws InvalidQuestArgsException {
      if (args.size() == 2) {
         int firstMonth = Integer.parseInt(args.get(0).split("/")[1]);
         return Arguments.create(Argument.from(args.get(0), (s) -> {
            return this.getDate(s, -1);
         }), Argument.from(args.get(1), (s) -> {
            return this.getDate(s, firstMonth);
         }));
      } else {
         throw new InvalidQuestArgsException(this.identifier(), quest, stage);
      }
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      LocalDate date = LocalDate.now(ZONE_ID);
      return date.isAfter((ChronoLocalDate)arguments.value(0, progress)) && date.isBefore((ChronoLocalDate)arguments.value(1, progress));
   }
}
