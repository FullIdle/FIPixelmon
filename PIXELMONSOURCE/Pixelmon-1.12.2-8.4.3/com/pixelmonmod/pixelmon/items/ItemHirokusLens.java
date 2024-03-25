package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.LensUsedEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;

public class ItemHirokusLens extends PixelmonItem {
   public static SoundEvent sound;
   public EnumCheatItemType type;

   public ItemHirokusLens(EnumCheatItemType type) {
      super("lens_" + type.toString().toLowerCase());
      this.type = type;
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.setNoRepair();
      this.field_77777_bU = 1;
      this.func_77656_e(type == EnumCheatItemType.Gold ? 4 : 0);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return false;
      } else if (!(target instanceof EntityPixelmon)) {
         return false;
      } else {
         EntityPixelmon pixelmon = (EntityPixelmon)target;
         if (pixelmon.getPokemonData().getStorage() != null) {
            return false;
         } else if (pixelmon.battleController != null) {
            return false;
         } else if (pixelmon.getBossMode() != EnumBossMode.NotBoss) {
            return false;
         } else if (pixelmon.getPokemonData().getBonusStats().preventsCapture()) {
            return false;
         } else {
            LensUsedEvent event = new LensUsedEvent((EntityPlayerMP)player, pixelmon, stack);
            boolean cancelled = Pixelmon.EVENT_BUS.post(event);
            if (event.shouldItemBeDamaged && !player.func_184812_l_()) {
               if (this.type == EnumCheatItemType.Silver) {
                  stack.func_190918_g(1);
               } else {
                  stack.func_77972_a(1, player);
               }
            }

            if (cancelled) {
               return true;
            } else {
               pixelmon.exposeInfo((EntityPlayerMP)player);
               player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u + 1.0, player.field_70161_v, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
               player.func_145747_a(new TextComponentTranslation("item.lens.used", new Object[]{pixelmon.getPokemonData().getTranslatedName()}));
               return true;
            }
         }
      }
   }

   static {
      sound = SoundEvents.field_187604_bf;
   }
}
