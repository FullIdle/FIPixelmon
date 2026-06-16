package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.Config;
import com.pixelmonmod.tcg.gui.GuiTCGOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = GuiTCGOverlay.class, remap = false
)
public class MixinGuiTCGOverlay {
    @Inject(
            method = "onRenderGameOverlay",
            at = @At("HEAD"),
            cancellable = true
    )
    public void shieldingRender(RenderGameOverlayEvent.Pre event, CallbackInfo ci) {
        if (Config.INSTANCE.shieldingTCGOverlay) ci.cancel();
    }
}
