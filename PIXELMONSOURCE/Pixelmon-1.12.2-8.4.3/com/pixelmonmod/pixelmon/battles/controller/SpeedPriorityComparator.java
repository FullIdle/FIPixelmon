package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class SpeedPriorityComparator extends SpeedComparator {
   protected boolean doesGoFirst(PixelmonWrapper p, PixelmonWrapper foe) {
      if (p.priority != foe.priority) {
         return p.priority > foe.priority;
      } else {
         return super.doesGoFirst(p, foe);
      }
   }
}
