package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.quests.actions.ExecutorMode;

public class Executor {
   private final ExecutorMode mode;
   private final int[] executors;

   public Executor(ExecutorMode mode, int[] executors) {
      this.mode = mode;
      this.executors = executors;
   }

   public ExecutorMode getMode() {
      return this.mode;
   }

   public int[] getExecutors() {
      return this.executors;
   }
}
