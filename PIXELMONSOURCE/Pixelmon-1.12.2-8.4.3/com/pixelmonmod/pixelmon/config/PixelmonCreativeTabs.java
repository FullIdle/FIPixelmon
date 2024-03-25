package com.pixelmonmod.pixelmon.config;

import com.pixelmonmod.pixelmon.enums.technicalmoves.Gen8TechnicalMachines;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PixelmonCreativeTabs {
   public static final CreativeTabs pokeball = new CreativeTabs("Pokeballs") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItemsPokeballs.pokeBall);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.pokeballs", new Object[0]);
      }
   };
   public static final CreativeTabs restoration = new CreativeTabs("Restoration") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItems.potion);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.restoration", new Object[0]);
      }
   };
   public static final CreativeTabs natural = new CreativeTabs("Natural") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItemsApricorns.apricornRed);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.natural", new Object[0]);
      }
   };
   public static final CreativeTabs held = new CreativeTabs("Held Items") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItemsHeld.expShare);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.held_items", new Object[0]);
      }
   };
   public static final CreativeTabs tms = new CreativeTabs("TMs/HMs") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return PixelmonItemsTMs.createStackFor(Gen8TechnicalMachines.MegaPunch, 1);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.tm_hm", new Object[0]);
      }
   };
   public static final CreativeTabs utilityBlocks = new CreativeTabs("Utility Blocks") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(Item.func_150898_a(PixelmonBlocks.warpPlate));
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.utility_blocks", new Object[0]);
      }
   };
   public static final CreativeTabs badges = new CreativeTabs("Badges") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItemsBadges.marshBadge);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.badges", new Object[0]);
      }
   };
   public static final CreativeTabs decoration = new CreativeTabs("Decoration") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(Item.func_150898_a(PixelmonBlocks.redUmbrellaBlock));
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.decoration", new Object[0]);
      }
   };
   public static final CreativeTabs PokeLoot = new CreativeTabs("PokeLoot") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonBlocks.pokeChest);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.pokeloot", new Object[0]);
      }
   };
   public static final CreativeTabs quests = new CreativeTabs("Quest Items") {
      @SideOnly(Side.CLIENT)
      public ItemStack func_78016_d() {
         return new ItemStack(PixelmonItems.questEditor);
      }

      @SideOnly(Side.CLIENT)
      public String func_78024_c() {
         return I18n.func_135052_a("pixelmon.creativetab.quests", new Object[0]);
      }
   };
}
