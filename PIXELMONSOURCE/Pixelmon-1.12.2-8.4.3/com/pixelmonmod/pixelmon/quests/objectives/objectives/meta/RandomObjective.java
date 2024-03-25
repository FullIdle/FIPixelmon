package com.pixelmonmod.pixelmon.quests.objectives.objectives.meta;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.Action;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RandomObjective implements IObjective {
   public String identifier() {
      return "RANDOM";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("actions", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("weights", false, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      Dice dice = new Dice();

      for(int i = 0; i < args.size(); ++i) {
         String[] split = args.get(i).split(":");
         int weight = Integer.parseInt(split[0]);
         String[] split2 = split[1].split(",");
         Integer[] actions = new Integer[split2.length];

         for(int j = 0; j < split2.length; ++j) {
            actions[j] = Integer.parseInt(split2[j]);
         }

         dice.addWeight(weight, actions);
      }

      dice.setTotal();
      return Arguments.create(Argument.of(dice));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) throws InvalidQuestArgsException {
      Dice dice = (Dice)arguments.value(0, progress);
      List actions = dice.roll();
      Iterator var9 = actions.iterator();

      while(var9.hasNext()) {
         int action = (Integer)var9.next();
         Action a = (Action)stage.getParsedActions().get(action);
         a.execute(progress.getQuest(), stage, data, progress);
      }

      return false;
   }

   private static class Dice {
      private int total;
      private final HashMap map;

      private Dice() {
         this.map = new HashMap();
      }

      public void addWeight(int weight, Integer... actions) {
         this.map.put(weight, Arrays.asList(actions));
      }

      public void setTotal() {
         this.total = 0;

         int weight;
         for(Iterator var1 = this.map.keySet().iterator(); var1.hasNext(); this.total += weight) {
            weight = (Integer)var1.next();
         }

      }

      public List roll() {
         int roll = RandomHelper.rand.nextInt(this.total);
         int runningTotal = 0;

         Map.Entry entry;
         for(Iterator var3 = this.map.entrySet().iterator(); var3.hasNext(); runningTotal += (Integer)entry.getKey()) {
            entry = (Map.Entry)var3.next();
            if ((Integer)entry.getKey() + runningTotal > roll) {
               return (List)entry.getValue();
            }
         }

         return new ArrayList();
      }

      // $FF: synthetic method
      Dice(Object x0) {
         this();
      }
   }
}
