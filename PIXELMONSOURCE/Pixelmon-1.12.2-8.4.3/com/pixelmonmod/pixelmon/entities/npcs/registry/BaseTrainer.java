package com.pixelmonmod.pixelmon.entities.npcs.registry;

import java.util.ArrayList;

public class BaseTrainer {
   public static int _index = 0;
   public int id;
   public String name;
   public ArrayList textures = new ArrayList();

   public BaseTrainer(String name) {
      this.name = name;
      this.id = _index++;
   }

   public void addTexture(String texture) {
      this.textures.add(texture);
   }
}
