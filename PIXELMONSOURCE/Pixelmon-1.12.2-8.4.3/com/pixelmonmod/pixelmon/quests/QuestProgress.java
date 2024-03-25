package com.pixelmonmod.pixelmon.quests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.quests.FinishQuestEvent;
import com.pixelmonmod.pixelmon.api.events.quests.QuestObjectiveEvent;
import com.pixelmonmod.pixelmon.api.events.quests.QuestStageEvent;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.comm.SendQuestData;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.objectives.objectives.entity.DialogueInjectObjective;
import com.pixelmonmod.pixelmon.quests.quest.Context;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.storage.playerData.ISaveData;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.FakePlayer;

public class QuestProgress implements ISaveData {
   private short stage;
   private HashMap data1;
   private HashMap data2;
   private int[] objectiveCompletion;
   private int[] objectiveCompletionReference;
   private boolean started;
   private boolean complete;
   private boolean failed;
   private Boolean abandonable = null;
   private String questFilename;
   private UUID identifier;
   private QuestData parent;
   private transient Quest quest = null;

   public QuestProgress(Quest quest, QuestData parent) {
      this.questFilename = quest.getFilename();
      this.identifier = UUID.randomUUID();
      this.stage = 0;
      this.data1 = new HashMap();
      this.data2 = new HashMap();
      this.initQuantities();
      this.started = false;
      this.complete = false;
      this.failed = false;
      this.abandonable = quest.isAbandonable();
      this.parent = parent;
   }

   public void start() {
      this.started = true;
   }

   public void fail(EntityPlayerMP player) {
      Pixelmon.EVENT_BUS.post(new FinishQuestEvent.Fail(player, this));
      this.failed = true;
      this.stage = 0;
      this.data1.clear();
      this.data2.clear();
      this.complete(player);
      this.initQuantities();
   }

   public void complete(EntityPlayerMP player) {
      Pixelmon.EVENT_BUS.post(new FinishQuestEvent.Complete(player, this));
      this.complete = true;
      this.objectiveCompletion = new int[0];
      this.objectiveCompletionReference = new int[0];
      this.sendTo(player);
   }

   public boolean isStarted() {
      return this.started;
   }

   public boolean isComplete() {
      return this.complete && !this.failed;
   }

   public boolean isFailed() {
      return this.failed;
   }

   public void initQuantities() {
      Stage stage = this.getCurrentStage();
      if (stage != null) {
         int size = stage.getParsedObjectives().size();
         this.objectiveCompletion = new int[size];
         this.objectiveCompletionReference = new int[size];

         for(int i = 0; i < this.objectiveCompletion.length; ++i) {
            Objective objective = (Objective)stage.getParsedObjectives().get(i);
            this.objectiveCompletion[i] = objective.getQuantity(stage, this.parent, this, objective, objective.getData());
            this.objectiveCompletionReference[i] = this.objectiveCompletion[i];
         }
      }

   }

   public void reopen() {
      this.complete = false;
      this.failed = false;
   }

   public Long getDataLong(String key) {
      return (Long)this.data1.getOrDefault(key, (Object)null);
   }

   public String getDataString(String key) {
      return (String)this.data2.getOrDefault(key, (Object)null);
   }

   public String getData(String key) {
      String s = this.getDataString(key);
      if (s == null) {
         Long l = this.getDataLong(key);
         if (l != null) {
            s = String.valueOf(l);
         }
      }

      return s;
   }

   public HashMap getDataLongMap() {
      return this.data1;
   }

   public HashMap getDataStringMap() {
      return this.data2;
   }

   public ArrayList getDataUUIDs() {
      ArrayList uuids = new ArrayList();
      Iterator var2 = this.data2.values().iterator();

      while(var2.hasNext()) {
         String str = (String)var2.next();

         try {
            uuids.add(UUID.fromString(str));
         } catch (Exception var5) {
         }
      }

      return uuids;
   }

   public void setData(String key, long value) {
      this.data1.put(key, value);
   }

   public void setData(String key, String value) {
      this.data2.put(key, value);
   }

