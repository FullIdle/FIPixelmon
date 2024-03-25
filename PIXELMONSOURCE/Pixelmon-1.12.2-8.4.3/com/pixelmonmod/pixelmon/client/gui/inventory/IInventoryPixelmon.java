package com.pixelmonmod.pixelmon.client.gui.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;

interface IInventoryPixelmon {
   void superDrawScreen(int var1, int var2, float var3);

   float getZLevel();

   int getGUILeft();

   void offsetGUILeft(int var1);

   void subDrawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6);

   List getButtonList();

   void renderToolTipPublic(ItemStack var1, int var2, int var3);
}
