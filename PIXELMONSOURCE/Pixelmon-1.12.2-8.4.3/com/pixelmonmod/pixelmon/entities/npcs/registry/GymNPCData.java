package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import java.util.ArrayList;

public class GymNPCData implements ITrainerData {
   public String id;
   public EnumNPCType type;
   ArrayList names = new ArrayList();
   public ArrayList textures = new ArrayList();
   ArrayList chat = new ArrayList();
   ArrayList trainerChat = new ArrayList();
   public int winnings;

   public GymNPCData(String id) {
      this.id = id;
   }

   void addName(String name) {
      this.names.add(name);
   }

   void addTexture(String texture) {
      this.textures.add(texture);
   }

   void addChat(String... lines) {
      if (this.type == EnumNPCType.ChattingNPC) {
         this.chat.add(lines);
      } else if (this.type == EnumNPCType.Trainer && lines.length == 3) {
         this.trainerChat.add(new TrainerChat(lines[0], lines[1], lines[2]));
      }

   }

   public int getRandomTextureIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.textures.size() - 1);
   }

   public String getRandomTexture() {
      return (String)RandomHelper.getRandomElementFromList(this.textures);
   }

   public int getRandomChatIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.chat.size() - 1);
   }

   public int getRandomTrainerChatIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.trainerChat.size() - 1);
   }

   public int getRandomNameIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.names.size() - 1);
   }

   public String getName(int index) {
      return index < this.names.size() && index >= 0 ? (String)this.names.get(index) : (String)this.names.get(0);
   }

   public TrainerChat getChat(int index) {
      return index < this.trainerChat.size() && index >= 0 ? (TrainerChat)this.trainerChat.get(index) : (TrainerChat)this.trainerChat.get(0);
   }
}
