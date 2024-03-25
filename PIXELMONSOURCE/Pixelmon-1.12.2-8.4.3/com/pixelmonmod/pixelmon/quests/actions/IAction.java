package com.pixelmonmod.pixelmon.quests.actions;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.editor.IQuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;

public interface IAction extends IQuestElement {
   String identifier();

   Arguments parse(Quest var1, Stage var2, ArgsIn var3) throws InvalidQuestArgsException;

   void execute(Quest var1, Stage var2, QuestData var3, QuestProgress var4, Arguments var5) throws InvalidQuestArgsException;

   default QuestElement getStructure() {
      return new QuestElement(QuestElementType.ACTION, this.identifier(), true);
   }
}
