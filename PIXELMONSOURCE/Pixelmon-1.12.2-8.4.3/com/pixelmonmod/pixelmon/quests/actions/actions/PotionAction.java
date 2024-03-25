package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.ArgumentType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Argument;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionAction implements IAction {
   public String identifier() {
      return "POTION";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("potion", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("duration", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("amplitude", false, false, ArgumentType.WHOLE_NUMBER, new String[0]), new QuestElementArgument("ambient", false, false, ArgumentType.BOOLEAN, new String[0]), new QuestElementArgument("particles", false, false, ArgumentType.BOOLEAN, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.create(Argument.from(args.get(0), Potion::func_180142_b, MobEffects.field_76424_c), Argument.from(args.get(1), Integer::parseInt, 0), Argument.from(args.get(2), Integer::parseInt, 0), Argument.from(args.get(3), Boolean::parseBoolean, false), Argument.from(args.get(4), Boolean::parseBoolean, true));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      data.getPlayer().func_70690_d(new PotionEffect((Potion)arguments.value(0, progress), (Integer)arguments.value(1, progress), (Integer)arguments.value(2, progress), (Boolean)arguments.value(3, progress), (Boolean)arguments.value(4, progress)));
   }
}
