package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.helper.ResourcesHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = GuiResources.class,
        remap = false
)
public class MixinGuiResources {
    @Shadow public static ResourceLocation pixelmonOverlayExtended2;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        pixelmonOverlayExtended2 = ResourcesHelper.PIXELMON_OVERLAY_EXTENDED_2;
    }
}
