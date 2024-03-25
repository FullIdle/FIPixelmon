package com.pixelmonmod.pixelmon.quests.client;

public class QuestNotification {
   private Type type;
   private String[] strings;
   private int age;
   private int maxAge;

   static enum Type {
      NEW_QUEST,
      OBJECTIVE_COMPLETE,
      QUEST_COMPLETE,
      QUEST_FAILED;
   }
}
