package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BattleSwitch implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(BattleSwitch message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientProxy.battleManager.mode = BattleMode.EnforcedSwitch;
            ClientProxy.battleManager.oldMode = BattleMode.MainMenu;
         });
         return null;
      }
   }
}
