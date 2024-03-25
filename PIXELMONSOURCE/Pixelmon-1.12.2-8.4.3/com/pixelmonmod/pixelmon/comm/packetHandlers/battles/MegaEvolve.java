package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.MegaEvolution;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MegaEvolve implements IMessage {
   UUID pokemonUUID;
   boolean ultraBurst;

   public MegaEvolve() {
   }

   public MegaEvolve(UUID pokemonUUID, boolean ultraBurst) {
      this.pokemonUUID = pokemonUUID;
      this.ultraBurst = ultraBurst;
   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.pokemonUUID);
      buf.writeBoolean(this.ultraBurst);
   }

   public void fromBytes(ByteBuf buf) {
      this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
      this.ultraBurst = buf.readBoolean();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(MegaEvolve message, MessageContext ctx) {
         if (message.ultraBurst) {
            ClientProxy.battleManager.ultraBurst = message.pokemonUUID;
            ClientProxy.battleManager.hasUltraBurst = true;
         } else {
            ClientProxy.battleManager.megaEvolution = message.pokemonUUID;
         }

         ClientProxy.battleManager.mode = BattleMode.MegaEvolution;
         MegaEvolution.selectEntity();
         return null;
      }
   }
}
