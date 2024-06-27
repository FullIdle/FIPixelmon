package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.commands.BattleFixesCommand;
import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.fipixelmonmod.fipixelmon.common.data.functions.MinecraftFunction;
import com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets.ClientCloseScreenPacket;
import com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets.ClientOpenScreenPacket;
import com.fipixelmonmod.fipixelmon.common.util.GuiHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinClientProxy {
    @Inject(
            method = "registerCommands",
            remap = false,
            at = @At("HEAD")
    )
    private void registerCommands(CallbackInfo ci) {
        ClientCommandHandler.instance.registerCommand(new BattleFixesCommand());
    }

    @Inject(
            method = "postInit",
            remap = false,
            at = @At("TAIL")
    )
    private void postInit(CallbackInfo ci) {
        MinecraftFunction.displayGuiScreen = (gui -> {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null)
                return;
            EnumGuiScreen currentGui = GuiHelper.getPixelmonGuiType(mc.currentScreen);
            EnumGuiScreen newGui = GuiHelper.getPixelmonGuiType((GuiScreen) gui);
            UUID uniqueID = mc.player.getUniqueID();
            if (newGui == null && currentGui == null) return;
            if (newGui != null){
                if (currentGui != null) {
                    fIPixelmon$close(uniqueID,currentGui);
                }
                fIPixelmon$open(uniqueID,newGui);
                return;
            }
            fIPixelmon$close(uniqueID,currentGui);
        });
    }

    @Unique
    private static void fIPixelmon$close(UUID uuid, EnumGuiScreen type) {
        Cache.playerNowScreen.remove(uuid);
        Pixelmon.network.sendToServer(new ClientCloseScreenPacket(type));
    }

    @SneakyThrows
    @Unique
    private static void fIPixelmon$open(UUID uuid, EnumGuiScreen type) {
        Cache.playerNowScreen.put(uuid, type);
        Pixelmon.network.sendToServer(new ClientOpenScreenPacket(type));
    }
}
