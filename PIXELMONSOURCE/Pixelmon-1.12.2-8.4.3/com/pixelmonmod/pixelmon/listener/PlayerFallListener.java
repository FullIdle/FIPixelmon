package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import com.pixelmonmod.pixelmon.items.armor.armoreffects.IArmorEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerFallListener {
   @SubscribeEvent
   public static void onPlayerFall(LivingFallEvent event) {
      if (!event.getEntity().field_70170_p.field_72995_K && event.getEntity() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.getEntity();
         ItemStack boots = player.func_184582_a(EntityEquipmentSlot.FEET);
         if (boots.func_77973_b() instanceof GenericArmor && ((GenericArmor)boots.func_77973_b()).field_77878_bZ == PixelmonItemsTools.DAWNSTONEARMORMAT && IArmorEffect.isWearingFullSet(player, PixelmonItemsTools.DAWNSTONEARMORMAT)) {
            event.setCanceled(true);
         }
      }

   }
}
