package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

public enum EnumTier {
   Unrestricted,
   OU,
   UU,
   RU,
   NU,
   PU;

   public String getTierID() {
      return this.name().toLowerCase();
   }
}
