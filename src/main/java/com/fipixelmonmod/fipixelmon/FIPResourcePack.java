package com.fipixelmonmod.fipixelmon;

import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FIPResourcePack extends FolderResourcePack {
    public static ArrayList<FIPResourcePack> INSTANCES = new ArrayList<>();

    public FIPResourcePack(File p_i1291_1_) {
        super(p_i1291_1_);
        INSTANCES.add(this);
    }

    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        return hasResourceName(p_110589_1_.getResourcePath());
    }

    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        return getInputStreamByName(p_110590_1_.getResourcePath());
    }
}
