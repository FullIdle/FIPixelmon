package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumFossils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFossil extends PixelmonItem {
   public EnumFossils fossil;

   public ItemFossil(EnumFossils fossil) {
      super(fossil.getItemName());
      this.fossil = fossil;
      this.func_77637_a(PixelmonCreativeTabs.natural);
      this.canRepair = false;
   }

   public void func_77624_a(ItemStack stack, @Nullable World worldIn, List tooltip, ITooltipFlag flagIn) {
      if (this.fossil.getGeneration() != 8) {
         tooltip.add(I18n.func_74838_a("pixelmon." + this.fossil.getPokemon().name.toLowerCase() + ".name"));
      }

   }

   public EnumFossils getFossil() {
      return this.fossil;
   }
}
