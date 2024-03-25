package com.pixelmonmod.pixelmon.quests.client;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.StageIcon;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

public class QuestProgressClient extends QuestProgressClientData {
   public QuestProgressClient() {
   }

   public QuestProgressClient(ByteBuf buf) {
      this.read(buf);
   }

   public String format(ObjectiveDetail detail) {
      return this.format(detail.getKey(), detail);
   }

   public String format(String key) {
      return this.format(key, (ObjectiveDetail)null);
   }

   public String format(String key, @Nullable ObjectiveDetail detail) {
      if (key == null) {
         key = "";
      }

      String text = I18n.func_188566_a(key) ? I18n.func_135052_a(key, new Object[0]) : key;
      if (!text.startsWith("quest.") && !text.isEmpty()) {
         if (detail != null) {
            text = detail.applyPlaceholders(text);
         }

         if (text.contains("?")) {
            Iterator var4;
            Map.Entry entry;
            for(var4 = this.dataLongMap.entrySet().iterator(); var4.hasNext(); text = text.replace("?" + (String)entry.getKey() + "?", Long.toString((Long)entry.getValue()))) {
               entry = (Map.Entry)var4.next();
            }

            for(var4 = this.dataStringMap.entrySet().iterator(); var4.hasNext(); text = text.replace("?" + (String)entry.getKey() + "?", I18n.func_135052_a((String)entry.getValue(), new Object[0]))) {
               entry = (Map.Entry)var4.next();
            }
         }

         return text;
      } else {
         return "";
      }
   }

   public String getFilename() {
      return this.filename;
   }

   public UUID getIdentifier() {
      return this.identifier;
   }

   public ArrayList getMarkers() {
      return this.markers;
   }

   public QuestColor getColor() {
      if (this.isSpecialColor() && this.color.getR() == -1) {
         World world = Minecraft.func_71410_x().field_71441_e;
         long mod = world.func_72820_D() % 120L;
         if (mod > 60L) {
            mod -= (mod - 60L) * 2L;
         }

         mod = (long)((int)((double)mod * 1.7));
         return new QuestColor(28, 65 + (int)mod, 138);
      } else {
         return this.color;
      }
   }

   public boolean isSpecialColor() {
      return this.specialColor;
   }

   public boolean isAbandonable() {
      return this.abandonable;
   }

   public boolean isRepeatable() {
      return this.repeatable;
   }

   public int getWeight() {
      return this.weight;
   }

   public StageIcon getIcon() {
      return this.icon;
   }

   public boolean isReadyForTurnIn() {
      return this.readyForTurnIn;
   }

   public boolean isComplete() {
      return this.complete;
   }

   public String getName() {
      String name = this.format(this.name);
      return name != null && !name.isEmpty() ? name : null;
   }

   public ArrayList getObjectives() {
      return this.objectives;
   }

   public String getDesc() {
      return this.format(this.desc);
   }

   public void update() {
      QuestDataClient.getInstance().update(this.filename, this);
   }

   public void read(ByteBuf buf) {
      super.read(buf);
   }

   public void write(QuestProgress progress, ByteBuf buf) {
      super.write(progress, buf);
   }
}
