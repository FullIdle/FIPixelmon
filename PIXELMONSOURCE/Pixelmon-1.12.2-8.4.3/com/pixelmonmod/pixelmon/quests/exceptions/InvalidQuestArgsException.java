package com.pixelmonmod.pixelmon.quests.exceptions;

import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;

public class InvalidQuestArgsException extends Exception {
   public InvalidQuestArgsException() {
   }

   public InvalidQuestArgsException(String message) {
      super(message);
   }

   public InvalidQuestArgsException(String key, Quest quest, Stage stage) {
      super(key + " - " + quest.getFilename() + ", stage " + stage.getStage());
   }
}
