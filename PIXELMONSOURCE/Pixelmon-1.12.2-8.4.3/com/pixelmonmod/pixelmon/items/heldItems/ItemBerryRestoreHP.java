package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Confusion;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Gluttony;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Ripen;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumBerryRestoreHP;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.HealFixed;
import com.pixelmonmod.pixelmon.items.HealFraction;
import com.pixelmonmod.pixelmon.items.IHealHP;
import com.pixelmonmod.pixelmon.items.MedicinePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBerryRestoreHP extends ItemBerry {
   public EnumBerryRestoreHP berryType;
   public StatsType confusedStat;
   private MedicinePotion healMethod;

   public ItemBerryRestoreHP(EnumBerryRestoreHP berryType, EnumBerry berry, String itemName, StatsType confusedStat) {
      super(EnumHeldItems.berryRestoreHP, berry, itemName);
      this.berryType = berryType;
      this.confusedStat = confusedStat;
      this.healMethod = new MedicinePotion(this.getHealAmount());
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      return this.healMethod.useMedicine(new DelegateLink(pokemon.getPokemonData()));
   }

   public void tookDamage(PixelmonWrapper attacker, PixelmonWrapper pokemon, float damage, DamageTypeEnum damageType) {
      if (pokemon.isAlive() && pokemon.getHealthPercent() <= (float)this.getThreshold(pokemon)) {
         this.eatBerry(pokemon);
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (newPokemon.getHealthPercent() <= (float)this.getThreshold(newPokemon)) {
         this.eatBerry(newPokemon);
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.applySwitchInEffect(pw);
   }

   public void eatBerry(PixelmonWrapper pokemon) {
      if (canEatBerry(pokemon) && this.healPokemon(pokemon)) {
         super.eatBerry(pokemon);
         pokemon.consumeItem();
      }

   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!this.healPokemon(targetWrapper)) {
         userWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }

   public boolean healPokemon(PixelmonWrapper pokemon) {
      boolean ripened = pokemon.getBattleAbility().isAbility(Ripen.class);
      if (this.healMethod.useMedicine(new WrapperLink(pokemon), ripened ? 2.0 : 1.0)) {
         String nickname = pokemon.getNickname();
         pokemon.bc.sendToAll("pixelmon.helditems.consumerestorehp", nickname, this.getLocalizedName());
         if (pokemon.getNature().decreasedStat == this.confusedStat && pokemon.addStatus(new Confusion(), pokemon)) {
            pokemon.bc.sendToAll("pixelmon.effect.becameconfused", nickname);
         }

         if (ripened) {
            pokemon.bc.sendToAll("pixelmon.abilities.ripen", pokemon.getNickname(), this.getLocalizedName());
         }

         return true;
      } else {
         return false;
      }
   }

   public IHealHP getHealAmount() {
      switch (this.berryType) {
         case sitrusBerry:
            return new HealFraction(0.25F);
         case oranBerry:
            return new HealFixed(10);
         default:
            return new HealFraction(0.33333334F);
      }
   }

   public int getThreshold(PixelmonWrapper user) {
      switch (this.berryType) {
         case sitrusBerry:
         case oranBerry:
            return 50;
         default:
            return user.getBattleAbility() instanceof Gluttony ? 50 : 25;
      }
   }
}
