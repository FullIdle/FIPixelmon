package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemAbsorbBulb extends ItemRaiseStat {
   public ItemAbsorbBulb() {
      super(EnumHeldItems.absorbbulb, "absorb_bulb", StatsType.SpecialAttack, EnumType.Water);
   }
}
