package com.pixelmonmod.pixelmon.client.render.item;

import com.pixelmonmod.pixelmon.blocks.BlockScroll;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityScroll;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PixelmonTEISR extends TileEntityItemStackRenderer {
   public static PixelmonTEISR instance = new PixelmonTEISR();
   private final TileEntityScroll scroll;

   public PixelmonTEISR() {
      this.scroll = new TileEntityScroll(BlockScroll.Type.Waters);
   }

   public void func_179022_a(ItemStack stack) {
      this.func_192838_a(stack, 1.0F);
   }

   public void func_192838_a(ItemStack stack, float partialTicks) {
      Item item = stack.func_77973_b();
      if (item == PixelmonItems.scrollOfWaters || item == PixelmonItems.scrollOfDarkness) {
         this.scroll.setItemValues(stack);
         TileEntityRendererDispatcher.field_147556_a.func_192855_a(this.scroll, 0.0, 0.0, 0.0, 0.0F, partialTicks);
      }

   }
}
