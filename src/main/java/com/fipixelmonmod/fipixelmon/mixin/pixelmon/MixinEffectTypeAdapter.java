package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.EffectTypeConfig;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.EffectTypeAdapter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(
        value = EffectTypeAdapter.class,
        remap = false
)
public class MixinEffectTypeAdapter {
    @Shadow @Final public static HashMap<String, Class<? extends EffectBase>> EFFECTS;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void injected(CallbackInfo ci) {
        EffectTypeConfig.extraEffectTypes.forEach((name,  config) -> EFFECTS.put(name, (Class<? extends EffectBase>) config.getRepresentedClass()));
    }
}
