package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.util.PlayerUtil;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ServerSwap;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerSwap.Handler.class,remap = false)
public class MixinServerSwapHandler {
    @Inject(
            method = "onSyncMessage(Lcom/pixelmonmod/pixelmon/comm/packetHandlers/clientStorage/newStorage/ServerSwap;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V",
            remap = false,
            cancellable = true,
            at = @At("HEAD")
    )
    private void onSyncMessage(ServerSwap message, MessageContext ctx, CallbackInfo ci){
        EntityPlayerMP player = ctx.getServerHandler().player;
        if (!PlayerUtil.getNowGuiScreen(player.getUniqueID()).equals(EnumGuiScreen.PC)) {
            player.sendMessage(new TextComponentString("§c§l你的PC不安全,请重新打开!"));
            ci.cancel();
        }
    }
}
