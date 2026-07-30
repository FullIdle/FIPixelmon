package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.events.RecalculateStatsEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Stats.class, remap = false)
public class MixinStats {
    @Inject(
            method = "recalculateStats",
            at = @At("HEAD"),
            cancellable = true
    )
    private void recalculateStats$HEAD(CallbackInfo ci) {
        if (Pixelmon.EVENT_BUS.post(new RecalculateStatsEvent.Pre(((Stats) (Object) this)))) {
            ci.cancel();
            return;
        }
    }

    @Inject(
            method = "recalculateStats",
            at = @At("RETURN")
    )
    private void recalculateStats$RETURN(CallbackInfo ci) {
        Pixelmon.EVENT_BUS.post(new RecalculateStatsEvent.Post(((Stats) (Object) this)));
    }
}
