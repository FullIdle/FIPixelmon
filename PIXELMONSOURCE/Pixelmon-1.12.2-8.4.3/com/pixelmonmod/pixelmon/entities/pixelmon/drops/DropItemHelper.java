package com.pixelmonmod.pixelmon.entities.pixelmon.drops;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class DropItemHelper {
   private DropItemHelper() {
   }

   public static void giveItemStack(EntityPlayerMP player, ItemStack is, boolean announce) {
      int all = is.func_190916_E();
      int leftovers = player.field_71071_by.func_70452_e(is);
      if (leftovers == 0) {
         if (announce) {
            ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.receivedrop", is.func_190916_E(), new TextComponentTranslation(is.func_77977_a() + ".name", new Object[0]));
         }
      } else if (leftovers == all) {
         player.func_146097_a(is, false, false);
         ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.fullinventory", new TextComponentTranslation(is.func_77977_a() + ".name", new Object[0]));
      } else if (leftovers > 0) {
         is = is.func_77946_l();
         is.func_190920_e(leftovers);
         player.func_146097_a(is, false, false);
         ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.leftovers");
      }

   }

   public static void dropItemOnGround(Vec3d position, EntityPlayerMP player, ItemStack is, boolean ownedByPlayer, boolean announce) {
      if (!is.func_190926_b()) {
         EntityItem itemOnGround = new EntityItem(player.field_70170_p, position.field_72450_a, position.field_72448_b + 0.5, position.field_72449_c, is);
         itemOnGround.func_174869_p();
         player.field_70170_p.func_72838_d(itemOnGround);
         if (ownedByPlayer) {
            itemOnGround.func_145797_a(player.func_70005_c_());
         }

         if (announce) {
            ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.fullinventory", new TextComponentTranslation(is.func_77977_a() + ".name", new Object[0]));
         }

      }
   }
}
