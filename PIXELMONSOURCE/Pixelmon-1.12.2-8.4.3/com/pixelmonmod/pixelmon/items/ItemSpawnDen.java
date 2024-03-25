package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSpawnDen extends Item {
   public static boolean isOPOnly = false;

   public ItemSpawnDen() {
      this.func_77637_a(PixelmonCreativeTabs.decoration);
      this.func_77655_b("den_spawner");
      this.setRegistryName("den_spawner");
      this.func_77625_d(1);
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack itemstack = player.func_184586_b(hand);
      if (isOPOnly && !player.func_70003_b(4, "pixelmon.denspawner.use")) {
         ChatHandler.sendChat(player, "pixelmon.general.needop");
         return new ActionResult(EnumActionResult.FAIL, itemstack);
      } else {
         if (!world.field_72995_K && player.field_71075_bZ.field_75098_d) {
            double x = (double)((int)player.field_70165_t) + 0.5;
            double y = (double)((int)player.field_70163_u);
            double z = (double)((int)player.field_70161_v) + 0.5;
            EntityDen den = new EntityDen(world, x, y, z);
            Pixelmon.proxy.spawnEntitySafely(den, world);
            world.func_184133_a((EntityPlayer)null, player.func_180425_c(), SoundEvents.field_187604_bf, SoundCategory.BLOCKS, 0.5F, 1.0F);
         }

         return new ActionResult(EnumActionResult.SUCCESS, itemstack);
      }
   }

   public EnumRarity func_77613_e(ItemStack par1ItemStack) {
      return EnumRarity.EPIC;
   }
}
