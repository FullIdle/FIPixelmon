package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartSpectate implements IMessage {
   private EnumBattleType battleType;
   private UUID uuid;

   public StartSpectate() {
      this.battleType = EnumBattleType.Single;
   }

   public StartSpectate(UUID spectatedPlayer, EnumBattleType battleType) {
      this.battleType = EnumBattleType.Single;
      this.battleType = battleType;
      this.uuid = spectatedPlayer;
   }

   public void fromBytes(ByteBuf buf) {
      this.battleType = EnumBattleType.values()[buf.readInt()];
      this.uuid = new UUID(buf.readLong(), buf.readLong());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.battleType.ordinal());
      buf.writeLong(this.uuid.getMostSignificantBits());
      buf.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(StartSpectate message, MessageContext ctx) {
         ClientProxy.battleManager.spectatingUUID = message.uuid;
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientProxy.battleManager.startSpectate(message.battleType);
         });
         return null;
      }
   }
}
