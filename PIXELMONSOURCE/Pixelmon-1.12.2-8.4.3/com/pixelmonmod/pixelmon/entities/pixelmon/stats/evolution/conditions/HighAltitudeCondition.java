package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class HighAltitudeCondition extends EvoCondition {
   public float minAltitude;

   public HighAltitudeCondition() {
      super("highAltitude");
      this.minAltitude = 127.0F;
   }

   public HighAltitudeCondition(float minAltitude) {
      this();
      this.minAltitude = minAltitude;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      if (pixelmon.func_70902_q() != null) {
         return pixelmon.func_70902_q().func_174791_d().field_72448_b >= (double)this.minAltitude;
      } else {
         return pixelmon.func_174791_d().field_72448_b >= (double)this.minAltitude;
      }
   }
}
