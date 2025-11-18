package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.PokeBallConfig;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiHelper.class, remap = false)
public class MixinGuiHelper {
    @Inject(
            method = "bindPokeballTexture",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void bindPokeballTexture(EnumPokeballs ball, CallbackInfo ci) {
        PokeBallConfig config = PokeBallConfig.extraPokeBallConfig.get(ball);
        if (config != null) {
            config.bindPokeballTexture();
            ci.cancel();
        }
    }
}
