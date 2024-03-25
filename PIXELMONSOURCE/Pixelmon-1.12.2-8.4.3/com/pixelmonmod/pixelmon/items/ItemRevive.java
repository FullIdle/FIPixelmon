package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.UseRevive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRevive extends ItemMedicine {
   public ItemRevive(String itemName, IMedicine... healMethods) {
      super(itemName, healMethods);
   }

   public ActionResult func_77659_a(World world, EntityPlayer playerIn, EnumHand hand) {
      if (world.field_72995_K) {
         Pokemon pokemon = ClientStorageManager.party.get(GuiPixelmonOverlay.selectedPixelmon);
         if (pokemon != null) {
            Pixelmon.network.sendToServer(new UseRevive(pokemon.getUUID()));
         }
      }

      return super.func_77659_a(world, playerIn, hand);
   }
}
