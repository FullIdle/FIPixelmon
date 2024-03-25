package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionDeveloper implements IInteraction {
   UUID oa10712 = UUID.fromString("c3f78c91-d7c0-4cb5-baa2-75edb7735c97");
   UUID waterdude = UUID.fromString("04e10682-dfe3-4c2f-9bcb-5e04ec7b647d");
   UUID jay113355 = UUID.fromString("8139124d-840a-486e-802e-d08b4a66a08c");
   UUID ras = UUID.fromString("da5e4167-84c4-4c1c-95b7-de18354f77bf");
   UUID isi = UUID.fromString("f93a4a85-8d0d-415e-8a39-420feeab53e8");
   UUID ribchop = UUID.fromString("4dbf5224-8d72-46f8-94a9-434ab5a1ef96");

   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      UUID id = player.func_146103_bH().getId();
      if (!player.func_70093_af()) {
         return false;
      } else if (this.waterdude.equals(id)) {
         return pixelmon.getPokemonData().addRibbon(EnumRibbonType.WATERDUDE, true);
      } else if (this.oa10712.equals(id)) {
         return pixelmon.getPokemonData().addRibbon(EnumRibbonType.OA10712, true);
      } else if (this.jay113355.equals(id) && itemstack.func_77973_b() == Items.field_151055_y) {
         return pixelmon.getPokemonData().addRibbon(EnumRibbonType.JAY113355, true);
      } else if (this.ras.equals(id)) {
         return pixelmon.getPokemonData().addRibbon(EnumRibbonType.RAS, true);
      } else if (this.isi.equals(id)) {
         return pixelmon.getPokemonData().addRibbon(EnumRibbonType.ISI, true);
      } else {
         return this.ribchop.equals(id) ? pixelmon.getPokemonData().addRibbon(EnumRibbonType.RIBCHOP, true) : false;
      }
   }

   public boolean processOnEmptyHand(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemstack) {
      return true;
   }
}
