package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.blocks.GenericOre;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class PixelmonOres {
   static void registerOres() {
      OreDictionary.registerOre("oreRuby", PixelmonBlocks.rubyOre);
      OreDictionary.registerOre("oreSapphire", PixelmonBlocks.sapphireOre);
      OreDictionary.registerOre("oreAmethyst", PixelmonBlocks.amethystOre);
      OreDictionary.registerOre("oreCrystal", PixelmonBlocks.crystalOre);
      OreDictionary.registerOre("oreSilicon", PixelmonBlocks.siliconOre);
      OreDictionary.registerOre("oreAluminium", PixelmonBlocks.bauxite);
      OreDictionary.registerOre("oreAluminum", PixelmonBlocks.bauxite);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandBlock);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandCorner1Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandCorner2Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandCorner3Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandCorner4Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandSide1Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandSide2Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandSide3Block);
      OreDictionary.registerOre("blockPokesand", PixelmonBlocks.pokeSandSide4Block);
      OreDictionary.registerOre("gemRuby", PixelmonItems.ruby);
      OreDictionary.registerOre("gemSapphire", PixelmonItems.sapphire);
      OreDictionary.registerOre("gemAmethyst", PixelmonItems.amethyst);
      OreDictionary.registerOre("gemCrystal", PixelmonItems.crystal);
      OreDictionary.registerOre("ingotAluminum", PixelmonItems.aluminiumIngot);
      OreDictionary.registerOre("itemSilicon", PixelmonItems.siliconItem);
      OreDictionary.registerOre("blockRuby", PixelmonBlocks.rubyBlock);
      OreDictionary.registerOre("blockSapphire", PixelmonBlocks.sapphireBlock);
      OreDictionary.registerOre("blockAmethyst", PixelmonBlocks.amethystBlock);
      OreDictionary.registerOre("blockCrystal", PixelmonBlocks.crystalBlock);
      OreDictionary.registerOre("blockSilicon", PixelmonBlocks.siliconBlock);
      OreDictionary.registerOre("blockAluminium", PixelmonBlocks.aluminiumBlock);
      OreDictionary.registerOre("blockAluminum", PixelmonBlocks.aluminiumBlock);
      OreDictionary.registerOre("blockBraille", new ItemStack(PixelmonBlocks.blockBraille, 1, 32767));
      OreDictionary.registerOre("blockBraille", new ItemStack(PixelmonBlocks.blockBraille2, 1, 32767));
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBachsTin);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBeanTin);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBobsTin);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBoiledEgg);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBread);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryBrittleBones);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryCoconutMilk);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryFancyApple);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryFreshCream);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryFriedFood);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryFruitBunch);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryGigantamix);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryInstantNoodles);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryLargeLeek);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryMixedMushrooms);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryMoomooCheese);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryPackagedCurry);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryPasta);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryPotatoPack);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryPrecookedBurger);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.curryPungentRoot);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.currySaladMix);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.currySausages);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.currySmokePokeTail);
      OreDictionary.registerOre("itemCurryKey", PixelmonItems.currySpiceMix);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.rawstBerry);
      OreDictionary.registerOre("green_berry", PixelmonItems.rabutaBerry);
      OreDictionary.registerOre("green_berry", PixelmonItems.durinBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.lumBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.rindoBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.kebiaBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.tangaBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.babiriBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.aguavBerry);
      OreDictionary.registerOre("green_berry", PixelmonItems.hondewBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.salacBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.starfBerry);
      OreDictionary.registerOre("green_berry", PixelmonItemsHeld.micleBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.cheriBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.leppaBerry);
      OreDictionary.registerOre("red_berry", PixelmonItems.razzBerry);
      OreDictionary.registerOre("red_berry", PixelmonItems.spelonBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.occaBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.chopleBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.habanBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.figyBerry);
      OreDictionary.registerOre("red_berry", PixelmonItems.pomegBerry);
      OreDictionary.registerOre("red_berry", PixelmonItems.tamatoBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.liechiBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.lansatBerry);
      OreDictionary.registerOre("red_berry", PixelmonItemsHeld.custapBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.chestoBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItems.cornnBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItems.pamtreBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItems.belueBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.payapaBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.kasibBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.colburBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.wikiBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.ganlonBerry);
      OreDictionary.registerOre("purple_berry", PixelmonItemsHeld.enigmaBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItemsHeld.pechaBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItems.nanabBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItems.magostBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItems.watmelBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItemsHeld.persimBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItemsHeld.magoBerry);
      OreDictionary.registerOre("pink_berry", PixelmonItemsHeld.roseliBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.aspearBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItems.pinapBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.sitrusBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.wacanBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.shucaBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.chartiBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.chilanBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.iapapaBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItems.qualotBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItems.grepaBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.keeBerry);
      OreDictionary.registerOre("yellow_berry", PixelmonItemsHeld.jabocaBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.oranBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.passhoBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.cobaBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItems.kelpsyBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.apicotBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.marangaBerry);
      OreDictionary.registerOre("blue_berry", PixelmonItemsHeld.rowapBerry);
   }

   static void populateOres() {
      ((GenericOre)PixelmonBlocks.rubyOre).addDrop(new ItemStack(PixelmonItems.ruby, 1));
      ((GenericOre)PixelmonBlocks.sapphireOre).addDrop(new ItemStack(PixelmonItems.sapphire, 1));
      ((GenericOre)PixelmonBlocks.amethystOre).addDrop(new ItemStack(PixelmonItems.amethyst, 1));
      ((GenericOre)PixelmonBlocks.crystalOre).addDrop(new ItemStack(PixelmonItems.crystal, 1));
   }

   static void registerFurnaceRecipes(FurnaceRecipes recipes) {
      recipes.func_151393_a(PixelmonBlocks.bauxite, new ItemStack(PixelmonItems.aluminiumIngot), 1.0F);
      recipes.func_151393_a(PixelmonBlocks.amethystOre, new ItemStack(PixelmonItems.amethyst), 2.0F);
      recipes.func_151393_a(PixelmonBlocks.crystalOre, new ItemStack(PixelmonItems.crystal), 2.0F);
      recipes.func_151393_a(PixelmonBlocks.rubyOre, new ItemStack(PixelmonItems.ruby), 2.0F);
      recipes.func_151393_a(PixelmonBlocks.sapphireOre, new ItemStack(PixelmonItems.sapphire), 2.0F);
      recipes.func_151393_a(PixelmonBlocks.siliconOre, new ItemStack(PixelmonItems.siliconItem), 2.0F);
   }

   public static boolean itemMatches(@Nonnull ItemStack stack, String oreName) {
      if (!OreDictionary.doesOreNameExist(oreName)) {
         return false;
      } else {
         NonNullList list = OreDictionary.getOres(oreName);
         Iterator var3 = list.iterator();

         ItemStack itemStack;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            itemStack = (ItemStack)var3.next();
         } while(!ItemStack.func_179545_c(stack, itemStack));

         return true;
      }
   }
}
