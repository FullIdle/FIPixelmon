package com.pixelmonmod.pixelmon.client.gui.pokedex;

import java.util.function.Consumer;

public class AnimationHelper {
   private int maxFrame;
   private int frame;
   private int level;
   private Consumer updator;

   public AnimationHelper(int maxFrame, Consumer updator) {
      this.maxFrame = maxFrame;
      this.updator = updator;
   }

   public AnimationHelper(int maxFrame, int level, Consumer updator) {
      this.maxFrame = maxFrame;
      this.level = level;
      this.updator = updator;
   }

   public boolean update() {
      this.updator.accept(this.frame);
      return this.frame == this.maxFrame || ++this.frame == this.maxFrame;
   }

   public boolean isComplete() {
      return this.frame == this.maxFrame;
   }

   public int getLevel() {
      return this.level;
   }
}
