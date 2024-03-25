package com.pixelmonmod.pixelmon.items.armor.armoreffects;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.config.PixelmonItemsTools;
import com.pixelmonmod.pixelmon.items.armor.GenericArmor;
import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(
   modid = "pixelmon"
)
public class ThunderstoneEffect implements IArmorEffect {
   private static final UUID thunderStoneBoostUUID = UUID.fromString("de4f0383-fcf9-4ba7-8ffc-0767c1ead7b9");
   private static final AttributeModifier SpeedModifier2x;

   @SubscribeEvent
   public static void onArmorChange(LivingEquipmentChangeEvent event) {
      if (event.getEntityLiving() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.getEntityLiving();
         Multimap map = HashMultimap.create();
         map.put(SharedMonsterAttributes.field_111263_d.func_111108_a(), SpeedModifier2x);
         if (IArmorEffect.isWearingFullSet(player, PixelmonItemsTools.THUNDERSTONEARMORMAT)) {
            player.func_110140_aT().func_111147_b(map);
         } else {
            player.func_110140_aT().func_111148_a(map);
         }
      }

   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack stack, GenericArmor armor) {
      if (!world.field_72995_K) {
         if (IArmorEffect.isWearingFullSet(player, armor.field_77878_bZ) && player.field_70173_aa % 20 == 1) {
            player.func_70690_d(new PotionEffect(MobEffects.field_76422_e, 40, 1, true, true));
         }

      }
   }

   static {
      SpeedModifier2x = (new AttributeModifier(thunderStoneBoostUUID, SharedMonsterAttributes.field_111263_d.func_111108_a(), 1.0, 1)).func_111168_a(false);
   }
}
