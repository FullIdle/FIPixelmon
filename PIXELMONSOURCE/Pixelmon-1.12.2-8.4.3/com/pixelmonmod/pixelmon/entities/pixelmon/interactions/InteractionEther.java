package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.OpenBattleMode;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.items.ItemEther;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionEther implements IInteraction {
   public boolean processInteract(EntityPixelmon entityPixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      if (player instanceof EntityPlayerMP && entityPixelmon.func_70902_q() == player) {
         Item item = itemstack.func_77973_b();
         if (item instanceof ItemEther) {
            if (!entityPixelmon.getPokemonData().getMoveset().hasFullPP()) {
               Pixelmon.network.sendTo(new OpenBattleMode(BattleMode.ChooseEther, entityPixelmon.getPartyPosition()), (EntityPlayerMP)player);
            } else {
               ChatHandler.sendChat(player, "pixelmon.interaction.ppfail", entityPixelmon.getNickname());
            }

            return true;
         }
      }

      return false;
   }
}
