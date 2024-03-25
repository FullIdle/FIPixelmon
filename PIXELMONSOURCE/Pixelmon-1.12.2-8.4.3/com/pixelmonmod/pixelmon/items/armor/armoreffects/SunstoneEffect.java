package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SunstoneEffect implements IArmorEffect {
   public void onArmorTick(World world, EntityPlayer player, ItemStack stack, GenericArmor armor) {
      if (!world.field_72995_K) {
         if (IArmorEffect.isWearingFullSet(player, armor.field_77878_bZ) && !stack.func_77948_v()) {
            stack.func_77966_a(Enchantments.field_180310_c, 4);
            stack.func_77966_a(Enchantments.field_180308_g, 4);
         }

      }
   }
}
