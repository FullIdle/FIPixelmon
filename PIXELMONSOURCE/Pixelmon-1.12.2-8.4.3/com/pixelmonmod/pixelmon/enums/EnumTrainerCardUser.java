package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import java.util.EnumSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumTrainerCardUser {
   REGULAR,
   MODELER("modeler_text.png"),
   SUPPORT("support_text.png"),
   DEVELOPER("developer_text.png"),
   ADMINISTRATOR("administrator_text.png");

   public final ResourceLocation resource;

   private EnumTrainerCardUser() {
      this.resource = null;
   }

   private EnumTrainerCardUser(String file) {
      this.resource = new ResourceLocation(GuiResources.prefix + "gui/trainercards/" + file);
   }

   @SideOnly(Side.CLIENT)
   public static EnumTrainerCardUser getFromPlayer(EntityPlayer player) {
      PixelExtrasData data = PlayerExtraDataStore.get(player);
      EnumSet ranks = data.getAvailableSashs();
      if (!ranks.contains(PixelExtrasData.SashType.RANK_ADMIN) && !ranks.contains(PixelExtrasData.SashType.RANK_JR)) {
         if (ranks.contains(PixelExtrasData.SashType.RANK_DEV)) {
            return DEVELOPER;
         } else if (ranks.contains(PixelExtrasData.SashType.RANK_SUPPORT)) {
            return SUPPORT;
         } else {
            return ranks.contains(PixelExtrasData.SashType.RANK_MODELER) ? MODELER : REGULAR;
         }
      } else {
         return ADMINISTRATOR;
      }
   }
}
