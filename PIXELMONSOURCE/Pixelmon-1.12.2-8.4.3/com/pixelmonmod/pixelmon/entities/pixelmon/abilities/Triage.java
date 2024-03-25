package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;

public class Triage extends AbilityBase {
   public float modifyPriority(PixelmonWrapper pokemon, float priority, MutableBoolean triggered) {
      if (pokemon.attack != null && pokemon.attack.isAttack("Absorb", "Drain Punch", "Draining Kiss", "Dream Eater", "Floral Healing", "Giga Drain", "Heal Order", "Heal Pulse", "Healing Wish", "Horn Leech", "Leech Life", "Lunar Dance", "Mega Drain", "Milk Drink", "Moonlight", "Morning Sun", "Oblivion Wing", "Parabolic Charge", "Purify", "Recover", "Rest", "Roost", "Shore Up", "Slack Off", "Soft-Boiled", "Strength Sap", "Swallow", "Synthesis", "Wish")) {
         priority += 3.0F;
         triggered.setTrue();
      }

      return priority;
   }
}
