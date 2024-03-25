package com.pixelmonmod.tcg.api.card.spec.requirement;

import java.util.List;

public interface Requirement {
   default int getPriority() {
      return 1;
   }

   default boolean shouldContinue() {
      return true;
   }

   List getAliases();

   boolean fits(String var1);

   List create(String var1);

   Requirement createInstance(Object var1);

   boolean isDataMatch(Object var1);

   boolean isMinecraftMatch(Object var1);

   void applyData(Object var1);

   void applyMinecraft(Object var1);

   Object getValue();
}
