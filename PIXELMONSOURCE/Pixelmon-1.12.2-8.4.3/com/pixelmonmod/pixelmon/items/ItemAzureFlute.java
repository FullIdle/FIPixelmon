package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.legendary.ArceusEvent;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTimespaceAltar;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.Iterator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemAzureFlute extends PixelmonItem {
   public ItemAzureFlute() {
      super("azure_flute");
      this.func_77625_d(1);
      this.func_77637_a(CreativeTabs.field_78026_f);
   }

   public ActionResult func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
      ItemStack stack = playerIn.func_184586_b(hand);
      if (!worldIn.field_72995_K) {
         Iterator var5 = worldIn.field_147482_g.iterator();

         while(var5.hasNext()) {
            TileEntity te = (TileEntity)var5.next();
            if (te instanceof TileEntityTimespaceAltar && te.func_145835_a(playerIn.field_70165_t, playerIn.field_70163_u, playerIn.field_70161_v) <= 200.0) {
               TileEntityTimespaceAltar altar = (TileEntityTimespaceAltar)te;
               if (altar.timeSpent == 0 && !altar.chainIn && altar.orbIn == TileEntityTimespaceAltar.Orb.NONE) {
                  altar.summoningShiny = PixelmonConfig.getShinyRate(worldIn.field_73011_w.getDimension()) > 0.0F && RandomHelper.getRandomChance(1.0F / PixelmonConfig.getShinyRate(worldIn.field_73011_w.getDimension()));
                  if (Pixelmon.EVENT_BUS.post(new ArceusEvent.PlayFlute((EntityPlayerMP)playerIn, stack, altar))) {
                     return new ActionResult(EnumActionResult.FAIL, stack);
                  }

                  stack.func_190918_g(1);
                  altar.summoningPlayer = playerIn;
                  altar.summoningState = worldIn.func_180495_p(te.func_174877_v());
                  altar.flutePlayed = true;
                  return new ActionResult(EnumActionResult.SUCCESS, stack);
               }
            }
         }

         playerIn.func_145747_a(new TextComponentString(TextFormatting.GRAY + I18n.func_74838_a("pixelmon.items.azure.fail")));
      }

      return new ActionResult(EnumActionResult.SUCCESS, stack);
   }

   public boolean func_77636_d(ItemStack stack) {
      return true;
   }
}
