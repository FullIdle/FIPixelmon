package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumBerryStatus;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.IMedicine;
import com.pixelmonmod.pixelmon.items.MedicineStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBerryStatus extends ItemBerry {
   public EnumBerryStatus berryType;
   private StatusType[] statusHealed;
   private IMedicine healMethod;

   public ItemBerryStatus(EnumBerryStatus berryType, EnumBerry berry, String itemName, StatusType... statusHealed) {
      super(EnumHeldItems.berryStatus, berry, itemName);
      this.berryType = berryType;
      this.healMethod = new MedicineStatus(statusHealed);
      this.statusHealed = statusHealed;
   }

   public boolean interact(EntityPixelmon pokemon, ItemStack itemstack, EntityPlayer player) {
      if (this.healMethod.useMedicine(new DelegateLink(pokemon.getPokemonData()))) {
         if (!player.field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
         }

         return true;
      } else {
         return false;
      }
   }

   public void onStatusAdded(PixelmonWrapper user, PixelmonWrapper opponent, StatusBase status) {
      if (!user.isFainted()) {
         this.eatBerry(user);
      }
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.eatBerry(newPokemon);
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      this.applySwitchInEffect(pw);
   }

   public void eatBerry(PixelmonWrapper pokemon) {
      if (canEatBerry(pokemon) && this.healStatus(pokemon)) {
         super.eatBerry(pokemon);
         pokemon.consumeItem();
      }

   }

   public boolean useFromBag(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
      if (!this.healMethod.useMedicine(new WrapperLink(targetWrapper))) {
         targetWrapper.bc.sendToAll("pixelmon.general.noeffect");
      }

      return super.useFromBag(userWrapper, targetWrapper);
   }

   public boolean healStatus(PixelmonWrapper pokemon) {
      pokemon.eatingBerry = true;
      boolean healed = this.healMethod.useMedicine(new WrapperLink(pokemon));
      pokemon.eatingBerry = false;
      return healed;
   }

   public boolean canHealStatus(StatusType status) {
      StatusType[] var2 = this.statusHealed;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StatusType type = var2[var4];
         if (type == status) {
            return true;
         }
      }

      return false;
   }
}
