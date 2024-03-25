package com.pixelmonmod.pixelmon.config.recipes;

import com.pixelmonmod.pixelmon.blocks.decorative.BlockUnown;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class UnownBlockRecipe implements IRecipe {
   private ItemStack output;
   private int form;
   private ResourceLocation name;

   public UnownBlockRecipe(int damage) {
      int numUnownBlocks = BlockUnown.getNumUnownBlocks();
      int form = damage;
      if (damage == numUnownBlocks - 2) {
         form = numUnownBlocks - 3;
      } else if (damage == numUnownBlocks - 3) {
         form = numUnownBlocks - 2;
      }

      Block unownBlock = PixelmonBlocks.blockUnown;
      if (damage >= 16) {
         damage -= 16;
         unownBlock = PixelmonBlocks.blockUnown2;
      }

      this.form = form;
      this.output = new ItemStack(unownBlock, 1, damage);
      this.setRegistryName(new ResourceLocation("pixelmon", "unown_block" + ((BlockUnown)unownBlock).alphabetInUse[damage]));
   }

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      boolean hasBlock = false;
      boolean hasPhoto = false;
      boolean hasExtra = false;

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         ItemStack stack = inv.func_70301_a(i);
         if (!stack.func_190926_b()) {
            if (!hasBlock && stack.func_77973_b() == Item.func_150898_a(PixelmonBlocks.blockUnown2) && stack.func_77952_i() == BlockUnown.getBlankUnownBlockIndex()) {
               hasBlock = true;
            } else {
               if (hasPhoto || stack.func_77973_b() != PixelmonItems.itemPixelmonSprite || !stack.func_77942_o()) {
                  hasExtra = true;
                  break;
               }

               if (stack.func_77978_p().func_74764_b("SpriteName") && stack.func_77978_p().func_74779_i("SpriteName").equals("pixelmon:sprites/pokemon/201" + EnumSpecies.Unown.getFormEnum(this.form).getSpriteSuffix(false))) {
                  hasPhoto = true;
               } else if (stack.func_77978_p().func_74764_b("ndex") && stack.func_77978_p().func_74762_e("ndex") == 201 && stack.func_77978_p().func_74762_e("form") == this.form) {
                  hasPhoto = true;
               }
            }
         }
      }

      return hasBlock && hasPhoto && !hasExtra;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      return this.output.func_77946_l();
   }

   public boolean func_194133_a(int width, int height) {
      return width == 2 || height == 2;
   }

   public ItemStack func_77571_b() {
      return this.output;
   }

   public IRecipe setRegistryName(ResourceLocation name) {
      this.name = name;
      return this;
   }

   @Nullable
   public ResourceLocation getRegistryName() {
      return this.name;
   }

   public Class getRegistryType() {
      return IRecipe.class;
   }

   public NonNullList func_179532_b(InventoryCrafting inv) {
      NonNullList nonnulllist = NonNullList.func_191197_a(inv.func_70302_i_(), ItemStack.field_190927_a);

      for(int i = 0; i < nonnulllist.size(); ++i) {
         ItemStack itemStack = inv.func_70301_a(i);
         if (itemStack.func_77973_b() == PixelmonItems.itemPixelmonSprite) {
            nonnulllist.set(i, itemStack.func_77946_l());
         } else {
            nonnulllist.set(i, ForgeHooks.getContainerItem(itemStack));
         }
      }

      return nonnulllist;
   }
}
