package com.pixelmonmod.pixelmon.quests.objectives.objectives.inserters;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
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
import java.util.ArrayList;

public class InsertSpecObjective implements IObjective {
   private final Mode mode;

   public InsertSpecObjective(Mode mode) {
      this.mode = mode;
   }

   public String identifier() {
      switch (this.mode) {
         case DEX_VALUES:
            return "DEX_VALUES_SPEC_INSERTER";
         case TYPE_VALUES:
            return "TYPE_VALUES_SPEC_INSERTER";
         default:
            return "DEX_VALUES_SPEC_INSERTER";
      }
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s;
      }), Argument.from(args.get(1), (s) -> {
         Object targets;
         String[] type;
         if (this.mode == InsertSpecObjective.Mode.DEX_VALUES) {
            type = args.get(1).split(",");
            if (!args.get(1).equalsIgnoreCase("-") && !type[0].equalsIgnoreCase("any")) {
               ArrayList values = new ArrayList();
               String[] var6 = type;
               int var7 = type.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  String value = var6[var8];
                  if (value.contains("-")) {
                     String[] split = value.split("-");
                     int a = Integer.parseInt(split[0]);
                     int b = Integer.parseInt(split[1]);

                     for(int i = a; i <= b; ++i) {
                        values.add(i);
                     }
                  } else {
                     values.add(Integer.parseInt(value));
                  }
               }

               targets = values.toArray(new Integer[0]);
            } else {
               targets = new Integer[0];
            }
         } else {
            type = args.get(1).split(",");
            if (!args.get(1).isEmpty() && !args.get(1).equalsIgnoreCase("-") && !type[0].equalsIgnoreCase("any")) {
               EnumType[] types = new EnumType[type.length];

               for(int ix = 0; ix < type.length; ++ix) {
                  types[ix] = EnumType.parseType(type[ix]);
               }

               targets = types;
            } else {
               targets = new EnumType[0];
            }
         }

         return targets;
      }, (Object)null), Argument.from(args.get(2), (s) -> {
         String[] natureSplit = s.split(",");
         EnumNature[] natures = !s.equalsIgnoreCase("-") && !natureSplit[0].equalsIgnoreCase("any") ? new EnumNature[natureSplit.length] : new EnumNature[0];

         for(int i = 0; i < natures.length; ++i) {
            natures[i] = EnumNature.natureFromString(natureSplit[i]);
         }

         return natures;
      }, (Object)null), Argument.from(args.get(3), (s) -> {
         String[] growthSplit = s.split(",");
         EnumGrowth[] growths = !s.equalsIgnoreCase("-") && !growthSplit[0].equalsIgnoreCase("any") ? new EnumGrowth[growthSplit.length] : new EnumGrowth[0];

         for(int i = 0; i < growths.length; ++i) {
            growths[i] = EnumGrowth.growthFromString(growthSplit[i]);
         }

         return growths;
      }, (Object)null));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      String key = (String)arguments.value(0, progress);
      Object targets = arguments.value(1, progress);
      EnumNature[] natures = (EnumNature[])arguments.value(2, progress);
      EnumGrowth[] growths = (EnumGrowth[])arguments.value(3, progress);
      String testStr = progress.getDataString(key);
      if (testStr != null && !testStr.isEmpty()) {
         return false;
      } else {
         EnumType type = null;
         EnumGrowth growth = null;
         EnumSpecies species = null;
         EnumNature nature = null;
         StringBuilder spec = new StringBuilder();
         int index;
         if (this.mode == InsertSpecObjective.Mode.DEX_VALUES) {
            Integer[] values = (Integer[])((Integer[])targets);
            if (values != null && values.length != 0) {
               index = RandomHelper.rand.nextInt(values.length);
               species = EnumSpecies.getFromDex(values[index]);
            } else {
               species = EnumSpecies.randomPoke(false);
            }

            if (species != null) {
               spec.append(species.getPokemonName());
            }
         } else {
            EnumType[] types = (EnumType[])((EnumType[])targets);
            if (types != null && types.length != 0) {
               index = RandomHelper.rand.nextInt(types.length);
               type = types[index];
            } else {
               do {
                  type = EnumType.values()[RandomHelper.rand.nextInt(EnumType.values().length)];
               } while(type == EnumType.Mystery);
            }

            spec.append("type:").append(type.func_176610_l());
         }

         int index;
         if (natures != null && natures.length > 0) {
            index = RandomHelper.rand.nextInt(natures.length);
            nature = natures[index];
            spec.append(" nature:").append(nature.name());
         }

         if (growths != null && growths.length > 0) {
            index = RandomHelper.rand.nextInt(growths.length);
            growth = growths[index];
            spec.append(" growth:").append(growth.name());
         }

         progress.setData(key, spec.toString());
         if (species != null) {
            progress.setData(key + "_S", species.getUnlocalizedName());
         }

         if (type != null) {
            progress.setData(key + "_T", type.getUnlocalizedName());
         }

         if (nature != null) {
            progress.setData(key + "_N", nature.getUnlocalizedName());
         }

         if (growth != null) {
            progress.setData(key + "_G", growth.getUnlocalizedName());
         }

         progress.sendTo(data.getPlayer());
         return true;
      }
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("key", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument(this.mode == InsertSpecObjective.Mode.DEX_VALUES ? "dex_values" : "type_values", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("natures", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("growths", true, false, ArgumentType.TEXT, new String[0])});
   }

   public static enum Mode {
      DEX_VALUES,
      TYPE_VALUES;
   }
}
