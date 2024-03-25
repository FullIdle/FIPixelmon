package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

public enum EnumBattleQueryResponse {
   Accept,
   Decline,
   Rules,
   Change,
   None;

   public boolean isAcceptResponse() {
      return this == Accept || this == Rules;
   }

   static EnumBattleQueryResponse getFromOrdinal(int ordinal) {
      EnumBattleQueryResponse[] values = values();
      return ordinal >= 0 && ordinal < values.length ? values[ordinal] : Decline;
   }
}
