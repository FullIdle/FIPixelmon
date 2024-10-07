package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import lombok.SneakyThrows;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin({MinecraftServer.class})
public class MixinMinecraftServer {
    @SneakyThrows
    @Inject(method = {"<clinit>"}, at = {@At("HEAD")}, remap = false)
    private static void clinit(CallbackInfo ci) {
        Method method = ReflectionHelper.findMethod(URLClassLoader.class, "addURL", "addURL", URL.class);
        ClassLoader classLoader = MinecraftServer.class.getClassLoader();
        File[] files = FIPixelmon.fiPixelmonFolder.listFiles();
        assert files != null;
        for (File file : files) {
            if (FileHelper.isZip(file)) {
                ZipFile zipFile = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                URL fileUrl = file.toURI().toURL();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    String name = zipEntry.getName();
                    if (!name.contains("/") || name.indexOf("/") == name.lastIndexOf("/"))
                        method.invoke(classLoader, new URL("jar:" + fileUrl + "!/" + name));
                }
                FileHelper.loadedZipFile.add(file);
                method.invoke(classLoader, fileUrl);
                zipFile.close();
            }
        }
    }
}

