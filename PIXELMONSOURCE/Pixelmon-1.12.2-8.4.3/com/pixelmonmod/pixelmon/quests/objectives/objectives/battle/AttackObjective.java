package com.pixelmonmod.pixelmon.quests.objectives.objectives.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
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

public class AttackObjective implements IObjective {
   private final String identifier;

   public AttackObjective(String identifier) {
      this.identifier = identifier;
   }

   public String identifier() {
      return this.identifier;
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("attacks", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("count", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("type", true, false, ArgumentType.TYPE, EnumType.getTypeNames(true)), new QuestElementArgument("category", true, false, ArgumentType.TEXT, new String[]{"Physical", "Special", "Status"}), new QuestElementArgument("result", true, false, ArgumentType.TEXT, new String[]{"proceed", "hit", "ignore", "killed", "succeeded", "charging", "unable", "failed", "missed", "notarget"}), new QuestElementArgument("damage", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("full_damage", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("accuracy", true, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("sound_based", true, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("user_spec", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("target_spec", true, false, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return s.split(",");
      }), Argument.from(args.get(1), Integer::parseInt, 1), Argument.from(args.get(2), EnumType::parseOrNull), Argument.from(args.get(3), AttackCategory::parseOrNull), Argument.from(args.get(4), AttackResult::parseOrNull), Argument.from(args.get(5), Bound::new), Argument.from(args.get(6), Bound::new), Argument.from(args.get(7), Bound::new), Argument.from(args.get(8), Boolean::parseBoolean), Argument.from(args.get(9), (s) -> {
         return PokemonSpec.from(s.replace("_", " ").split(" "));
      }), Argument.from(args.get(10), (s) -> {
         return PokemonSpec.from(s.replace("_", " ").split(" "));
      }));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      Pokemon user = (Pokemon)context.get(0);
      Pokemon target = (Pokemon)context.get(1);
      Attack attack = (Attack)context.get(2);
      MoveResults results = (MoveResults)context.get(3);
      String[] attacks = (String[])arguments.value(0, progress);
      EnumType type = (EnumType)arguments.value(2, progress);
      AttackCategory category = (AttackCategory)arguments.value(3, progress);
      AttackResult result = (AttackResult)arguments.value(4, progress);
      Bound damage = (Bound)arguments.value(5, progress);
      Bound fullDamage = (Bound)arguments.value(6, progress);
      Bound accuracy = (Bound)arguments.value(7, progress);
      Boolean soundBased = (Boolean)arguments.value(8, progress);
      PokemonSpec userSpec = (PokemonSpec)arguments.value(9, progress);
      PokemonSpec targetSpec = (PokemonSpec)arguments.value(10, progress);
      if (soundBased != null && soundBased != attack.isSoundBased()) {
         return false;
      } else if (category != null && attack.getAttackCategory() != category) {
         return false;
      } else if (type != null && attack.getType() != type) {
         return false;
      } else if (attacks != null && !attack.isAttack(attacks)) {
         return false;
      } else if (userSpec != null && !userSpec.matches(user)) {
         return false;
      } else if (targetSpec != null && !targetSpec.matches(target)) {
         return false;
      } else if (damage != null && !damage.isValid(results.damage)) {
         return false;
      } else if (fullDamage != null && !fullDamage.isValid(results.fullDamage)) {
         return false;
      } else if (accuracy != null && !accuracy.isValid(results.accuracy)) {
         return false;
      } else {
         return result == null || result == results.result;
      }
   }

   public int quantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return arguments.quantity(1, progress);
   }

   static class Bound {
      private final Mode mode;
      private final int value;

      public Bound(String arg) {
         this.mode = arg.startsWith("<") ? AttackObjective.Mode.LESS : (arg.startsWith(">") ? AttackObjective.Mode.MORE : AttackObjective.Mode.EQUAL);
         if (arg.startsWith("<") || arg.startsWith(">") || arg.startsWith("=")) {
            arg = arg.substring(1);
         }

         this.value = Integer.parseInt(arg);
      }

      public Bound(Mode mode, int value) {
         this.mode = mode;
         this.value = value;
      }

      public boolean isValid(int value) {
         switch (this.mode) {
            case EQUAL:
               return this.value == value;
            case LESS:
               return this.value >= value;
            case MORE:
               return this.value <= value;
            default:
               return false;
         }
      }
   }

   static enum Mode {
      LESS("<"),
      EQUAL("="),
      MORE(">");

      private final String symbol;

      private Mode(String s) {
         this.symbol = s;
      }

      public String toString() {
         return this.symbol;
      }
   }
}
