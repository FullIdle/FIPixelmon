package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.gson.JsonObject;
import net.minecraft.block.material.Material;

public class FlyingParameters {
   public int flyHeightMin = 0;
   public int flyHeightMax = 10;
   public float flySpeedModifier = 1.0F;
   public int flyRefreshRateY = 100;
   public int flyRefreshRateXZ = 100;
   public int flyRefreshRateSpeed = 100;
   public int flightTimeMin = 0;
   public int flightTimeMax = 20;
   public int flapRate = 20;
   public EnumLandingMaterials landingMaterials;

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FlyingParameters)) {
         return false;
      } else {
         FlyingParameters other = (FlyingParameters)o;
         return other.flyHeightMin == this.flyHeightMin && other.flyHeightMax == this.flyHeightMax && other.flySpeedModifier == this.flySpeedModifier && other.flyRefreshRateY == this.flyRefreshRateY && other.flyRefreshRateXZ == this.flyRefreshRateXZ && other.flightTimeMin == this.flightTimeMin && other.flightTimeMax == this.flightTimeMax && other.flapRate == this.flapRate && other.landingMaterials == this.landingMaterials;
      }
   }

   public FlyingParameters(int heightMin, int heightMax, float speedMod, int rateY, int rateXZ, int rateSpeed, int flightTimeMin, int flightTimeMax, int flapRate, String landingMaterial) {
      this.landingMaterials = FlyingParameters.EnumLandingMaterials.NONE;
      this.flyHeightMin = heightMin;
      this.flyHeightMax = heightMax;
      this.flySpeedModifier = speedMod;
      this.flyRefreshRateY = rateY;
      this.flyRefreshRateXZ = rateXZ;
      this.flyRefreshRateSpeed = rateSpeed;
      this.flightTimeMin = flightTimeMin;
      this.flightTimeMax = flightTimeMax;
      this.flapRate = flapRate / 4;
      if (landingMaterial.equalsIgnoreCase("grassandleaves")) {
         this.landingMaterials = FlyingParameters.EnumLandingMaterials.LEAVES_AND_GRASS;
      } else if (landingMaterial.equalsIgnoreCase("leaves")) {
         this.landingMaterials = FlyingParameters.EnumLandingMaterials.LEAVES;
      }

   }

   public FlyingParameters(JsonObject jsonObject) {
      this.landingMaterials = FlyingParameters.EnumLandingMaterials.NONE;
      if (jsonObject.has("heightmin")) {
         this.flyHeightMin = jsonObject.get("heightmin").getAsInt();
      }

      if (jsonObject.has("heightmax")) {
         this.flyHeightMax = jsonObject.get("heightmax").getAsInt();
      }

      if (jsonObject.has("speed")) {
         this.flySpeedModifier = jsonObject.get("speed").getAsFloat();
      }

      if (jsonObject.has("refreshratey")) {
         this.flyRefreshRateY = jsonObject.get("refreshratey").getAsInt();
      }

      if (jsonObject.has("refreshratexz")) {
         this.flyRefreshRateXZ = jsonObject.get("refreshratexz").getAsInt();
      }

      if (jsonObject.has("refreshratespeed")) {
         this.flyRefreshRateSpeed = jsonObject.get("refreshratespeed").getAsInt();
      }

      if (jsonObject.has("flaprate")) {
         this.flapRate = jsonObject.get("flaprate").getAsInt() / 4;
      }

   }

   public boolean willLandInMaterial(Material m) {
      if (this.landingMaterials == FlyingParameters.EnumLandingMaterials.NONE) {
         return false;
      } else if (m == Material.field_151584_j) {
         return true;
      } else {
         return m == Material.field_151577_b && this.landingMaterials == FlyingParameters.EnumLandingMaterials.LEAVES_AND_GRASS;
      }
   }

   public static enum EnumLandingMaterials {
      NONE,
      LEAVES,
      LEAVES_AND_GRASS;
   }
}
