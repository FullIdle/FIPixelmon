package com.fipixelmonmod.fipixelmon.resource;

import net.minecraft.client.resources.FileResourcePack;
import net.minecraftforge.fml.common.FMLLog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ZipResourcePack extends FileResourcePack implements FIPResourcePack {
    public static Map<File, ZipResourcePack> instanceCache = new HashMap<>();

    public static ZipResourcePack getInstance(File file) {
        return instanceCache.computeIfAbsent(file, ZipResourcePack::new);
    }

    public ZipResourcePack(File file) {
        super(file);
    }

    protected InputStream getInputStreamByName(String resourceName) throws IOException {
        try {
            return super.getInputStreamByName(resourceName);
        } catch (IOException e) {
            if ("pack.mcmeta".equals(resourceName)) {
                FMLLog.log.debug("Mod {} is missing a pack.mcmeta file, substituting a dummy one", this.resourcePackFile.getAbsolutePath());
                return new ByteArrayInputStream(("{\n \"pack\": {\n   \"description\": \"dummy FML pack for " + this.resourcePackFile.getAbsolutePath() + "\",\n   \"pack_format\": 2\n}\n}").getBytes(StandardCharsets.UTF_8));
            } else {
                throw e;
            }
        }
    }
}
