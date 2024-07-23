package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import lombok.SneakyThrows;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import static com.pixelmonmod.pixelmon.items.ItemUIElement.getHoverImage;
import static com.pixelmonmod.pixelmon.items.ItemUIElement.getImage;

@Mixin(value = ItemUIElement.class,remap = false)
public abstract class MixinUIElement {
    @SneakyThrows
    @Inject(
            method = "getImage(Lnet/minecraft/item/ItemStack;Z)Ljava/util/Optional;",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void getImageX(ItemStack stack, boolean hover, CallbackInfoReturnable<Optional<ResourceLocation>> cir){
        Optional<ResourceLocation> location = hover ? getHoverImage(stack) : getImage(stack);
        if (location.isPresent()) {
            ResourceLocation resourceLocation = location.get();
            if (resourceLocation.getResourceDomain().equalsIgnoreCase("url")) {
                String resourcePath = resourceLocation.getResourcePath();
                String[] split = resourcePath.split("\\|");
                String filePath = split[split.length-1];
                File file = new File(FIPixelmon.fiPixelmonFolder, filePath);
                Optional<ResourceLocation> value = Optional.of(new ResourceLocation("pixelmon", filePath));
                if (file.exists()) {
                    cir.setReturnValue(value);
                    cir.cancel();
                    return;
                }else{
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                String spec = split[0];
                FileUtils.copyURLToFile(new URL(spec),file);
                cir.setReturnValue(value);
                cir.cancel();
                return;
            }
        }
    }
}
