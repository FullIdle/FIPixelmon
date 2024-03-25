package com.pixelmonmod.pixelmon.battles.controller.participants;

public enum ParticipantType {
   Player,
   Trainer,
   WildPokemon,
   RaidPokemon;

   public static ParticipantType get(int index) {
      ParticipantType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ParticipantType p = var1[var3];
         if (p.ordinal() == index) {
            return p;
         }
      }

      return null;
   }
}
