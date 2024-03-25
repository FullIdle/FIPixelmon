package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class ItemTM extends PixelmonItem {
   public final String attackName;
   public final int index;
   public final boolean isHM = true;

   public ItemTM(String attackName, int tmIndex) {
      super("hm" + tmIndex);
      this.index = tmIndex;
      this.attackName = attackName;
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.tms);
      this.setNoRepair();
   }

   public String getTooltipText() {
      String ret = "";
      AttackBase ab = (AttackBase)AttackBase.getAttackBase(this.attackName).orElse((Object)null);
      String formattedData = I18n.func_74837_a("tm.movedata.tooltip", new Object[]{"§" + String.format("#%06X", 16777215 & ab.getAttackType().getColor()) + ab.getAttackType().getLocalizedName() + TextFormatting.GRAY, ab.getBasePower() != 0 ? ab.getBasePower() : "-", ab.getAccuracy() != -1 ? ab.getAccuracy() : "-", ab.getPPBase()});
      ret = ret + formattedData;
      if (I18n.func_94522_b(this.func_77658_a() + ".tooltip")) {
         ret = ret + I18n.func_74838_a(this.func_77658_a() + ".tooltip");
      }

      return ret;
   }
}
