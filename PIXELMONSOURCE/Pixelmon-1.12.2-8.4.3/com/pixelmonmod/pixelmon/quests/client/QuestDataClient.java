package com.pixelmonmod.pixelmon.quests.client;

import com.pixelmonmod.pixelmon.quests.client.ui.GuiQuests;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class QuestDataClient {
   private static final QuestDataClient INSTANCE = new QuestDataClient();
   private final LinkedHashMap quests = new LinkedHashMap();
   private final LinkedHashMap completeQuests = new LinkedHashMap();
   private int displayQuest = -1;

   public static QuestDataClient getInstance() {
      return INSTANCE;
   }

   private QuestDataClient() {
   }

   public QuestProgressClient getDisplayQuest() {
      if (this.displayQuest >= this.quests.size()) {
         this.displayQuest = 0;
      }

      int i = 0;

      for(Iterator var2 = this.quests.values().iterator(); var2.hasNext(); ++i) {
         QuestProgressClient qpc = (QuestProgressClient)var2.next();
         if (this.displayQuest == i) {
            return qpc;
         }
      }

      return null;
   }

   public void setDisplayQuest(QuestProgressClient qpcIn) {
      if (qpcIn == null) {
         this.displayQuest = -1;
      } else {
         int i = 0;

         for(Iterator var3 = this.quests.values().iterator(); var3.hasNext(); ++i) {
            QuestProgressClient qpc = (QuestProgressClient)var3.next();
            if (qpc == qpcIn) {
               this.displayQuest = i;
               return;
            }
         }

      }
   }

   public void cycleDisplayQuest() {
      boolean next = false;
      int i = 0;
      Iterator var3 = getInstance().getQuests().iterator();

      while(true) {
         while(var3.hasNext()) {
            QuestProgressClient qpc = (QuestProgressClient)var3.next();
            if (qpc.getName() != null) {
               if (this.displayQuest == -1) {
                  this.displayQuest = i;
                  return;
               }

               if (i == this.displayQuest) {
                  next = true;
                  ++i;
                  continue;
               }

               if (next) {
                  this.displayQuest = i;
                  return;
               }
            }

            ++i;
         }

         this.displayQuest = -1;
         return;
      }
   }

   public void clear() {
      this.quests.clear();
      this.completeQuests.clear();
   }

   public void update(String filename, QuestProgressClient quest) {
      if (filename != null && !filename.isEmpty()) {
         if (quest.isComplete()) {
            this.quests.remove(filename);
            this.completeQuests.put(filename, quest);
         } else {
            this.quests.put(filename, quest);
         }
      }

      GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
      if (screen instanceof GuiQuests) {
         GuiQuests questsGUI = (GuiQuests)screen;
         questsGUI.refreshQuests();
      }

   }

   public void remove(String key) {
      this.quests.remove(key);
   }

   public QuestProgressClient get(UUID uuid) {
      Iterator var2 = this.getQuests().iterator();

      QuestProgressClient qpc;
      do {
         if (!var2.hasNext()) {
            var2 = this.getCompleteQuests().iterator();

            do {
               if (!var2.hasNext()) {
                  return null;
               }

               qpc = (QuestProgressClient)var2.next();
            } while(!uuid.equals(qpc.getIdentifier()));

            return qpc;
         }

         qpc = (QuestProgressClient)var2.next();
      } while(!uuid.equals(qpc.getIdentifier()));

      return qpc;
   }

   public QuestProgressClient getVisible(UUID uuid) {
      Iterator var2 = this.getQuests().iterator();

      QuestProgressClient qpc;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         qpc = (QuestProgressClient)var2.next();
      } while(!uuid.equals(qpc.getIdentifier()) || qpc.isComplete() || !qpc.visible);

      return qpc;
   }

   public Collection getQuests() {
      return this.quests.values();
   }

   public Collection getCompleteQuests() {
      return this.completeQuests.values();
   }
}
