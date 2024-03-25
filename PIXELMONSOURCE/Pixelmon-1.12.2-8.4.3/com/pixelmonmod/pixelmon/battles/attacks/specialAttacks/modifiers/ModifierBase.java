package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers;

public class ModifierBase {
   public ModifierType type;
   public float value;
   public Integer multiplier = null;

   public ModifierBase() {
   }

   public ModifierBase(ModifierType type) {
      this.type = type;
   }

   public ModifierBase(ModifierType type, float value) {
      this.type = type;
      this.value = value;
   }
}
