package com.fipixelmonmod.fipixelmon.bridge;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @see com.pixelmonmod.pixelmon.client.gui.inventory.IInventoryPixelmon
 */
public interface IInventoryPixelmonBridge {
    void fIPixelmon$superDrawScreen(int var1, int var2, float var3);

    float fIPixelmon$getZLevel();

    int fIPixelmon$getGUILeft();

    void fIPixelmon$offsetGUILeft(int var1);

    void fIPixelmon$subDrawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6);

    List<GuiButton> fIPixelmon$getButtonList();

    void fIPixelmon$renderToolTipPublic(ItemStack var1, int var2, int var3);
}
