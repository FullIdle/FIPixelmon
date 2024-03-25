package com.pixelmonmod.pixelmon.entities.pixelmon.interactions.custom;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.enums.forms.EnumShearable;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.oredict.OreDictionary;

public class ShearInteraction extends PixelmonInteraction {
   public ShearInteraction() {
      super(1);
   }

   public boolean processInteract(Entity1Base pixelmon, EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (pixelmon.getPokemonData().getFormEnum() != EnumShearable.SHORN && stack != null && !stack.func_190926_b()) {
         if (stack.func_77973_b() == Items.field_151097_aZ) {
            player.func_71019_a(new ItemStack(Blocks.field_150325_L, player.func_70681_au().nextInt(3) + 5, ((EnumShearable)pixelmon.getPokemonData().getFormEnum()).getColorMeta()), false);
            pixelmon.getPokemonData().setForm(EnumShearable.SHORN);
            ShearableStats shearableStats = (ShearableStats)pixelmon.getPokemonData().getExtraStats(ShearableStats.class);
            shearableStats.growthStage = 10;
            return super.processInteract(pixelmon, player, hand, stack);
         }

         if (stack.func_77973_b() instanceof ItemDye) {
            int[] ids = OreDictionary.getOreIDs(stack);
            IEnumForm newColor = null;
            int[] var7 = ids;
            int var8 = ids.length;
            int var9 = 0;

            label96:
            while(var9 < var8) {
               int id = var7[var9];
               String name = OreDictionary.getOreName(id);
               switch (name.toLowerCase()) {
                  case "dyered":
                     newColor = EnumShearable.RED;
                     break label96;
                  case "dyeblack":
                     newColor = EnumShearable.BLACK;
                     break label96;
                  case "dyegreen":
                     newColor = EnumShearable.GREEN;
                     break label96;
                  case "dyebrown":
                     newColor = EnumShearable.BROWN;
                     break label96;
                  case "dyeblue":
                     newColor = EnumShearable.BLUE;
                     break label96;
                  case "dyepurple":
                     newColor = EnumShearable.PURPLE;
                     break label96;
                  case "dyecyan":
                     newColor = EnumShearable.CYAN;
                     break label96;
                  case "dyelightgray":
                     newColor = EnumShearable.LIGHTGRAY;
                     break label96;
                  case "dyegray":
                     newColor = EnumShearable.GRAY;
                     break label96;
                  case "dyepink":
                     newColor = EnumShearable.PINK;
                     break label96;
                  case "dyelime":
                     newColor = EnumShearable.LIME;
                     break label96;
                  case "dyeyellow":
                     newColor = EnumShearable.YELLOW;
                     break label96;
                  case "dyelightblue":
                     newColor = EnumShearable.LIGHTBLUE;
                     break label96;
                  case "dyemagenta":
                     newColor = EnumShearable.MAGENTA;
                     break label96;
                  case "dyeorange":
                     newColor = EnumShearable.ORANGE;
                     break label96;
                  case "dyewhite":
                     newColor = EnumShearable.NORMAL;
                     break label96;
                  default:
                     ++var9;
               }
            }

            if (newColor != null && newColor != pixelmon.getPokemonData().getFormEnum()) {
               player.func_184586_b(hand).func_190918_g(1);
               pixelmon.getPokemonData().setForm(newColor);
            }

            return true;
         }
      }

      return false;
   }
}
