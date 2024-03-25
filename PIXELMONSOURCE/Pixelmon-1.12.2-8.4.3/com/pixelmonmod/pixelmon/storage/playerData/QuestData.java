package com.pixelmonmod.pixelmon.storage.playerData;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.quests.AbandonQuestEvent;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.QuestRegistry;
import com.pixelmonmod.pixelmon.quests.comm.HideDisplayQuest;
import com.pixelmonmod.pixelmon.quests.comm.SetDisplayQuest;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class QuestData implements ISaveData {
   private final ArrayList progress = new ArrayList();
   private transient EntityPlayerMP player;
   private final transient ArrayList activeQuests = new ArrayList();
   private final transient ArrayList completeQuests = new ArrayList();

   public QuestData get(EntityPlayerMP player, boolean update) {
      this.player = player;
      if (update) {
         this.activeQuests.clear();
         this.completeQuests.clear();
         Iterator iterator = this.progress.iterator();

         while(iterator.hasNext()) {
            QuestProgress progress = (QuestProgress)iterator.next();
            if (QuestRegistry.getInstance().getQuest(progress.getQuestFilename()) == null) {
               iterator.remove();
            } else if (!progress.isComplete()) {
               this.activeQuests.add(progress.getQuestFilename());
            } else {
               this.completeQuests.add(progress.getQuestFilename());
            }
         }

         iterator = QuestRegistry.getInstance().getQuestCollection().iterator();

         while(true) {
            Quest quest;
            do {
               do {
                  if (!iterator.hasNext()) {
                     return this;
                  }

                  quest = (Quest)iterator.next();
               } while(this.activeQuests.contains(quest.getFilename()));
            } while(this.completeQuests.contains(quest.getFilename()) && !quest.isRepeatable());

            QuestProgress newQuest = new QuestProgress(quest, this);
            this.progress.add(newQuest);
            newQuest.sendTo(player);
         }
      } else {
         return this;
      }
   }

   public EntityPlayerMP getPlayer() {
      return this.player;
   }

   public ArrayList getProgress() {
      return this.progress;
   }

   public QuestProgress getProgressForQuest(String filename) {
      return this.getProgressForQuest(filename, false);
   }

   public QuestProgress getProgressForQuest(String filename, boolean underscoreSpaces) {
      Iterator var3 = this.getProgress().iterator();

      QuestProgress progress;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         progress = (QuestProgress)var3.next();
      } while(!progress.getQuestFilename().equalsIgnoreCase(filename) && (!underscoreSpaces || !progress.getQuestFilename().equalsIgnoreCase(filename.replace("_", " ") + ".json")));

      return progress;
   }

   public void receiveInternal(String identifier, boolean breakOnSuccess, Object... args) {
      try {
         Iterator var4 = this.progress.iterator();

         label42:
         while(true) {
            while(true) {
               QuestProgress progress;
               do {
                  if (!var4.hasNext()) {
                     break label42;
                  }

                  progress = (QuestProgress)var4.next();
               } while(progress.isComplete());

               Stage stage = progress.getCurrentStage();
               int i = 0;
               if (stage.getParsedObjectives() != null) {
                  Iterator var8 = stage.getParsedObjectives().iterator();

                  while(var8.hasNext()) {
                     Objective objective = (Objective)var8.next();
                     if (objective.receive(identifier, stage, this, progress, i++, args) && breakOnSuccess) {
                        break label42;
                     }
                  }
               } else {
                  progress.complete(this.player);
               }
            }
         }

         this.get(this.player, true);
      } catch (InvalidQuestArgsException var10) {
         var10.printStackTrace();
      }

   }

   public void receive(String identifier, Object... args) throws InvalidQuestArgsException {
      this.receiveInternal(identifier, false, args);
   }

   public void receiveMultipleInternal(String[] identifiers, Object[][] args, boolean breakOnSuccess) {
      try {
         Iterator var4 = this.progress.iterator();

         label52:
         while(true) {
            while(true) {
               QuestProgress progress;
               do {
                  if (!var4.hasNext()) {
                     break label52;
                  }

                  progress = (QuestProgress)var4.next();
               } while(progress.isComplete());

               Stage stage = progress.getCurrentStage();
               int i = 0;
               if (stage.getParsedObjectives() != null) {
                  for(Iterator var8 = stage.getParsedObjectives().iterator(); var8.hasNext(); ++i) {
                     Objective objective = (Objective)var8.next();

                     for(int o = 0; o < identifiers.length; ++o) {
                        if (objective.receive(identifiers[o], stage, this, progress, i, args[o]) && breakOnSuccess) {
                           break label52;
                        }
                     }
                  }
               } else {
                  progress.complete(this.player);
               }
            }
         }

         this.get(this.player, true);
      } catch (InvalidQuestArgsException var11) {
         var11.printStackTrace();
      }

   }

   public void receiveMultiple(String[] identifiers, Object[][] args) throws InvalidQuestArgsException {
      this.receiveMultipleInternal(identifiers, args, false);
   }

   public void abandonQuest(String filename, UUID identifier) {
      Iterator var3 = this.progress.iterator();

      while(var3.hasNext()) {
         QuestProgress progress = (QuestProgress)var3.next();
         if (progress.getQuestFilename().equals(filename) && progress.getIdentifier().equals(identifier) && progress.isAbandonable() && !progress.isComplete() && progress.getStage() >= progress.getQuest().getActiveStage() && !Pixelmon.EVENT_BUS.post(new AbandonQuestEvent(this.player, progress))) {
            progress.fail(this.player);
         }
      }

   }

   public void setTracking(QuestProgress progress) {
      if (this.player != null) {
         Pixelmon.network.sendTo(new SetDisplayQuest(progress), this.player);
      }

   }

   public void hideTracking() {
      if (this.player != null) {
         Pixelmon.network.sendTo(new HideDisplayQuest(), this.player);
      }

   }

   public void writeToNBT(NBTTagCompound nbt) {
      NBTTagList tagList = new NBTTagList();
      Iterator var3 = this.progress.iterator();

      while(var3.hasNext()) {
         QuestProgress progress = (QuestProgress)var3.next();
         NBTTagCompound tag = new NBTTagCompound();
         progress.writeToNBT(tag);
         tagList.func_74742_a(tag);
      }

      nbt.func_74782_a("Quests", tagList);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.progress.clear();
      if (nbt.func_74764_b("Quests")) {
         NBTTagList list = nbt.func_150295_c("Quests", 10);

         for(int i = 0; i < list.func_74745_c(); ++i) {
            NBTTagCompound tag = (NBTTagCompound)list.func_179238_g(i);
            String filename = tag.func_74779_i("QuestFilename");
            if (QuestRegistry.getInstance().getQuest(filename) == null) {
               Pixelmon.LOGGER.warn("Found invalid quest data " + filename + ", throwing out (A)");
            } else {
               try {
                  this.progress.add(new QuestProgress(tag, this));
               } catch (Exception var7) {
                  Pixelmon.LOGGER.warn("Found invalid quest data " + filename + ", throwing out (B)");
                  var7.printStackTrace();
               }
            }
         }
      }

   }
}
