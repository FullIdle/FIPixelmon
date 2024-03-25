package com.pixelmonmod.pixelmon.enums.battle;

import com.pixelmonmod.pixelmon.battles.controller.ai.AdvancedAI;
import com.pixelmonmod.pixelmon.battles.controller.ai.AggressiveAI;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.RandomAI;
import com.pixelmonmod.pixelmon.battles.controller.ai.TacticalAI;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import net.minecraft.util.text.translation.I18n;

public enum EnumBattleAIMode {
   Default,
   Random,
   Aggressive,
   Tactical,
   Advanced;

   public static EnumBattleAIMode getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return Random;
      }
   }

   public EnumBattleAIMode getNextMode() {
      return values()[(this.ordinal() + 1) % values().length];
   }

   public BattleAIBase createAI(BattleParticipant participant) {
      switch (this) {
         case Aggressive:
            return new AggressiveAI(participant);
         case Tactical:
            return new TacticalAI(participant);
         case Advanced:
            return new AdvancedAI(participant);
         case Random:
         default:
            return new RandomAI(participant);
      }
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.battleAI." + this.toString().toLowerCase());
   }
}
