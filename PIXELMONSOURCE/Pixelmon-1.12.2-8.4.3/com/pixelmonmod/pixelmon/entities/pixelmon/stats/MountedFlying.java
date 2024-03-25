package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.gson.JsonObject;

public class MountedFlying {
   public float upperAngleLimit = 45.0F;
   public float lowerAngleLimit = -45.0F;
   public float maxFlySpeed = 2.0F;
   public float decelerationRate = 0.3F;
   public float hoverDecelerationRate = 0.07F;
   public float accelerationRate = 0.05F;
   public float strafeAccelerationRate = 0.9F;
   public float strafeRollConversion = 30.0F;
   public float turnRate = 1.0F;
   public float pitchRate = 1.0F;
   public boolean staysHorizontalFlying = false;

   public MountedFlying(float upperAngleLimit, float lowerAngleLimit, float maxFlySpeed, float decelerationRate, float hoverDecelerationRate, float accelerationRate, float strafeAccelerationRate, float strafeRollConversion, float turnRate, float pitchRate, boolean staysHorizontalFlying) {
      this.upperAngleLimit = upperAngleLimit;
      this.lowerAngleLimit = lowerAngleLimit;
      this.maxFlySpeed = maxFlySpeed;
      this.decelerationRate = decelerationRate;
      this.hoverDecelerationRate = hoverDecelerationRate;
      this.accelerationRate = accelerationRate;
      this.strafeAccelerationRate = strafeAccelerationRate;
      this.strafeRollConversion = strafeRollConversion;
      this.turnRate = turnRate;
      this.pitchRate = pitchRate;
      this.staysHorizontalFlying = staysHorizontalFlying;
   }

   public MountedFlying(JsonObject jsonObject) {
      if (jsonObject.has("upperanglelimit")) {
         this.upperAngleLimit = jsonObject.get("upperanglelimit").getAsFloat();
      }

      if (jsonObject.has("loweranglelimit")) {
         this.lowerAngleLimit = jsonObject.get("loweranglelimit").getAsFloat();
      }

      if (jsonObject.has("maxflyspeed")) {
         this.maxFlySpeed = jsonObject.get("maxflyspeed").getAsFloat();
      }

      if (jsonObject.has("decelerationrate")) {
         this.decelerationRate = jsonObject.get("decelerationrate").getAsFloat();
      }

      if (jsonObject.has("hoverdecelerationrate")) {
         this.hoverDecelerationRate = jsonObject.get("hoverdecelerationrate").getAsFloat();
      }

      if (jsonObject.has("accelerationrate")) {
         this.accelerationRate = jsonObject.get("accelerationrate").getAsFloat();
      }

      if (jsonObject.has("strafeaccelerationrate")) {
         this.strafeAccelerationRate = jsonObject.get("strafeaccelerationrate").getAsFloat();
      }

      if (jsonObject.has("straferollconversion")) {
         this.strafeRollConversion = jsonObject.get("straferollconversion").getAsFloat();
      }

      if (jsonObject.has("turnrate")) {
         this.turnRate = jsonObject.get("turnrate").getAsFloat();
      }

      if (jsonObject.has("pitchrate")) {
         this.pitchRate = jsonObject.get("pitchrate").getAsFloat();
      }

      if (jsonObject.has("stayshorizontalflying")) {
         this.staysHorizontalFlying = jsonObject.get("stayshorizontalflying").getAsBoolean();
      }

   }
}
