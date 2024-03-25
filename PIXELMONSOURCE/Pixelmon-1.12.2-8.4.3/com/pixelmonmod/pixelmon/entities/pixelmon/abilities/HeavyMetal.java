package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

public class HeavyMetal extends AbilityBase {
   public float modifyWeight(float initWeight) {
      return initWeight * 2.0F;
   }
}
