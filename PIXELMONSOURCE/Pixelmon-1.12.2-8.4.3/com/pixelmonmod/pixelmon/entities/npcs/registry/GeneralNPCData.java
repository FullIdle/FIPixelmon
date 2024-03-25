package com.pixelmonmod.pixelmon.entities.npcs.registry;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;

public class GeneralNPCData {
   public String id;
   ArrayList names = new ArrayList();
   ArrayList textures = new ArrayList();
   ArrayList chat = new ArrayList();

   public GeneralNPCData(String id) {
      this.id = id;
   }

   void addName(String name) {
      this.names.add(name);
   }

   void addTexture(String texture) {
      this.textures.add(texture);
   }

   void addChat(String... lines) {
      this.chat.add(lines);
   }

   public String getRandomTexture() {
      return (String)RandomHelper.getRandomElementFromList(this.textures);
   }

   public ArrayList getTextures() {
      return this.textures;
   }

   public int getRandomChatIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.chat.size() - 1);
   }

   public int getRandomNameIndex() {
      return RandomHelper.getRandomNumberBetween(0, this.names.size() - 1);
   }
}
