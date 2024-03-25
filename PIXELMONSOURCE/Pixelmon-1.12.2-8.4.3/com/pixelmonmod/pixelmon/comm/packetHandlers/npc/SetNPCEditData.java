package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiChattingNPCEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiQuestGiverNPCEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiShopkeeperEditor;
import com.pixelmonmod.pixelmon.client.gui.npcEditor.GuiTrainerEditor;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetNPCEditData implements IMessage {
   SetTrainerData data;
   EnumNPCType npcType;
   ArrayList chatPages;
   String name;
   String json;
   String texture;

   public SetNPCEditData() {
   }

   public SetNPCEditData(SetTrainerData data) {
      this.npcType = EnumNPCType.Trainer;
      this.data = data;
   }

   public SetNPCEditData(String name, ArrayList chatPages) {
      this(name, chatPages, EnumNPCType.ChattingNPC);
   }

   public SetNPCEditData(String name, ArrayList chatPages, EnumNPCType type) {
      this.name = name;
      this.npcType = type;
      this.chatPages = chatPages;
   }

   public SetNPCEditData(String json, String name, String texture) {
      this.json = json;
      this.name = name;
      this.texture = texture;
      this.npcType = EnumNPCType.Shopkeeper;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.npcType.ordinal());
      switch (this.npcType) {
         case Trainer:
            this.data.encodeInto(buffer);
            break;
         case ChattingNPC:
         case QuestGiver:
            ByteBufUtils.writeUTF8String(buffer, this.name);
            buffer.writeShort(this.chatPages.size());
            Iterator var2 = this.chatPages.iterator();

            while(var2.hasNext()) {
               String page = (String)var2.next();
               ByteBufUtils.writeUTF8String(buffer, page);
            }

            return;
         case Shopkeeper:
            ByteBufUtils.writeUTF8String(buffer, this.name);
            ByteBufUtils.writeUTF8String(buffer, this.json);
            ByteBufUtils.writeUTF8String(buffer, this.texture);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.npcType = EnumNPCType.getFromOrdinal(buffer.readShort());
      switch (this.npcType) {
         case Trainer:
            this.data = new SetTrainerData();
            this.data.decodeInto(buffer);
            break;
         case ChattingNPC:
         case QuestGiver:
            this.name = ByteBufUtils.readUTF8String(buffer);
            this.chatPages = new ArrayList();
            int numPages = buffer.readShort();

            for(int i = 0; i < numPages; ++i) {
               this.chatPages.add(ByteBufUtils.readUTF8String(buffer));
            }

            return;
         case Shopkeeper:
            this.name = ByteBufUtils.readUTF8String(buffer);
            this.json = ByteBufUtils.readUTF8String(buffer);
            this.texture = ByteBufUtils.readUTF8String(buffer);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetNPCEditData message, MessageContext ctx) {
         switch (message.npcType) {
            case Trainer:
               GuiTrainerEditor.trainerData = message.data;
               break;
            case ChattingNPC:
               GuiChattingNPCEditor.name = message.name;
               GuiChattingNPCEditor.chatPages = message.chatPages;
               GuiChattingNPCEditor.chatChanged = true;
               break;
            case QuestGiver:
               GuiQuestGiverNPCEditor.name = message.name;
               GuiQuestGiverNPCEditor.chatPages = message.chatPages;
               GuiQuestGiverNPCEditor.chatChanged = true;
               break;
            case Shopkeeper:
               Minecraft mc = Minecraft.func_71410_x();
               mc.func_152344_a(() -> {
                  GuiShopkeeperEditor.json = message.json;
                  GuiShopkeeperEditor.name = message.name;
                  if (mc.field_71462_r instanceof GuiShopkeeperEditor) {
                     ((GuiShopkeeperEditor)mc.field_71462_r).updateShopkeeper(message.texture);
                  }

               });
         }

         return null;
      }
   }
}
