package com.pixelmonmod.pixelmon.enums;

public enum EnumFeatureState {
   Disabled,
   Available,
   Active;

   public final boolean isActive() {
      return this == Active;
   }

   public final boolean isAvailable() {
      return this == Active || this == Available;
   }
}
