package com.pixelmonmod.pixelmon.quests.objectives.objectives.meta;

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

public class TestDataObjective implements IObjective {
   public String identifier() {
      return "TEST_DATA";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("value", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("mode", true, false, ArgumentType.TEXT, new String[]{"Equality", "Equals", "GreaterThan", "LessThan", "GreaterThanOrEquals", "LessThanOrEquals"})});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), (s) -> {
         return s;
      }), Argument.from(args.get(2), Mode::get, TestDataObjective.Mode.Equality));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String v1 = progress.getData((String)arguments.value(0, progress));
      String v2 = (String)arguments.value(1, progress);
      Mode mode = (Mode)arguments.value(2, progress);
      return mode.compare(v1, v2);
   }

   public static enum Mode {
      Equality,
      Equals,
      GreaterThan,
      LessThan,
      GreaterThanOrEquals,
      LessThanOrEquals;

      public boolean compare(String a, String b) {
         double da = 0.0;
         double db = 0.0;
         boolean parsable = false;
         if (this != Equality && NumberUtils.isParsable(a) && NumberUtils.isParsable(b)) {
            da = Double.parseDouble(a);
            db = Double.parseDouble(b);
            parsable = true;
         }

         switch (this) {
            case Equals:
               if (parsable) {
                  return da == db;
               }

               return false;
            case GreaterThan:
               if (parsable) {
                  return da > db;
               }

               return false;
            case LessThan:
               if (parsable) {
                  return da < db;
               }

               return false;
            case GreaterThanOrEquals:
               if (parsable) {
                  return da >= db;
               }

               return false;
            case LessThanOrEquals:
               if (parsable) {
                  return da <= db;
               }

               return false;
            default:
               return a.equals(b);
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

         return Equality;
      }
   }
}
