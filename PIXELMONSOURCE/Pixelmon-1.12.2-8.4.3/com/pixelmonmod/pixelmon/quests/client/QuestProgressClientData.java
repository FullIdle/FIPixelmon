package com.pixelmonmod.pixelmon.quests.client;

import com.pixelmonmod.pixelmon.quests.QuestProgress;
import com.pixelmonmod.pixelmon.quests.comm.QuestMarker;
import com.pixelmonmod.pixelmon.quests.objectives.Objective;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import com.pixelmonmod.pixelmon.quests.quest.StageIcon;
import com.pixelmonmod.pixelmon.util.helpers.MapHelper;
import com.pixelmonmod.pixelmon.util.helpers.UUIDHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class QuestProgressClientData {
   protected String filename;
   protected UUID identifier;
   protected QuestColor color;
   protected boolean specialColor;
   protected boolean abandonable;
   protected boolean repeatable;
   protected int weight;
   protected String name;
   protected String desc;
   protected ArrayList objectives;
   protected ArrayList markers;
   protected StageIcon icon;
   protected boolean readyForTurnIn;
   protected boolean complete;
   protected boolean failed;
   protected HashMap dataLongMap;
   protected HashMap dataStringMap;
   protected boolean visible;

   public QuestProgressClientData() {
      this.icon = StageIcon.EXCLAMATION_MARK;
   }

   public QuestProgressClientData(ByteBuf buf) {
      this.icon = StageIcon.EXCLAMATION_MARK;
      this.read(buf);
   }

   public void read(ByteBuf buf) {
      this.filename = ByteBufUtils.readUTF8String(buf);
      this.identifier = UUIDHelper.readUUID(buf);
      this.dataLongMap = MapHelper.readStringLongMapFromByteBuf(buf);
      this.dataStringMap = MapHelper.readStringStringMapFromByteBuf(buf);
      this.visible = buf.readBoolean();
      int sizeA;
      int i;
      if (this.visible) {
         this.specialColor = buf.readBoolean();
         if (this.specialColor) {
            this.color = new QuestColor(buf.readInt(), 0, 0);
         } else {
            this.color = new QuestColor(buf.readInt());
         }

         this.abandonable = buf.readBoolean();
         this.repeatable = buf.readBoolean();
         this.weight = buf.readInt();
         this.name = ByteBufUtils.readUTF8String(buf);
         this.desc = ByteBufUtils.readUTF8String(buf);
         this.objectives = new ArrayList();
         sizeA = buf.readInt();

         for(i = 0; i < sizeA; ++i) {
            if (buf.readBoolean()) {
               ObjectiveDetail od = new ObjectiveDetail(ByteBufUtils.readUTF8String(buf), buf.readShort(), buf.readShort(), buf.readBoolean());
               if (!od.isComplete()) {
                  this.objectives.add(od);
               }
            }
         }
      }

      this.markers = new ArrayList();
      sizeA = buf.readInt();

      for(i = 0; i < sizeA; ++i) {
         this.markers.add(new QuestMarker(buf));
      }

      this.icon = StageIcon.getIcon(buf.readByte());
      this.readyForTurnIn = buf.readBoolean();
      this.complete = buf.readBoolean();
      this.failed = buf.readBoolean();
   }

   public void write(QuestProgress progress, ByteBuf buf) {
      Quest quest = progress.getQuest();
      boolean detail = progress.isComplete() || progress.getStage() >= quest.getActiveStage();
      ByteBufUtils.writeUTF8String(buf, quest.getFilename());
      UUIDHelper.writeUUID(progress.getIdentifier(), buf);
      MapHelper.writeStringLongMapToByteBuf(buf, progress.getDataLongMap());
      MapHelper.writeStringStringMapToByteBuf(buf, progress.getDataStringMap());
      buf.writeBoolean(detail);
      if (detail) {
         buf.writeBoolean(quest.getColor().getR() < 0);
         buf.writeInt(quest.getColor().getR() < 0 ? quest.getColor().getR() : quest.getColor().getRGB());
         buf.writeBoolean(progress.isAbandonable());
         buf.writeBoolean(quest.isRepeatable());
         buf.writeInt(quest.getWeight());
         ByteBufUtils.writeUTF8String(buf, quest.getUnlocalizedString("name", progress));
         ByteBufUtils.writeUTF8String(buf, quest.getUnlocalizedString(progress.isComplete() ? "desc-X" : "desc-" + progress.getStage(), progress));
         Stage stage = progress.getCurrentStage();
         buf.writeInt(progress.isComplete() ? 0 : stage.getParsedObjectives().size());
         int i = 0;
         if (!progress.isComplete()) {
            for(Iterator var7 = stage.getParsedObjectives().iterator(); var7.hasNext(); ++i) {
               Objective objective = (Objective)var7.next();
               String name = quest.getUnlocalizedString("stage-" + stage.getStage() + "-" + i, progress);
               if (name != null && !name.isEmpty()) {
                  buf.writeBoolean(true);
                  ByteBufUtils.writeUTF8String(buf, name);
                  buf.writeShort(progress.getObjectiveQuantityComplete(i));
                  buf.writeShort(progress.getObjectiveTotalQuantity(i));
                  buf.writeBoolean(progress.isObjectiveComplete(i));
               } else {
                  buf.writeBoolean(false);
               }
            }
         }
      }

      ArrayList markers = progress.getMarkers();
      buf.writeInt(markers.size());
      Iterator var11 = markers.iterator();

      while(var11.hasNext()) {
         QuestMarker marker = (QuestMarker)var11.next();
         marker.write(buf);
      }

      buf.writeByte(progress.getCurrentStage().getIcon().ordinal());
      buf.writeBoolean(progress.getNextStage() == null);
      buf.writeBoolean(progress.isComplete());
      buf.writeBoolean(progress.isFailed());
   }
}
