package com.pixelmonmod.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.lures.LureExpiredEvent;
import com.pixelmonmod.pixelmon.api.events.lures.LureExpiringEvent;
import com.pixelmonmod.pixelmon.api.lures.ILureExpirer;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

public class SteadyLureExpirer implements ILureExpirer {
   public void tick(EntityPlayerMP player, PlayerPartyStorage party, ItemStack lure) {
      if (!lure.func_77942_o() || !lure.func_77978_p().func_74767_n("Unbreakable")) {
         ++party.transientData.lureTick;
         if (party.transientData.lureTick >= PixelmonConfig.averageLureExpiryTicks / 128) {
            party.transientData.lureTick = 0;
            LureExpiringEvent degradeEvent = new LureExpiringEvent(player);
            if (!Pixelmon.EVENT_BUS.post(degradeEvent)) {
               lure.func_77964_b(lure.func_77952_i() + degradeEvent.damage);
               if (lure.func_77952_i() >= 128) {
                  LureExpiredEvent expiredEvent = new LureExpiredEvent(player);
                  if (!Pixelmon.EVENT_BUS.post(expiredEvent)) {
                     player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187662_cZ, SoundCategory.AMBIENT, 1.0F, 1.5F);
                     party.setLure((ItemLure)null);
                     player.func_145747_a(new TextComponentTranslation("pixelmon.lure.expire", new Object[0]));
                     if (expiredEvent.casingStack != null && !expiredEvent.casingStack.func_190926_b()) {
                        DropItemHelper.giveItemStack(player, expiredEvent.casingStack, false);
                     }

                     return;
                  }

                  lure.func_77964_b(127);
               }

               party.setLureStack(party.getLureStack());
            }
         }

      }
   }
}
