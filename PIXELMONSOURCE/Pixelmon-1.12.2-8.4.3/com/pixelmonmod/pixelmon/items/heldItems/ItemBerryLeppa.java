package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.OpenBattleMode;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemEther;
import com.pixelmonmod.pixelmon.items.ItemPPRestore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class ItemBerryLeppa extends ItemBerry {
   private Attack move;

   public ItemBerryLeppa() {
      super(EnumHeldItems.leppa, EnumBerry.Leppa, "leppa_berry");
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      if (!pokemon.getPokemonData().getMoveset().hasFullPP()) {
         Pixelmon.network.sendTo(new OpenBattleMode(BattleMode.ChooseEther, pokemon.getPartyPosition()), (EntityPlayerMP)player);
         return true;
      } else {
         return false;
      }
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      this.activate(pokemon);
   }

   private void activate(PixelmonWrapper pokemon) {
      Moveset moveset = pokemon.getMoveset();

      for(int i = 0; i < moveset.size(); ++i) {
         this.move = moveset.get(i);
         if (this.move != null && this.move.pp <= 0) {
            this.eatBerry(pokemon);
            break;
         }
      }

      this.move = null;
   }

   public void eatBerry(PixelmonWrapper pixelmon) {
      if (canEatBerry(pixelmon)) {
         if (this.move == null) {
            if (pixelmon.attack != null) {
               this.move = pixelmon.attack;
            }

            if (this.move == null) {
               return;
            }
         }

         boolean ripened = pixelmon.getBattleAbility().isAbility(Ripen.class);
         ItemPPRestore.restorePP(new WrapperLink(pixelmon), this.move, ripened ? 20 : 10, false);
         pixelmon.consumeItem();
         pixelmon.bc.sendToAll("pixelmon.helditems.consumeleppa", pixelmon.getNickname(), this.move.getMove().getTranslatedName());
         if (ripened) {
            pixelmon.bc.sendToAll("pixelmon.abilities.ripen", pixelmon.getNickname(), this.getLocalizedName());
         }

         super.eatBerry(pixelmon);
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.activate(newPokemon);
   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper, int selectedMove) {
      return ((ItemEther)PixelmonItems.ether).useFromBag(userWrapper, targetWrapper, selectedMove);
   }
}
