package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class ItemPPUp extends PixelmonItem {
   private int ppLevel = 1;

   public ItemPPUp(String name, boolean maxPP) {
      super(name);
      if (maxPP) {
         this.ppLevel = 3;
      }

      this.func_77637_a(PixelmonCreativeTabs.restoration);
   }

   public static void handleMoveSelect(EntityPlayerMP player, UUID poke, int moveIndex) {
      ItemStack stack = player.func_184614_ca();
      if (stack.func_77973_b() instanceof ItemPPUp) {
         ItemPPUp ppUp = (ItemPPUp)stack.func_77973_b();
         PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
         int slot = storage.getSlot(poke);
         if (slot != -1 && storage.get(slot) != null) {
            Pokemon pokemon = storage.get(slot);
            Attack attack = pokemon.getMoveset().get(moveIndex);
            if (attack != null) {
               int old = attack.getMaxPP();
               if (ppUp.ppLevel == 3 && attack.ppLevel < 3) {
                  attack.ppLevel = ppUp.ppLevel;
                  attack.pp += attack.getMaxPP() - old;
                  pokemon.markDirty(EnumUpdateType.Moveset);
                  ChatHandler.sendChat(player, "pixelmon.interaction.ppmax", attack.getMove().getTranslatedName());
                  if (!player.func_184812_l_()) {
                     stack.func_190918_g(1);
                  }
               } else if (ppUp.ppLevel == 1 && attack.ppLevel < 3) {
                  attack.ppLevel += ppUp.ppLevel;
                  attack.pp += attack.getMaxPP() - old;
                  pokemon.markDirty(EnumUpdateType.Moveset);
                  if (attack.ppLevel < 3) {
                     ChatHandler.sendChat(player, "pixelmon.interaction.ppup", attack.getMove().getTranslatedName());
                  } else {
                     ChatHandler.sendChat(player, "pixelmon.interaction.ppmax", attack.getMove().getTranslatedName());
                  }

                  if (!player.func_184812_l_()) {
                     stack.func_190918_g(1);
                  }
               } else {
                  ChatHandler.sendChat(player, "pixelmon.interaction.ppatmax");
               }
            }
         }

      }
   }
}
