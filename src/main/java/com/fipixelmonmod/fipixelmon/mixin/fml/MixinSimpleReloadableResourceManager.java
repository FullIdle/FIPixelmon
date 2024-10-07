package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.fipixelmonmod.fipixelmon.resource.FIPDataFolderResourcePack;
import com.fipixelmonmod.fipixelmon.resource.ZipResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(value = SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager {
    @Shadow
    public abstract void reloadResources(List<IResourcePack> p_110541_1_);

    @Inject(
            method = "reloadResources",
            at = @At("HEAD")
    )
    private void reloadResources(List<IResourcePack> resourcePacks, CallbackInfo ci) {
        for (File file : FileHelper.loadedZipFile) {
            ZipResourcePack instance = ZipResourcePack.getInstance(file);
            resourcePacks.add(instance);
            for (String domain : instance.getResourceDomains())
                FIPixelmon.logger.info("Link the resources of {} and {}", file.getPath(), domain);
        }
        FIPixelmon.logger.info("Link the resources Pixelmon and FIP!");
        resourcePacks.add(FIPDataFolderResourcePack.instance);
    }
}
