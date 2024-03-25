package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemCellBattery extends ItemRaiseStat {
   public ItemCellBattery() {
      super(EnumHeldItems.cellbattery, "cell_battery", StatsType.Attack, EnumType.Electric);
   }
}
