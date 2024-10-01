package com.fipixelmonmod.fipixelmon.resource;

import net.minecraft.client.resources.FileResourcePack;

import java.io.File;

public class ZipResourcePack extends FileResourcePack implements FIPResourcePack {
    public ZipResourcePack(File file) {
        super(file);
    }
}
