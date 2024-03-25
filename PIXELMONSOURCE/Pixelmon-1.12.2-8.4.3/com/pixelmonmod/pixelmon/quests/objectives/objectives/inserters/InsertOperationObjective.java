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
import org.apache.commons.lang3.math.NumberUtils;

public class InsertOperationObjective implements IObjective {
   public String identifier() {
      return "OPERATION_INSERTER";
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), (s) -> {
         return s;
      }), Argument.from(args.get(2), (s) -> {
         return s;
      }), Argument.from(args.get(3), Operator::get, InsertOperationObjective.Operator.Addition), Argument.from(args.get(4), Mode::get, InsertOperationObjective.Mode.None));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String key = (String)arguments.value(0, progress);
      String testStr = progress.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         return false;
      } else {
         String v1 = progress.getData((String)arguments.value(1, progress));
         String v2 = progress.getData((String)arguments.value(2, progress));
         if (NumberUtils.isParsable(v1) && NumberUtils.isParsable(v2)) {
            double d1 = Double.parseDouble(v1);
            double d2 = Double.parseDouble(v2);
            Operator operator = (Operator)arguments.value(3, progress);
            Mode mode = (Mode)arguments.value(4, progress);
            progress.setData(key, mode.perform(operator.calculate(d1, d2)));
            progress.sendTo(data.getPlayer());
            return true;
         } else {
            return false;
         }
      }
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("other_key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("other_key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("operator", false, false, ArgumentType.TEXT, new String[]{"Addition", "Subtraction", "Multiplication", "Division", "Modulo", "Power"}), new QuestElementArgument("mode", false, false, ArgumentType.TEXT, new String[]{"None", "Round", "Floor", "Ceiling"})});
   }

   public static enum Mode {
      None,
      Round,
      Floor,
      Ceiling;

      public String perform(double in) {
         switch (this) {
            case Floor:
               return String.valueOf((int)Math.floor(in));
            case Round:
               return String.valueOf((int)Math.round(in));
            case Ceiling:
               return String.valueOf((int)Math.ceil(in));
            default:
               return String.valueOf(in);
         }
      }

      public static Mode get(String name) {
         Mode[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Mode mode = var1[var3];
            if (mode.name().equalsIgnoreCase(name)) {
               return mode;
            }
         }

         return None;
      }
   }

   public static enum Operator {
      Addition,
      Subtraction,
      Multiplication,
      Division,
      Modulo,
      Power;

      public double calculate(double a, double b) {
         switch (this) {
            case Subtraction:
               return a - b;
            case Multiplication:
               return a * b;
            case Division:
               return a / b;
            case Modulo:
               return a % b;
            case Power:
               return Math.pow(a, b);
            default:
               return a + b;
         }
      }

      public static Operator get(String name) {
         Operator[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Operator operator = var1[var3];
            if (operator.name().equalsIgnoreCase(name)) {
               return operator;
            }
         }

         return Addition;
      }
   }
}
