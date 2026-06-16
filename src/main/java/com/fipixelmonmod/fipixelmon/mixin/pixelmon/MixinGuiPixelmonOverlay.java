package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.Config;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = GuiPixelmonOverlay.class, remap = false
)
public class MixinGuiPixelmonOverlay {
    @Inject(
            method = "onRenderGameOverlay",
            at = @At("HEAD"),
            cancellable = true)
    public void shieldingRender(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        if (Config.INSTANCE.shieldingOverlay) ci.cancel();
    }
}
