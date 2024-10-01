package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.fipixelmonmod.fipixelmon.resource.FIPDataFolderResourcePack;
import com.fipixelmonmod.fipixelmon.resource.ZipResourcePack;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Mixin(value = SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager {
    @Shadow
    public abstract void reloadResources(List<IResourcePack> p_110541_1_);

    @SneakyThrows
    @Inject(
            method = "reloadResources",
            at = @At("HEAD")
    )
    private void reloadResources(List<IResourcePack> resourcePacks, CallbackInfo ci) {
        Method method = ReflectionHelper.findMethod(URLClassLoader.class, "addURL", "addURL", URL.class);
        ClassLoader classLoader = Minecraft.class.getClassLoader();
        File[] files = FIPixelmon.fiPixelmonFolder.listFiles();
        assert files != null;
        for (File file : files)
            if (FileHelper.isZip(file)) {
                method.invoke(classLoader, file.toURI().toURL());
                ZipResourcePack filePack = new ZipResourcePack(file);
                resourcePacks.add(filePack);
                for (String domain : filePack.getResourceDomains())
                    FIPixelmon.logger.info("Link the resources of {} and {}", file.getPath(), domain);
            }
        FIPixelmon.logger.info("Link the resources Pixelmon and FIP!");
        resourcePacks.add(new FIPDataFolderResourcePack(FIPixelmon.fiPixelmonFolder));
    }
}
