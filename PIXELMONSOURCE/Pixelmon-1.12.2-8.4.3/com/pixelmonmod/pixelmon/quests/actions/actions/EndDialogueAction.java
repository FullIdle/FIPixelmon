package com.pixelmonmod.pixelmon.quests.actions.actions;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.actions.IAction;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.editor.args.QuestElementArgument;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public class EndDialogueAction implements IAction {
   public String identifier() {
      return "END_DIALOGUE";
   }

   public QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), new QuestElementArgument[0]);
   }

   public Arguments parse(Quest quest, Stage stage, ArgsIn args) {
      return Arguments.empty();
   }

   public void execute(Quest quest, Stage stage, QuestData data, QuestProgress progress, Arguments arguments) {
      data.getPlayer().func_71053_j();
   }
}
