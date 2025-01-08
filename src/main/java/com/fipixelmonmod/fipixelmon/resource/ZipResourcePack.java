package com.fipixelmonmod.fipixelmon.resource;

import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ZipResourcePack extends FileResourcePack implements FIPResourcePack {
    public static Map<File, ZipResourcePack> instanceCache = new HashMap<>();

    public static ZipResourcePack getInstance(File file) {
        return instanceCache.computeIfAbsent(file, ZipResourcePack::new);
    }

    public ZipResourcePack(File file) {
        super(file);
    }

    @Override
    public Set<String> getResourceDomains() {
        return Collections.singleton("pixelmon");
    }

    @Override
    protected InputStream getInputStreamByName(String resourceName) throws IOException {
        try {
            return super.getInputStreamByName(resourceName);
        } catch (IOException var3) {
            if ("pack.mcmeta".equals(resourceName)) {
                FMLLog.log.debug("Folder {} is missing a pack.mcmeta file, substituting a dummy one", this.resourcePackFile.getAbsolutePath());
                return new ByteArrayInputStream(("{\n \"pack\": {\n   \"description\": \"dummy FML pack for " + "pixelmon" + "\",\n   \"pack_format\": 2\n}\n}").getBytes(StandardCharsets.UTF_8));
            } else {
                throw var3;
            }
        }
    }

    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        return this.hasResourceName(p_110589_1_.getResourcePath());
    }

    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        return this.getInputStreamByName(p_110590_1_.getResourcePath());
    }
}
