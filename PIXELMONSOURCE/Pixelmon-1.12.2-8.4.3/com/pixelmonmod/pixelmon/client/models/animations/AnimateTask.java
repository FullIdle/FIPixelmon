package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.blocks.tileEntities.IFrameCounter;
import java.util.Timer;
import java.util.TimerTask;

public class AnimateTask extends TimerTask {
   public static Timer timer = new Timer("Pixelmon Animation Thread");
   IFrameCounter frameCounter;
   int startFrame;
   int endFrame;

   public AnimateTask(IFrameCounter frameCounter, int startFrame, int endFrame) {
      this.frameCounter = frameCounter;
      this.startFrame = startFrame;
      this.endFrame = endFrame;
      frameCounter.setFrame(startFrame);
   }

   public void run() {
      if (this.frameCounter.getFrame() != this.endFrame) {
         this.frameCounter.setFrame(this.frameCounter.getFrame() + 1);
      } else {
         this.cancel();
      }

   }
}
