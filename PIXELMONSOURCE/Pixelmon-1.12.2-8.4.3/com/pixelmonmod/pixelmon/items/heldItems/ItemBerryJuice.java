package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.HealFixed;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.MedicinePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBerryJuice extends ItemHeld {
   private MedicinePotion healMethod = new MedicinePotion(new HealFixed(20));
   private static final int PERCENT_THRESHOLD = 50;

   public ItemBerryJuice(String itemName) {
      super(EnumHeldItems.berryJuice, itemName);
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      return this.healMethod.useMedicine(new DelegateLink(pokemon.getPokemonData()));
   }

   public void tookDamage(PixelmonWrapper attacker, PixelmonWrapper pokemon, float damage, DamageTypeEnum damageType) {
      if (pokemon.isAlive() && pokemon.getHealthPercent() <= 50.0F) {
         this.consumeItem(pokemon);
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.getHealthPercent() <= 50.0F) {
         this.consumeItem(newPokemon);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.applySwitchInEffect(pw);
   }

   private void consumeItem(PixelmonWrapper pokemon) {
      if (this.healPokemon(pokemon)) {
         pokemon.consumeItem();
      }

   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!this.healPokemon(userWrapper)) {
         userWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }

   public boolean healPokemon(PixelmonWrapper pokemon) {
      if (this.healMethod.useMedicine(new WrapperLink(pokemon))) {
         String nickname = pokemon.getNickname();
         pokemon.bc.sendToAll("pixelmon.helditems.consumerestorehp", nickname, this.getLocalizedName());
         return true;
      } else {
         return false;
      }
   }
}
