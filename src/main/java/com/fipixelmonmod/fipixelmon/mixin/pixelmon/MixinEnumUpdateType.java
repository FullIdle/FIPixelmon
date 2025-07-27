package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.helper.EnumUpdateTypeHelper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = EnumUpdateType.class,remap = false
)
public class MixinEnumUpdateType {
    @Shadow public static EnumUpdateType[] CLIENT;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/comm/EnumUpdateType;$VALUES:[Lcom/pixelmonmod/pixelmon/comm/EnumUpdateType;",
                    shift = At.Shift.AFTER
            )
    )
    private static void clinit(CallbackInfo ci) {
        EnumUpdateTypeHelper.inject();
    }

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void clinitTail(CallbackInfo ci) {
        CLIENT = ArrayUtils.add(CLIENT,EnumUpdateTypeHelper.getTeraType());
    }
}
