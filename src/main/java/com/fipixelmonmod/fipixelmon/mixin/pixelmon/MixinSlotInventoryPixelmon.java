package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.SlotInventoryPixelmonBridge;
import com.pixelmonmod.pixelmon.client.gui.inventory.SlotInventoryPixelmon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
        value = SlotInventoryPixelmon.class,
        remap = false
)
public abstract class MixinSlotInventoryPixelmon implements SlotInventoryPixelmonBridge {
    @Shadow abstract void setX(int newX);

    @Override
    public void fIPixelmon$setX(int x) {
        setX(x);
    }
}
