package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.gson.JsonObject;

public class SwimmingParameters {
   public int depthRangeStart = 0;
   public int depthRangeEnd = 5;
   public float swimSpeed = 1.0F;
   public float decayRate = 0.99F;
   public int refreshRate = 100;

   public SwimmingParameters(int depthMin, int depthMax, double swimSpeed, double decayRate, int refreshRate) {
      this.depthRangeStart = depthMin;
      this.depthRangeEnd = depthMax;
      this.swimSpeed = (float)swimSpeed;
      this.decayRate = (float)decayRate;
      this.refreshRate = refreshRate;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SwimmingParameters)) {
         return false;
      } else {
         SwimmingParameters other = (SwimmingParameters)o;
         return other.depthRangeStart == this.depthRangeStart && other.depthRangeEnd == this.depthRangeEnd && other.swimSpeed == this.swimSpeed && other.decayRate == this.decayRate && other.refreshRate == this.refreshRate;
      }
   }

   public SwimmingParameters(JsonObject jsonObject) {
      if (jsonObject.has("depthmin")) {
         this.depthRangeStart = jsonObject.get("depthmin").getAsInt();
      }

      if (jsonObject.has("depthmax")) {
         this.depthRangeEnd = jsonObject.get("depthmax").getAsInt();
      }

      if (jsonObject.has("swimspeed")) {
         this.swimSpeed = jsonObject.get("swimspeed").getAsFloat();
      }

      if (jsonObject.has("decayrate")) {
         this.decayRate = jsonObject.get("decayrate").getAsFloat();
      }

      if (jsonObject.has("refreshrate")) {
         this.refreshRate = jsonObject.get("refreshrate").getAsInt();
      }

   }
}
