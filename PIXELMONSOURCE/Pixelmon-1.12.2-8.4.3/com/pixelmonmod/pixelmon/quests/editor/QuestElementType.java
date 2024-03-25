package com.pixelmonmod.pixelmon.quests.editor;

public enum QuestElementType {
   OBJECTIVE(1),
   ACTION(2);

   private final int argsOffset;

   private QuestElementType(int argsOffset) {
      this.argsOffset = argsOffset;
   }

   public int getArgsOffset() {
      return this.argsOffset;
   }
}
