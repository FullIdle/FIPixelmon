package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.ItemHeldConfig;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import lombok.val;
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

@Mixin(value = EnumHeldItems.class, remap = false)
public class MixinEnumHeldItems {
    @Mutable
    @Shadow
    @Final
    private static EnumHeldItems[] $VALUES;

    @Invoker("<init>")
    private static EnumHeldItems create(String enumName, int ordinal) {
        throw new IllegalStateException("Unreachable");
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lcom/pixelmonmod/pixelmon/enums/heldItems/EnumHeldItems;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/heldItems/EnumHeldItems;", ordinal = 0, shift = At.Shift.AFTER), remap = false)
    private static void addEnum(CallbackInfo ci) {
        val element = create("fipixelmon", $VALUES.length);
        $VALUES = ArrayUtils.add($VALUES, element);
        ItemHeldConfig.init(element);
    }
}