   public void sendTo(EntityPlayerMP player) {
      if (!(player instanceof FakePlayer) && player != null) {
         Pixelmon.network.sendTo(new SendQuestData(this), player);
      }

   }

   public short getStage() {
      return this.stage;
   }

   public Stage getCurrentStage() {
      return this.getQuest().getStage(this);
   }

   public Stage getNextStage() {
      return this.getQuest().getNextStage(this.stage);
   }

   public boolean setStage(short stage) {
      short prevStage = this.stage;
      this.stage = stage;
      Stage s = this.getQuest().getStage(this);
      if (s != null && s.getParsedObjectives() != null) {
         int objArrSize = s.getParsedObjectives().size();
         this.objectiveCompletion = new int[objArrSize];
         this.objectiveCompletionReference = new int[objArrSize];

         for(int i = 0; i < this.objectiveCompletion.length; ++i) {
            Objective objective = (Objective)s.getParsedObjectives().get(i);
            this.objectiveCompletion[i] = objective.getQuantity(s, this.parent, this, objective, objective.getData());
            if (Pixelmon.devEnvironment) {
               System.out.println("o: " + objective);
            }

            this.objectiveCompletionReference[i] = this.objectiveCompletion[i];
         }

         Pixelmon.EVENT_BUS.post(new QuestStageEvent.Set(this.parent.getPlayer(), this, this.getCurrentStage(), false));
         return true;
      } else {
         this.stage = prevStage;
         Pixelmon.EVENT_BUS.post(new QuestStageEvent.Set(this.parent.getPlayer(), this, this.getCurrentStage(), true));
         return false;
      }
   }

