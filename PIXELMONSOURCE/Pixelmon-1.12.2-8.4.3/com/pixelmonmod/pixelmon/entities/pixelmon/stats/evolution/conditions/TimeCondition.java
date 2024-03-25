package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;

public class TimeCondition extends EvoCondition {
   public WorldTime time;
   public ArrayList times;

   public TimeCondition() {
      super("time");
      this.time = null;
      this.times = null;
   }

   public TimeCondition(WorldTime time) {
      this();
      this.time = time;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      ArrayList current = WorldTime.getCurrent(pixelmon.func_70902_q() != null ? pixelmon.func_70902_q().field_70170_p : pixelmon.field_70170_p);
      if (this.time != null) {
         return current.contains(this.time);
      } else {
         return this.times != null ? CollectionHelper.anyMatch(current, this.times) : false;
      }
   }
}
