package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumQuestItems;

public class ItemQuest extends PixelmonItem {
   public EnumQuestItems type;

   public ItemQuest(EnumQuestItems type) {
      this(type, 16);
   }

   public ItemQuest(EnumQuestItems type, int maxStackSize) {
      super(type.getFileName());
      this.type = type;
      this.func_77625_d(maxStackSize);
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.quests);
      this.canRepair = false;
   }
}
