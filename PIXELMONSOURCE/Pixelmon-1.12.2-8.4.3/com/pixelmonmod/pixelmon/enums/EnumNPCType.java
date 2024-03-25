package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCFisherman;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCQuestGiver;
import com.pixelmonmod.pixelmon.entities.npcs.NPCRelearner;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import net.minecraft.world.World;

public enum EnumNPCType {
   Trainer("Trainer"),
   ChattingNPC("Chat"),
   Relearner("Relearner"),
   Tutor("Tutor"),
   TransferTutor("TransferTutor"),
   Trader("Trader"),
   Shopkeeper("Shopkeeper"),
   NurseJoy("Nurse"),
   Doctor("Doctor"),
   OldFisherman("OldFisherman"),
   QuestGiver("QuestGiver");

   private String defaultName;

   private EnumNPCType(String defaultName) {
      this.defaultName = defaultName;
   }

   public String getDefaultName() {
      return this.defaultName;
   }

   public static EnumNPCType getFromOrdinal(short ordinal) {
      return values()[ordinal];
   }

   public static EnumNPCType getFromOrdinal(int ordinal) {
      return values()[ordinal];
   }

   public static EnumNPCType getFromString(String npcType) {
      EnumNPCType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumNPCType type = var1[var3];
         if (type.defaultName.equalsIgnoreCase(npcType) || type.name().equalsIgnoreCase(npcType)) {
            return type;
         }
      }

      if (npcType.equalsIgnoreCase("chat")) {
         return ChattingNPC;
      } else {
         return null;
      }
   }

   public EntityNPC constructNew(World world) {
      switch (this) {
         case Tutor:
            return new NPCTutor(world);
         case Doctor:
            return new NPCNurseJoy(world, 0);
         case NurseJoy:
            return new NPCNurseJoy(world, 1);
         case Trader:
            return new NPCTrader(world);
         case Trainer:
            return new NPCTrainer(world);
         case Relearner:
            return new NPCRelearner(world);
         case QuestGiver:
            return new NPCQuestGiver(world);
         case Shopkeeper:
            return new NPCShopkeeper(world);
         case ChattingNPC:
            return new NPCChatting(world);
         case OldFisherman:
            return new NPCFisherman(world);
         default:
            return null;
      }
   }
}
