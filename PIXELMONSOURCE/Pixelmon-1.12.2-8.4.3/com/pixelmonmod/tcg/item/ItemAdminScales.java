package com.pixelmonmod.tcg.item;

import com.pixelmonmod.tcg.TCG;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemAdminScales extends Item {
   public ItemAdminScales() {
      this.func_77655_b(getName());
      this.func_77625_d(1);
      this.setRegistryName("tcg", getName());
      this.func_77637_a(TCG.tabTCG);
   }

   public void func_77624_a(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
      tooltip.add(I18n.func_74838_a("item.scales.lore"));
   }

   public static String getName() {
      return "scales";
   }
}
