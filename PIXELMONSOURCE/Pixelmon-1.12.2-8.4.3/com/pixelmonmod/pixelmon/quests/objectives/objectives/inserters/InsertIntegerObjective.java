package com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters;

import com.pixelmonmod.pixelmon.RandomHelper;
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

public class InsertIntegerObjective implements IObjective {
   public String identifier() {
      return "INTEGER_INSERTER";
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), (s) -> {
         String[] strs;
         if (s.contains("-")) {
            strs = s.split("-");
            return new Range(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]));
         } else {
            strs = s.split(";");
            int[] ints = new int[strs.length];

            for(int i = 0; i < strs.length; ++i) {
               ints[i] = Integer.parseInt(strs[i]);
            }

            return ints;
         }
      }));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String key = (String)arguments.value(0, progress);
      String testStr = progress.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         return false;
      } else {
         Object options = arguments.value(1, progress);
         if (options instanceof Range) {
            Range range = (Range)options;
            progress.setData(key, String.valueOf(range.get()));
         } else {
            int[] ints = (int[])((int[])options);
            progress.setData(key, String.valueOf(ints[RandomHelper.rand.nextInt(ints.length)]));
         }

         progress.initQuantities();
         progress.sendTo(data.getPlayer());
         return true;
      }
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("values", false, false, ArgumentType.TEXT, new String[0])});
   }

   private static class Range {
      private final int a;
      private final int b;

      public Range(int a, int b) {
         if (a < b) {
            this.a = a;
            this.b = b;
         } else {
            this.b = a;
            this.a = b;
         }

      }

      public int get() {
         int dif = this.b - this.a;
         return dif <= 0 ? this.a : RandomHelper.rand.nextInt(dif) + this.a;
      }
   }
}
