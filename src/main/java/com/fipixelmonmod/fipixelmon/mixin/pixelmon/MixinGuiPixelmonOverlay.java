package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiPixelmonOverlay.class)
public class MixinGuiPixelmonOverlay {
    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    public void init(CallbackInfo ci){
        GuiPixelmonOverlay.icons.clear();
    }
}