   private boolean tryDialogueContinuity(QuestData quest, Stage oldStage, Stage newStage) throws InvalidQuestArgsException {
      if (oldStage != null && newStage != null) {
         Objective oldObjA = null;
         Objective newObjA = null;
         DialogueInjectObjective oldObjB = null;
         DialogueInjectObjective newObjB = null;
         Iterator var8 = oldStage.getParsedObjectives().iterator();

         Objective newObjTest;
         while(var8.hasNext()) {
            newObjTest = (Objective)var8.next();
            if (newObjTest.getInternalObjective() instanceof DialogueInjectObjective) {
               oldObjA = newObjTest;
               oldObjB = (DialogueInjectObjective)newObjTest.getInternalObjective();
               break;
            }
         }

         var8 = newStage.getParsedObjectives().iterator();

         while(var8.hasNext()) {
            newObjTest = (Objective)var8.next();
            if (newObjTest.getInternalObjective() instanceof DialogueInjectObjective) {
               newObjA = newObjTest;
               newObjB = (DialogueInjectObjective)newObjTest.getInternalObjective();
               break;
            }
         }

         if (oldObjA != null && newObjA != null && oldObjB != null && newObjB != null) {
            UUID oldUUID = (UUID)oldObjA.getData().value(0, this);
            UUID newUUID = (UUID)newObjA.getData().value(0, this);
            if (oldUUID != null && oldUUID.equals(newUUID)) {
               newObjB.test(newStage, quest, this, newObjA, newObjA.getData(), new Context(new Object[]{quest.getPlayer().func_71121_q().func_175733_a(newUUID)}));
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean moveStage(QuestData quest, Stage oldStage, short newStageID) throws InvalidQuestArgsException {
      this.setStage(newStageID);
      Stage newStage = this.getCurrentStage();
      return this.tryDialogueContinuity(quest, oldStage, newStage);
   }

   public void completeObjective(int index) {
      Stage stage = this.getCurrentStage();
      if (index < this.objectiveCompletion.length) {
         QuestObjectiveEvent.Progress event = new QuestObjectiveEvent.Progress(this.parent.getPlayer(), this, stage, (Objective)stage.getParsedObjectives().get(index), index, this.objectiveCompletion[index]);
         Pixelmon.EVENT_BUS.post(event);
         this.objectiveCompletion[index] = event.completion;
         if (this.objectiveCompletion[index] > 0) {
            int var10002 = this.objectiveCompletion[index]--;
         }

      }
   }

   public void resetObjective(int index) {
      if (index < this.objectiveCompletion.length) {
         this.objectiveCompletion[index] = 1;
      }
   }

   public boolean isObjectiveComplete(int index) {
      if (index >= this.objectiveCompletion.length) {
         return false;
      } else {
         return this.objectiveCompletion[index] == 0;
      }
   }

   public int getObjectiveQuantityComplete(int index) {
      return index >= this.objectiveCompletion.length ? 0 : this.objectiveCompletionReference[index] - this.objectiveCompletion[index];
   }

   public int getObjectiveTotalQuantity(int index) {
      return index >= this.objectiveCompletion.length ? 0 : this.objectiveCompletionReference[index];
   }

   public boolean canProgress() {
      int[] var1 = this.objectiveCompletion;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int i = var1[var3];
         if (i > 0) {
            return false;
         }
      }

      return true;
   }

   public QuestMarker.Type getMarkerType() {
      return this.getNextStage() == null ? QuestMarker.Type.QUESTION : QuestMarker.Type.EXCLAMATION;
   }

   public ArrayList getMarkers() {
      Stage stage = this.getCurrentStage();
      ArrayList markers = new ArrayList();
      int i = 0;
      Iterator var4 = stage.getParsedObjectives().iterator();

      while(var4.hasNext()) {
         Objective objective = (Objective)var4.next();
         markers.addAll(objective.mark(stage, this, i++, objective.getData()));
      }

      return markers;
   }

   public String getQuestFilename() {
      return this.questFilename;
   }

   public Quest getQuest() {
      if (this.quest == null) {
         Iterator var1 = QuestRegistry.getInstance().getQuestCollection().iterator();

         while(var1.hasNext()) {
            Quest quest = (Quest)var1.next();
            if (quest.getFilename().equals(this.getQuestFilename())) {
               this.quest = quest;
               break;
            }
         }
      }

      return this.quest;
   }

   public void invalidate() {
      this.quest = null;
   }

   public UUID getIdentifier() {
      return this.identifier;
   }

   public boolean isAbandonable() {
      if (this.abandonable == null) {
         this.abandonable = this.getQuest().isAbandonable();
      }

      return this.abandonable;
   }

   public void setAbandonable(boolean abandonable) {
      this.abandonable = abandonable;
   }

   public QuestProgress(NBTTagCompound nbt, QuestData data) {
      this.readFromNBT(nbt);
      this.parent = data;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      nbt.func_74777_a("QuestStage", this.stage);
      NBTTagList data1TagList = new NBTTagList();
      Iterator var3 = this.data1.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry entry = (Map.Entry)var3.next();
         NBTTagCompound kvPair = new NBTTagCompound();
         kvPair.func_74778_a("QuestDataKey", (String)entry.getKey());
         kvPair.func_74772_a("QuestDataValue", (Long)entry.getValue());
         data1TagList.func_74742_a(kvPair);
      }

      nbt.func_74782_a("QuestData1", data1TagList);
      NBTTagList data2TagList = new NBTTagList();
      Iterator var8 = this.data2.entrySet().iterator();

      while(var8.hasNext()) {
         Map.Entry entry = (Map.Entry)var8.next();
         NBTTagCompound kvPair = new NBTTagCompound();
         kvPair.func_74778_a("QuestDataKey", (String)entry.getKey());
         kvPair.func_74778_a("QuestDataValue", (String)entry.getValue());
         data2TagList.func_74742_a(kvPair);
      }

      nbt.func_74782_a("QuestData2", data2TagList);
      nbt.func_74757_a("QuestStarted", this.started);
      nbt.func_74757_a("QuestComplete", this.complete);
      nbt.func_74757_a("QuestFailed", this.failed);
      nbt.func_74757_a("QuestAbandonable", this.isAbandonable());
      if (this.objectiveCompletion == null) {
         this.objectiveCompletion = new int[this.getCurrentStage().getParsedObjectives().size()];

         for(int i = 0; i < this.objectiveCompletion.length; ++i) {
            Objective objective = (Objective)this.getCurrentStage().getParsedObjectives().get(i);
            this.objectiveCompletion[i] = objective.getQuantity(this.getCurrentStage(), this.parent, this, objective, objective.getData());
         }
      }

      int[] array = new int[this.objectiveCompletion.length];
      System.arraycopy(this.objectiveCompletion, 0, array, 0, array.length);
      nbt.func_74783_a("QuestCompletion", array);
      int[] array2 = new int[this.objectiveCompletionReference.length];
      System.arraycopy(this.objectiveCompletionReference, 0, array2, 0, array2.length);
      nbt.func_74783_a("QuestCompletionRef", array2);
      nbt.func_74778_a("QuestFilename", this.questFilename);
      nbt.func_186854_a("QuestIdentifier", this.identifier);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.questFilename = nbt.func_74779_i("QuestFilename");
      this.identifier = nbt.func_186857_a("QuestIdentifier");
      this.stage = nbt.func_74765_d("QuestStage");
      if (this.data1 == null) {
         this.data1 = new HashMap();
      } else {
         this.data1.clear();
      }

      NBTTagList list;
      int i;
      NBTTagCompound kvPair;
      if (nbt.func_74764_b("QuestData1")) {
         list = nbt.func_150295_c("QuestData1", 10);

         for(i = 0; i < list.func_74745_c(); ++i) {
            kvPair = (NBTTagCompound)list.func_179238_g(i);
            this.data1.put(kvPair.func_74779_i("QuestDataKey"), kvPair.func_74763_f("QuestDataValue"));
         }
      }

      if (this.data2 == null) {
         this.data2 = new HashMap();
      } else {
         this.data2.clear();
      }

      if (nbt.func_74764_b("QuestData2")) {
         list = nbt.func_150295_c("QuestData2", 10);

         for(i = 0; i < list.func_74745_c(); ++i) {
            kvPair = (NBTTagCompound)list.func_179238_g(i);
            this.data2.put(kvPair.func_74779_i("QuestDataKey"), kvPair.func_74779_i("QuestDataValue"));
         }
      }

      this.started = nbt.func_74767_n("QuestStarted");
      this.complete = nbt.func_74767_n("QuestComplete");
      this.failed = nbt.func_74767_n("QuestFailed");
      if (nbt.func_74764_b("QuestAbandonable")) {
         this.abandonable = nbt.func_74767_n("QuestAbandonable");
      } else {
         this.abandonable = this.getQuest().isAbandonable();
      }

      int objectivesLen = this.getCurrentStage().getParsedObjectives().size();
      this.objectiveCompletion = new int[objectivesLen];
      this.objectiveCompletionReference = new int[objectivesLen];
      int[] loadedObjectiveCompletion = nbt.func_74759_k("QuestCompletion");
      int[] loadedObjectiveCompletionReference = nbt.func_74759_k("QuestCompletionRef");
      int i;
      if (loadedObjectiveCompletion.length == objectivesLen && loadedObjectiveCompletionReference.length == objectivesLen) {
         i = 0;
         int[] var14 = loadedObjectiveCompletion;
         int var7 = loadedObjectiveCompletion.length;

         int var8;
         int value;
         for(var8 = 0; var8 < var7; ++var8) {
            value = var14[var8];
            if (i >= this.objectiveCompletion.length) {
               break;
            }

            this.objectiveCompletion[i++] = (short)value;
         }

         int j = 0;
         int[] var16 = loadedObjectiveCompletionReference;
         var8 = loadedObjectiveCompletionReference.length;

         for(value = 0; value < var8; ++value) {
            int value = var16[value];
            if (j >= this.objectiveCompletionReference.length) {
               break;
            }

            this.objectiveCompletionReference[j++] = (short)value;
         }
      } else {
         Pixelmon.LOGGER.warn("Found invalid objective progress data in an instance of quest " + this.getQuestFilename() + ", resetting that instance!");

         for(i = 0; i < objectivesLen; ++i) {
            Objective objective = (Objective)this.getCurrentStage().getParsedObjectives().get(i);
            this.objectiveCompletion[i] = objective.getQuantity(this.getCurrentStage(), this.parent, this, objective, objective.getData());
            this.objectiveCompletionReference[i] = this.objectiveCompletion[i];
         }
      }

   }

   public QuestData getParent() {
      return this.parent;
   }
}
