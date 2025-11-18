package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.fipixelmonmod.fipixelmon.resource.FIPDataFolderResourcePack;
import com.fipixelmonmod.fipixelmon.resource.ZipResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

@Mixin(value = SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager implements IResourceManager {
    @Shadow
    public abstract void reloadResources(List<IResourcePack> p_110541_1_);

    @Shadow
    @Final
    private Map<String, FallbackResourceManager> domainResourceManagers;

    @Inject(
            method = "reloadResourcePack",
            at = @At("TAIL")
    )
    private void reloadResourcePack(IResourcePack resourcePack, CallbackInfo ci) {
        if (resourcePack.getResourceDomains().contains("pixelmon")) {
            FallbackResourceManager pixelmonResourcePack = this.domainResourceManagers.get("pixelmon");
            for (File file : FileHelper.loadedZipFile) {
                ZipResourcePack instance = ZipResourcePack.getInstance(file);
                pixelmonResourcePack.addResourcePack(instance);
                for (String domain : instance.getResourceDomains())
                    FIPixelmon.logger.info("Link the resources of {} and {}", file.getPath(), domain);
            }
            pixelmonResourcePack.addResourcePack(FIPDataFolderResourcePack.instance);
            FIPixelmon.logger.info("Link the resources Pixelmon and FIP!");
        }
    }
}
