package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.entities.pixelmon.interactions.InteractionTeraShard;
import com.pixelmonmod.pixelmon.CommonProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = CommonProxy.class, remap = false
)
public class MixinCommonProxy {
    /**
     * 太晶碎块交互
     */
    @Inject(
            method = "registerInteractions",
            at = @At("HEAD")
    )
    private void registerInteractions(CallbackInfo ci) {
        EntityPixelmon.interactionList.add(InteractionTeraShard.INSTANCE);
    }
}
