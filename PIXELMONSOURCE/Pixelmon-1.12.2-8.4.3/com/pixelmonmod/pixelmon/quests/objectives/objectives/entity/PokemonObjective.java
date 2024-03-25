package com.pixelmonmod.pixelmon.quests.objectives.objectives.entity;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
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

public class PokemonObjective implements IObjective {
   private final String identifier;

   public PokemonObjective(String identifier) {
      this.identifier = identifier;
   }

   public int quantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return arguments.quantity(1, progress);
   }

   public String identifier() {
      return this.identifier;
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("spec", true, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("count", false, false, ArgumentType.WHOLE_NUMBER, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), (s) -> {
         return !s.equalsIgnoreCase("-") && !s.equalsIgnoreCase("any") ? PokemonSpec.from(s.replace("_", " ").split(" ")) : new PokemonSpec(new String[0]);
      }, new PokemonSpec(new String[0])), Argument.from(args.get(1), Integer::parseInt, 1));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) {
      Pokemon pokemon = (Pokemon)context.get(0);
      PokemonSpec spec = (PokemonSpec)arguments.value(0, progress);
      return spec == null || spec.args == null || spec.args.length == 0 || spec.matches(pokemon);
   }
}
