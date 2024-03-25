package com.pixelmonmod.pixelmon.api.spawning.conditions;

public class TriggerLocation extends LocationType {
   public TriggerLocation(String name) {
      super(name);
      this.setBaseBlockCondition((type) -> {
         return false;
      });
      this.setSurroundingBlockCondition((type) -> {
         return false;
      });
   }
}
