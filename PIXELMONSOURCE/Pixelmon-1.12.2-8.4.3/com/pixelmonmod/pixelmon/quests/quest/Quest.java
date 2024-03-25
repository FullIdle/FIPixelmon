package com.pixelmonmod.pixelmon.quests.quest;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.exceptions.InvalidQuestArgsException;
import com.pixelmonmod.pixelmon.util.helpers.TextHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Quest {
   public static Class standardQuest = Quest.class;
   private int weight;
   private boolean abandonable;
   private boolean repeatable;
   private QuestColor color;
   private short activeStage;
   private ArrayList stages;
   private HashMap strings;
   private transient HashMap mappedStages;
   private transient String filename;
   private static final String[] newQuestNames = new String[]{"The Phantom Quest", "Attack of the Quests", "Revenge of the Quest", "A New Quest", "The Quest Strikes Back", "Return of the Quest", "The Quest Awakens", "The Last Quest", "The Rise of Quest"};

   public Quest() {
      this.mappedStages = new HashMap();
      this.stages = new ArrayList();
      this.weight = 0;
      this.abandonable = true;
      this.color = new QuestColor(255, 255, 0);
      this.activeStage = 10;
      this.strings = new HashMap();
   }

   public Quest(String filename) {
      this();
      this.filename = filename;
      this.stages.add(new Stage(0, 10, "BLOCKER"));
      this.stages.add(new Stage(10, -1, "BLOCKER"));
      this.setDefaultStrings();
      this.setDefaultStrings(0, 1);
      this.setDefaultStrings(10, 1);
   }

   public Quest(Quest toCopy, String newFilename) {
      this.mappedStages = new HashMap();
      this.weight = toCopy.weight;
      this.abandonable = toCopy.abandonable;
      this.repeatable = toCopy.repeatable;
      this.color = new QuestColor(toCopy.color.getRGB());
      this.activeStage = toCopy.activeStage;
      this.stages = new ArrayList();
      Iterator var3 = toCopy.stages.iterator();

      while(var3.hasNext()) {
         Stage stage = (Stage)var3.next();
         this.stages.add(new Stage(stage));
      }

      this.strings = new HashMap(toCopy.strings);
      this.filename = newFilename;
   }

   public Quest(ByteBuf buf) {
      this.mappedStages = new HashMap();
      this.readFromByteBuf(buf);
   }

   public void putNewStringIfNeeded(String key, String value) {
      if (!I18n.func_94522_b(this.getLangKey(key))) {
         this.strings.putIfAbsent(key, value);
      }

   }

   public void setDefaultStrings() {
      this.putNewStringIfNeeded("name", newQuestNames[RandomHelper.rand.nextInt(9)]);
      this.putNewStringIfNeeded("desc-X", "Quest Complete");
   }

   public void setDefaultStrings(int stage, int objectives) {
      this.putNewStringIfNeeded("desc-" + stage, "Stage " + stage + " Description");

      for(int i = 0; i < objectives; ++i) {
         this.setDefaultStrings(stage, i, true);
      }

   }

   public void setDefaultStrings(int stage, int objective, boolean unused) {
      this.putNewStringIfNeeded("stage-" + stage + "-" + objective, "Complete an Objective");
   }

   public void clearDefaultStrings(int stage, int objectives) {
      this.strings.remove("desc-" + stage);

      for(int i = 0; i < objectives; ++i) {
         this.clearDefaultStrings(stage, i, true);
      }

   }

   public void clearDefaultStrings(int stage, int objective, boolean unused) {
      this.strings.remove("stage-" + stage + "-" + objective);
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }

   public void parseAndMap() throws InvalidQuestArgsException {
      Iterator var1 = this.stages.iterator();

      while(var1.hasNext()) {
         Stage stage = (Stage)var1.next();
         stage.parse(this);
         this.mappedStages.put(stage.getStage(), stage);
      }

   }

   public String getFilename() {
      return this.filename;
   }

   public String getIdentityName() {
      return this.filename.replace(" ", "_").replace(".json", "");
   }

   public String getPrintableName() {
      return this.getIdentityName().replace("_", " ");
   }

   public QuestColor getColor() {
      return this.color;
   }

   public void setColor(QuestColor color) {
      this.color = color;
   }

   public int getWeight() {
      return this.weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   public boolean isRepeatable() {
      return this.repeatable;
   }

   public void toggleRepeatability() {
      this.repeatable = !this.repeatable;
   }

   public boolean isAbandonable() {
      return this.abandonable;
   }

   public void toggleAbandonability() {
      this.abandonable = !this.abandonable;
   }

   public short getActiveStage() {
      return this.activeStage;
   }

   public void setActiveStage(short activeStage) {
      this.activeStage = activeStage;
   }

   public ArrayList getStages() {
      return this.stages;
   }

   public Stage getStage(QuestProgress progress) {
      Stage stage = (Stage)this.mappedStages.get(progress.getStage());
      if (stage == null) {
         Stage previousStage = null;

         Stage s;
         for(Iterator var4 = this.stages.iterator(); var4.hasNext(); previousStage = s) {
            s = (Stage)var4.next();
            if (s.getStage() > progress.getStage()) {
               if (previousStage == null) {
                  progress.setStage((short)0);
               } else {
                  progress.setStage(previousStage.getStage());
               }

               stage = (Stage)this.mappedStages.get(progress.getStage());
               break;
            }
         }
      }

      return stage;
   }

   public Stage getStage(short stage) {
      Iterator var2 = this.stages.iterator();

      Stage s;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         s = (Stage)var2.next();
      } while(s.getStage() != stage);

      return s;
   }

   public Stage getNextStage(short stage) {
      short next = -1;
      Iterator var3 = this.stages.iterator();

      while(true) {
         Stage s;
         do {
            if (!var3.hasNext()) {
               if (next == -1) {
                  return null;
               }

               return this.getStage(next);
            }

            s = (Stage)var3.next();
         } while(s.getStage() >= next && next != -1);

         if (s.getStage() > stage) {
            next = s.getStage();
         }
      }
   }

   public String getUnlocalizedString(String key) {
      return this.getUnlocalizedString(key, (QuestProgress)null);
   }

   public String getUnlocalizedString(String key, @Nullable QuestProgress progress) {
      String string = (String)this.strings.get(key);
      if (string != null && !string.isEmpty()) {
         return TextHelper.format(string);
      } else {
         return I18n.func_94522_b(key) ? key : this.getLangKey(key);
      }
   }

   public String getLangKey(String key) {
      return ("quest." + this.getIdentityName() + "." + key).toLowerCase();
   }

   public HashMap getUnlocalizedStringMap() {
      return this.strings;
   }

   public void putUnlocalizedString(String key, String value) {
      this.strings.put(key, value);
   }

   public void removeUnlocalizedString(String key) {
      this.strings.remove(key);
   }

   public static Builder builder() {
      return new Builder();
   }

   public void readFromByteBuf(ByteBuf buf) {
      this.weight = buf.readInt();
      this.abandonable = buf.readBoolean();
      this.repeatable = buf.readBoolean();
      this.color = new QuestColor(buf);
      this.activeStage = buf.readShort();
      this.stages = new ArrayList();
      int stageCount = buf.readShort();

      for(int i = 0; i < stageCount; ++i) {
         this.stages.add(new Stage(buf));
      }

      this.strings = new HashMap();
      int stringCount = buf.readShort();

      for(int i = 0; i < stringCount; ++i) {
         this.strings.put(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf));
      }

      this.filename = ByteBufUtils.readUTF8String(buf);
   }

   public void writeToByteBuf(ByteBuf buf) {
      buf.writeInt(this.weight);
      buf.writeBoolean(this.abandonable);
      buf.writeBoolean(this.repeatable);
      this.color.writeToByteBuf(buf);
      buf.writeShort(this.activeStage);
      buf.writeShort(this.stages.size());
      Iterator var2 = this.stages.iterator();

      while(var2.hasNext()) {
         Stage stage = (Stage)var2.next();
         stage.writeToByteBuf(buf);
      }

      buf.writeShort(this.strings.size());
      var2 = this.strings.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry entry = (Map.Entry)var2.next();
         ByteBufUtils.writeUTF8String(buf, (String)entry.getKey());
         ByteBufUtils.writeUTF8String(buf, (String)entry.getValue());
      }

      if (this.filename == null) {
         this.filename = "?";
      }

      ByteBufUtils.writeUTF8String(buf, this.filename);
   }

   public static class Builder {
      private final Quest quest = new Quest();

      public Builder setColor(QuestColor color) {
         this.quest.color = color;
         return this;
      }

      public Builder setRepeatable(boolean repeatable) {
         this.quest.repeatable = repeatable;
         return this;
      }

      public Builder setAbandonable(boolean abandonable) {
         this.quest.abandonable = abandonable;
         return this;
      }

      public Builder setWeight(int weight) {
         this.quest.weight = weight;
         return this;
      }

      public Builder setActiveStage(short activeStage) {
         this.quest.activeStage = activeStage;
         return this;
      }

      public Builder addString(String key, String value) {
         this.quest.strings.put(key, value);
         return this;
      }

      public Stage.Builder addStage() {
         return new Stage.Builder(this);
      }

      void addStage(Stage stage) {
         this.quest.stages.add(stage);
      }

      public Quest build(String identifier) throws InvalidQuestArgsException {
         this.quest.filename = identifier;
         this.quest.parseAndMap();
         this.quest.setDefaultStrings();
         return this.quest;
      }
   }
}
