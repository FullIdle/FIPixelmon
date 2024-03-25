package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemRepel extends PixelmonItem {
   EnumRepel repel;

   public ItemRepel(EnumRepel repel) {
      super(repel.name().toLowerCase());
      this.repel = repel;
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (hand != EnumHand.OFF_HAND && !world.field_72995_K) {
         ItemStack heldStack = player.func_184586_b(hand);
         if (!player.func_184812_l_()) {
            heldStack.func_77979_a(1);
         }

         boolean hadRepel = RepelHandler.hasRepel((EntityPlayerMP)player);
         RepelHandler.applyRepel((EntityPlayerMP)player, this.repel);
         if (hadRepel) {
            ChatHandler.sendFormattedChat(player, TextFormatting.DARK_GREEN, "item.repel.applyextended");
         } else {
            ChatHandler.sendFormattedChat(player, TextFormatting.DARK_GREEN, "item.repel.apply");
         }

         return new ActionResult(EnumActionResult.SUCCESS, stack);
      } else {
         return new ActionResult(EnumActionResult.PASS, stack);
      }
   }

   public static enum EnumRepel {
      REPEL(6000),
      SUPER_REPEL(18000),
      MAX_REPEL(36000);

      public final int ticks;

      private EnumRepel(int ticks) {
         this.ticks = ticks;
      }
   }
}
