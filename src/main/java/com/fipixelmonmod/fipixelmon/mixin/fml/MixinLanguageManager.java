package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.resource.FIPResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = LanguageManager.class)
public class MixinLanguageManager {
    @Inject(
            method = "parseLanguageMetadata",
            at = @At("HEAD")
    )
    private void parseLanguageMetadata(List<IResourcePack> resourcePacks, CallbackInfo ci) {
        resourcePacks.removeIf(pack -> pack instanceof FIPResourcePack);
    }
}
