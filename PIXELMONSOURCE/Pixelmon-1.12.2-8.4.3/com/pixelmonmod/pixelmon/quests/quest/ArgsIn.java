package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.quests.editor.QuestElementType;

public class ArgsIn {
   private final String[] args;

   public ArgsIn(String[] args, QuestElementType elementType) {
      this.args = new String[args.length - elementType.getArgsOffset()];
      System.arraycopy(args, elementType.getArgsOffset(), this.args, 0, this.args.length);
   }

   public String get(int index) {
      if (index >= 0 && index < this.args.length) {
         String arg = this.args[index];
         return arg.isEmpty() ? "-" : arg;
      } else {
         return "-";
      }
   }

   public int size() {
      return this.args.length;
   }

   public int usableSize() {
      int count = 0;

      for(int i = 0; i < this.size(); ++i) {
         if (!this.get(i).equalsIgnoreCase("-")) {
            ++count;
         }
      }

      return count;
   }
}
