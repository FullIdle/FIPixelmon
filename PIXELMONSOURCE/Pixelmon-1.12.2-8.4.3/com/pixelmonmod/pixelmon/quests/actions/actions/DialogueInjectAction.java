package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueNextAction;
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

public class DialogueInjectAction implements IAction {
   public String identifier() {
      return "DIALOGUE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("name", false, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("dialogue", false, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_1", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_2", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_3", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_4", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_5", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_6", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_7", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_8", true, true, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      Dialogue.DialogueBuilder builder = Dialogue.builder();
      int choiceID = 0;

      for(int i = 0; i < args.size(); ++i) {
         switch (i) {
            case 0:
               builder.setName(quest.getUnlocalizedString(args.get(i)));
               break;
            case 1:
               builder.setText(quest.getUnlocalizedString(args.get(i)));
               break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
               if (!args.get(i).equalsIgnoreCase("-")) {
                  builder.addChoice(Choice.builder().setText(quest.getUnlocalizedString(args.get(i))).build(choiceID++));
               }
         }
      }

      return Arguments.create(Argument.of(builder));
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      Dialogue.DialogueBuilder builder = (Dialogue.DialogueBuilder)arguments.value(0, progress);
      builder.injectHandler((event) -> {
         event.setAction(DialogueNextAction.DialogueGuiAction.CLOSE);
      });
      builder.build().open(progress.getIdentifier(), data.getPlayer());
   }
}
