package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.commands.BattleFixesCommand;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

@Mixin(value = ClientBattleManager.class, remap = false)
public abstract class MixinClientBattleManager {
    @Shadow public ArrayList<IMessage> selectedActions;

    @Shadow public int selectedAttack;

    @SneakyThrows
    @Inject(method = "finishSelection"
            , at = @At("HEAD")
            , remap = false
    )
    private void finishSelection(CallbackInfo ci){
        BattleFixesCommand.cache.clear();
        for (IMessage action : this.selectedActions) {
            Constructor<? extends IMessage> con = action.getClass().getDeclaredConstructor();
            con.setAccessible(true);
            IMessage msg = con.newInstance();
            ByteBuf buffer = Unpooled.buffer();
            action.toBytes(buffer);
            msg.fromBytes(buffer);
            BattleFixesCommand.cache.add(msg);
        }
    }
}
