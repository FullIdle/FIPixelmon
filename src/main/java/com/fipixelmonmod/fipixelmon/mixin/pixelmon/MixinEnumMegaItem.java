package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.EnumMegaItemBridge;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemHelper;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnumMegaItem.class, remap = false)
public class MixinEnumMegaItem implements EnumMegaItemBridge {
    @Shadow @Final private int type;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumMegaItem;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumMegaItem;",
                    shift = At.Shift.AFTER
            )
    )
    private static void clinit(CallbackInfo ci) {
        EnumMegaItemHelper.inject();
    }

    @Override
    public boolean fIPixelmon$isTerastal() {
        return this.type == 2;
    }
}
