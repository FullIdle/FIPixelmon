package com.fipixelmonmod.fipixelmon.resource;

import net.minecraft.client.resources.IResourcePack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface FIPResourcePack extends IResourcePack {
    @Override
    default Set<String> getResourceDomains() {
        return new HashSet<>(Arrays.asList("pixelmon", "fipixelmon"));
    }
}
