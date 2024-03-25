package com.fipixelmonmod.fipixelmon.mixin;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Pixelmon.class,remap = false)
public class MixinPixelmon {
    @Inject(method = "preInit",at = @At("HEAD"),remap = false)
    private void perInit(FMLPreInitializationEvent event, CallbackInfo ci){
    }
}
