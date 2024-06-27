package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets.ClientCloseScreenPacket;
import com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets.ClientOpenScreenPacket;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.PacketRegistry;
import com.pixelmonmod.pixelmon.util.helpers.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketRegistry.class,remap = false)
public class MixinPacketRegister {
    @Inject(
            method = "registerPackets",
            remap = false,
            at = @At("TAIL")
    )
    private static void registerPackets(CallbackInfo ci){
        int id = ReflectionHelper.getPrivateValue(PacketRegistry.class, null, "id");
        Pixelmon.network.registerMessage(ClientOpenScreenPacket.Handler.class, ClientOpenScreenPacket.class, id++, Side.SERVER);
        Pixelmon.network.registerMessage(ClientCloseScreenPacket.Handler.class,ClientCloseScreenPacket.class,id++,Side.SERVER);
        ReflectionHelper.setPrivateValue(PacketRegistry.class,null,id,"id");
    }
}
