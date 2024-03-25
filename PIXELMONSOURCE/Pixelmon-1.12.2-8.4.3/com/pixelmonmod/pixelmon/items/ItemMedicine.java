package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.WrapperLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class ItemMedicine extends PixelmonItem {
   private IMedicine[] healMethods;
   private int friendshipDecreaseNormal;
   private int friendshipDecreaseHigh;

   public ItemMedicine(String itemName, IMedicine... healMethods) {
      super(itemName);
      this.func_77625_d(16);
      this.func_77637_a(PixelmonCreativeTabs.restoration);
      this.canRepair = false;
      this.healMethods = healMethods;
   }

   public ItemMedicine setFriendshipDecrease(int normal, int high) {
      this.friendshipDecreaseNormal = normal;
      this.friendshipDecreaseHigh = high;
      return this;
   }

   public boolean useFromBag(PixelmonWrapper pixelmonWrapper, PixelmonWrapper target, int additionalInfo) {
      this.useMedicine(new WrapperLink(target), additionalInfo);
      return super.useFromBag(pixelmonWrapper, target, additionalInfo);
   }

   public boolean useMedicine(PokemonLink target, int additionalInfo) {
      boolean success = false;
      IMedicine[] var4 = this.healMethods;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         IMedicine healMethod = var4[var6];
         success = healMethod.useMedicine(target) || success;
      }

      if (success) {
         int friendship = target.getFriendship();
         target.adjustFriendship(friendship >= 200 ? -this.friendshipDecreaseHigh : -this.friendshipDecreaseNormal);
      } else {
         target.sendMessage("pixelmon.general.noeffect");
      }

      return success;
   }

   public ActionResult func_77659_a(World world, EntityPlayer playerIn, EnumHand hand) {
      return new ActionResult(EnumActionResult.SUCCESS, playerIn.func_184586_b(hand));
   }
}
