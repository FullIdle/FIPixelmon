package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BattleGuiClosed implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(BattleGuiClosed message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            this.processMessage(message, ctx);
         });
         return null;
      }

      private void processMessage(BattleGuiClosed message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         BattleControllerBase b = BattleRegistry.getBattle(player);
         if (b != null && b.battleEnded) {
            BattleRegistry.deRegisterBattle(b);
         }

         PlayerPartyStorage pps = Pixelmon.storageManager.getParty(player);
         Iterator var6 = pps.transientData.onceBattleDone.iterator();

         while(var6.hasNext()) {
            Consumer consumer = (Consumer)var6.next();
            consumer.accept(Optional.ofNullable(b));
         }

         pps.transientData.onceBattleDone.clear();
      }
   }
}
