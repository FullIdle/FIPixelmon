package com.pixelmonmod.pixelmon.client.gui.battles.rules;

public enum EnumRulesGuiState {
   Propose,
   Accept,
   WaitPropose,
   WaitAccept,
   WaitChange;

   boolean isWaiting() {
      return this == WaitPropose || this == WaitAccept || this == WaitChange;
   }

   public static EnumRulesGuiState getFromOrdinal(int ordinal) {
      EnumRulesGuiState[] values = values();
      return ordinal >= 0 && ordinal < values.length ? values[ordinal] : WaitChange;
   }
}
