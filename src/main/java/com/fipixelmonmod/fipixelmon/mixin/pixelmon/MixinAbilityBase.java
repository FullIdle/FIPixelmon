package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.data.AbilityConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.pixelmonmod.pixelmon.api.spawning.IRarityTweak;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Mixin(value = AbilityBase.class, remap = false)
public abstract class MixinAbilityBase implements ITranslatable, IRarityTweak {
    @Inject(
            method = "getAbility(Ljava/lang/String;)Ljava/util/Optional;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/regex/Matcher;replaceAll(Ljava/lang/String;)Ljava/lang/String;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private static void getAbility(String name, CallbackInfoReturnable<Optional<AbilityBase>> cir, @Local(name = "name") String localName) {
        val config = AbilityConfig.extraAbilities.get(localName);
        if (config != null) try {
            cir.setReturnValue(Optional.of(config.representedClass.getConstructor().newInstance()));
            cir.cancel();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(
            method = "getName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getName(CallbackInfoReturnable<String> cir) {
        if (!AbilityConfig.clazzAbilities.containsKey(this.getClass())) return;
        cir.setReturnValue(AbilityConfig.clazzAbilities.get(this.getClass()).name);
        cir.cancel();
    }
}
