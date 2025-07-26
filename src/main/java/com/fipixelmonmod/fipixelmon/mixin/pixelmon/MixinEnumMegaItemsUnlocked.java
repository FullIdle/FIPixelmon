package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.bridge.EnumMegaItemsUnlockedBridge;
import com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper;
import com.fipixelmonmod.fipixelmon.helper.ObjectHelper;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = EnumMegaItemsUnlocked.class,
        remap = false
)
public class MixinEnumMegaItemsUnlocked implements EnumMegaItemsUnlockedBridge {
    @Shadow
    @Final
    public static EnumMegaItemsUnlocked None;

    @Shadow
    @Final
    public static EnumMegaItemsUnlocked Mega;

    @Shadow
    @Final
    public static EnumMegaItemsUnlocked Both;

    @Shadow
    @Final
    public static EnumMegaItemsUnlocked Dynamax;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/pixelmonmod/pixelmon/enums/EnumMegaItemsUnlocked;$VALUES:[Lcom/pixelmonmod/pixelmon/enums/EnumMegaItemsUnlocked;",
                    shift = At.Shift.AFTER
            )
    )
    private static void clinit(CallbackInfo ci) {
        EnumMegaItemsUnlockedHelper.inject();
    }

    /**
     * @author FIGSQ
     * @reason 增加多一个，这个设计非常蠢
     */
    @Overwrite
    public boolean canMega() {
        return ObjectHelper.equals(this, Mega) || ObjectHelper.equals(this, Both) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.BOTH_MEGA) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.THREE);
    }

    /**
     * @author FIGSQ
     * @reason 增加多一个，这个设计非常蠢
     */
    @Overwrite
    public boolean canDynamax() {
        return ObjectHelper.equals(this, Dynamax) || ObjectHelper.equals(this, Both) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.BOTH_DYN) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.THREE);
    }

    @Override
    public boolean fIPixelmon$canTerastal() {
        return ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.TERASTAL) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.BOTH_MEGA) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.BOTH_DYN) ||
                ObjectHelper.equals(this, EnumMegaItemsUnlockedHelper.THREE);
    }
}
