package com.pixelmonmod.pixelmon.quests.objectives.objectives.entity;

import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.api.events.NPCChatEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
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
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import java.util.UUID;
import net.minecraft.entity.Entity;

public class DialogueInjectObjective implements IObjective {
   public String identifier() {
      return "DIALOGUE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), new QuestElementArgument[]{new QuestElementArgument("entity_uuid", false, false, ArgumentType.TEXT, new String[0]), new QuestElementArgument("name", false, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("dialogue", false, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_1", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_2", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_3", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_4", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_5", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_6", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_7", true, true, ArgumentType.TEXT, new String[0]), new QuestElementArgument("choice_8", true, true, ArgumentType.TEXT, new String[0])});
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      Dialogue.DialogueBuilder builder = Dialogue.builder();
      int choiceID = 0;

      for(int i = 1; i <= args.size(); ++i) {
         switch (i - 1) {
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

      return Arguments.create(Argument.from(args.get(0), UUIDHelper::questUUID), Argument.of(builder));
   }

   public boolean test(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments, Context context) throws InvalidQuestArgsException {
      Entity npc = (Entity)context.get(0);
      if (npc != null) {
         UUID uuid = (UUID)arguments.value(0, progress);
         if (uuid != null && npc.getPersistentID().equals(uuid)) {
            if (npc instanceof NPCQuestGiver && context.size() > 1 && context.get(1) != null) {
               NPCChatEvent event = (NPCChatEvent)context.get(1);
               event.setCanceled(true);
            }

            Dialogue.DialogueBuilder builder = (Dialogue.DialogueBuilder)arguments.value(1, progress);
            builder.injectHandler((eventx) -> {
               data.receiveMultipleInternal(new String[]{"NPC_RESPOND", "NPC_TALK", "NPC_SHOW", "NPC_GIVE"}, new Object[][]{{npc, eventx.choice.choiceID}, {npc}, {npc}, {npc}}, true);
            });
            builder.build().open(progress.getIdentifier(), data.getPlayer());
         }
      }

      return false;
   }
}
