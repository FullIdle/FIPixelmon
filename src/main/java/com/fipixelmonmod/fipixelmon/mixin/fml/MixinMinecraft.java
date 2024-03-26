package com.fipixelmonmod.fipixelmon.mixin.fml;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.common.FIPResourcePack;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Mixin(value = Minecraft.class)
public class MixinMinecraft {
    @Shadow private IReloadableResourceManager mcResourceManager;

    @Shadow private static Minecraft instance;

    @SneakyThrows
    @Inject(method = "init",
            at = @At("TAIL")
    )
    private void init(CallbackInfo ci){
        Field field = SimpleReloadableResourceManager.class.getDeclaredField("field_110548_a");
        field.setAccessible(true);
        Field field1 = FallbackResourceManager.class.getDeclaredField("field_110540_a");
        field1.setAccessible(true);
        Map<String, FallbackResourceManager> map = (Map<String, FallbackResourceManager>) field.get(instance.getResourceManager());
        FallbackResourceManager fallbackRM = map.get("pixelmon");
        fallbackRM.addResourcePack(new FIPResourcePack(FIPixelmon.fiPixelmonFolder));
        FIPixelmon.logger.info("ADD FIP ASSETS");
    }
}
