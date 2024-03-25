package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FirestoneEffect implements IArmorEffect {
   public void onArmorTick(World world, EntityPlayer player, ItemStack stack, GenericArmor armor) {
      if (!world.field_72995_K) {
         if (IArmorEffect.isWearingFullSet(player, armor.field_77878_bZ) && player.field_70173_aa % 20 == 1) {
            if (!stack.func_77948_v()) {
               stack.func_77966_a(Enchantments.field_77329_d, 2);
            }

            player.func_70690_d(new PotionEffect(MobEffects.field_76426_n, 40, 2, true, true));
         }

      }
   }
}
