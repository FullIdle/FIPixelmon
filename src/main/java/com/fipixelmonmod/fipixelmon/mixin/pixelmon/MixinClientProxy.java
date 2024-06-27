package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.commands.BattleFixesCommand;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import net.minecraftforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinClientProxy {
    @Inject(
            method = "registerCommands",
            remap = false,
            at = @At("HEAD")
    )
    private void registerCommands(CallbackInfo ci){
        ClientCommandHandler.instance.registerCommand(new BattleFixesCommand());
    }
}
