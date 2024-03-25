package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

public class Adaptability extends AbilityBase {
   public double modifyStab(double stab) {
      return stab == 1.5 ? 2.0 : 1.0;
   }
}
