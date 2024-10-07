package com.fipixelmonmod.fipixelmon.resource;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

public class FIPDataFolderResourcePack extends FolderResourcePack implements FIPResourcePack {
    public static final FIPDataFolderResourcePack instance = new FIPDataFolderResourcePack(FIPixelmon.fiPixelmonFolder);

    private FIPDataFolderResourcePack(File file) {
        super(file);
    }

    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        return hasResourceName(p_110589_1_.getResourcePath());
    }

    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        return getInputStreamByName(p_110590_1_.getResourcePath());
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
}
