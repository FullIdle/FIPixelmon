package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class OpponentInfo {
   public EnumSpecies species = null;
   public Gender gender = null;
   public int form = -1;
   public int specialTexture = -1;
   public float lastSeenHealthPercentage = -1.0F;
   public String[] seenMoves = new String[]{"?", "?", "?", "?"};
   public String ability = "pixelmon.battles.info.unknown";
   public String heldItem = "pixelmon.battles.info.unknown";
}
