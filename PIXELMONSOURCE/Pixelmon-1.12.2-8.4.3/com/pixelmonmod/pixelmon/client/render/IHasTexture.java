package com.pixelmonmod.pixelmon.client.render;

import javax.annotation.Nonnull;
import net.minecraft.util.ResourceLocation;

public interface IHasTexture {
   @Nonnull
   ResourceLocation getTexture();
}
