package com.pixelmonmod.pixelmon.quests.objectives;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.editor.IQuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElement;
import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.quest.ArgsIn;
import com.pixelmonmod.pixelmon.quests.quest.Arguments;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.ArrayList;

public interface IObjective extends IQuestElement {
   String identifier();

   Arguments parse(Quest var1, Stage var2, ArgsIn var3) throws InvalidQuestArgsException;

   boolean test(Stage var1, QuestData var2, QuestProgress var3, Objective var4, Arguments var5, Context var6) throws InvalidQuestArgsException;

   default ArrayList mark(Stage stage, QuestProgress progress, Objective objective, int objectiveIndex, Arguments arguments, Context context) {
      return new ArrayList();
   }

   default int quantity(Stage stage, QuestData data, QuestProgress progress, Objective objective, Arguments arguments) {
      return 1;
   }

   default QuestElement getStructure() {
      return new QuestElement(QuestElementType.OBJECTIVE, this.identifier(), true);
   }
}
