package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.IInventoryPixelmonBridge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;

@Mixin(
        targets = "com.pixelmonmod.pixelmon.client.gui.inventory.IInventoryPixelmon",
        remap = false
)
public interface MixinIInventoryPixelmon extends IInventoryPixelmonBridge {
    @Shadow
    List<GuiButton> getButtonList();

    @Shadow
    void renderToolTipPublic(ItemStack itemStack, int i, int i1);

    @Shadow
    void subDrawGradientRect(int i, int i1, int i2, int i3, int i4, int i5);

    @Shadow
    void offsetGUILeft(int i);

    @Shadow
    int getGUILeft();

    @Shadow
    float getZLevel();

    @Shadow
    void superDrawScreen(int i, int i1, float v);

    @Override
    default void fIPixelmon$superDrawScreen(int var1, int var2, float var3) {
        superDrawScreen(var1,var2,var3);
    }

    @Override
    default float fIPixelmon$getZLevel() {
        return getZLevel();
    }

    @Override
    default int fIPixelmon$getGUILeft() {
        return getGUILeft();
    }

    @Override
    default void fIPixelmon$offsetGUILeft(int var1) {
        offsetGUILeft(var1);
    }

    @Override
    default void fIPixelmon$subDrawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
        subDrawGradientRect(var1,var2,var3,var4,var5,var6);
    }

    @Override
    default List<GuiButton> fIPixelmon$getButtonList() {
        return getButtonList();
    }

    @Override
    default void fIPixelmon$renderToolTipPublic(ItemStack var1, int var2, int var3) {
        renderToolTipPublic(var1,var2,var3);
    }
}
