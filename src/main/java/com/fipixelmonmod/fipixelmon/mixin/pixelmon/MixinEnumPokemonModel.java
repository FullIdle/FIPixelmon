package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(value = EnumPokemonModel.class,remap = false)
public class MixinEnumPokemonModel {
    @Shadow
    String fileName;

    @Inject(method = "getModelLocation",remap = false,
            at = @At("HEAD"),
            cancellable = true)
    private void getModelLocation(CallbackInfoReturnable<ResourceLocation> cir){
        if (new File(FIPixelmon.fiPixelmonFolder,fileName).exists()) {
            cir.setReturnValue(new ResourceLocation("pixelmon",fileName));
            cir.cancel();
        }
    }
}
