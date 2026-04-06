package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import lombok.var;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnumGuiScreen.class, remap = false)
public class MixinEnumGuiScreen {
    @Mutable
    @Shadow @Final private static EnumGuiScreen[] $VALUES;

    @Invoker("<init>")
    private static EnumGuiScreen create(String enumName, int ordinal) {
        throw new IllegalStateException("Unreachable");
    }

    @Inject(method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumGuiScreen;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumGuiScreen;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false)
    private static void addEnum(CallbackInfo ci) {
        var index = $VALUES.length;
        $VALUES = ArrayUtils.add(
                $VALUES,
                create("PokeChecker", index++)
        );
    }
}
