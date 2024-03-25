package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemKeyItem extends PixelmonItem {
   private final KeyItem item;

   public ItemKeyItem(KeyItem item) {
      super(item.name);
      this.item = item;
      this.func_77625_d(1);
      this.func_77637_a(CreativeTabs.field_78026_f);
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
      ItemStack stack = playerIn.func_184586_b(hand);
      if (!worldIn.field_72995_K) {
         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(playerIn.func_110124_au());
         boolean given = false;
         switch (this.item) {
            case KeyStone:
               if (!pps.getMegaItemsUnlocked().canMega()) {
                  pps.unlockMega();
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.MegaItem);
                  given = true;
               }
               break;
            case WishingStar:
               if (!pps.getMegaItemsUnlocked().canDynamax()) {
                  pps.unlockDynamax();
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.MegaItem, 1);
                  given = true;
               }
               break;
            case OvalCharm:
               if (!pps.getOvalCharm().isAvailable()) {
                  pps.setOvalCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.OvalCharm);
                  given = true;
               }
               break;
            case ShinyCharm:
               if (!pps.getShinyCharm().isAvailable()) {
                  pps.setShinyCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.ShinyCharm);
                  given = true;
               }
               break;
            case ExpCharm:
               if (!pps.getExpCharm().isAvailable()) {
                  pps.setExpCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.ExpCharm);
                  given = true;
               }
               break;
            case CatchingCharm:
               if (!pps.getCatchingCharm().isAvailable()) {
                  pps.setCatchingCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.CatchingCharm);
                  given = true;
               }
               break;
            case MarkCharm:
               if (!pps.getMarkCharm().isAvailable()) {
                  pps.setMarkCharm(EnumFeatureState.Active);
                  OpenScreen.open(pps.getPlayer(), EnumGuiScreen.MarkCharm);
                  given = true;
               }
         }

         if (given) {
            if (!playerIn.func_184812_l_()) {
               stack.func_190918_g(1);
            }

            return new ActionResult(EnumActionResult.SUCCESS, stack);
         } else {
            playerIn.func_145747_a(new TextComponentString(TextFormatting.GRAY + I18n.func_74838_a("pixelmon.items.alreadyhave")));
            return new ActionResult(EnumActionResult.FAIL, stack);
         }
      } else {
         return new ActionResult(EnumActionResult.PASS, stack);
      }
   }

   public static enum KeyItem {
      KeyStone("key_stone"),
      ShinyCharm("shiny_charm"),
      OvalCharm("oval_charm"),
      WishingStar("wishing_star"),
      ExpCharm("exp_charm"),
      CatchingCharm("catching_charm"),
      MarkCharm("mark_charm");

      private final String name;

      private KeyItem(String name) {
         this.name = name;
      }
   }
}
