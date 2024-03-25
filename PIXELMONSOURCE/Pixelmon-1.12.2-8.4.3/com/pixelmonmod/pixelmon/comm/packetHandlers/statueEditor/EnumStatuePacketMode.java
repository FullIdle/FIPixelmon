package com.pixelmonmod.pixelmon.comm.packetHandlers.statueEditor;

import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public enum EnumStatuePacketMode {
   SetName,
   SetGrowth,
   SetLabel,
   SetTextureType,
   SetAnimation,
   SetModelStanding,
   SetAnimationFrame,
   SetForm,
   SetGender,
   SetShouldAnimate;

   public static EnumStatuePacketMode getFromOrdinal(int value) {
      EnumStatuePacketMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumStatuePacketMode m = var1[var3];
         if (m.ordinal() == value) {
            return m;
         }
      }

      return null;
   }

   public Object chooseValueForMode(String stringValue, Boolean boolValue, Integer intValue) {
      switch (this) {
         case SetName:
            return EnumSpecies.get(stringValue);
         case SetGrowth:
            return EnumGrowth.growthFromString(stringValue);
         case SetLabel:
            return stringValue;
         case SetTextureType:
            return stringValue;
         case SetAnimation:
            return stringValue;
         case SetModelStanding:
            return boolValue;
         case SetAnimationFrame:
            return intValue;
         case SetForm:
            return intValue;
         case SetGender:
            return intValue;
         case SetShouldAnimate:
            return boolValue;
         default:
            return null;
      }
   }
}
