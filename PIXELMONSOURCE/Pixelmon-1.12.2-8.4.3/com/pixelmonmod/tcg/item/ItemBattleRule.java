package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import net.minecraft.item.Item;

public class ItemBattleRule extends Item {
   public ItemBattleRule() {
      this.func_77655_b(getName());
      this.func_77637_a(TCG.tabTCG);
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
   }

   public static String getName() {
      return "battle_rule";
   }
}
