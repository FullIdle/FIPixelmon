package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RemoveSpectator implements IMessage {
   public int battleControllerIndex;
   public UUID uuid;

   public RemoveSpectator() {
   }

   public RemoveSpectator(int battleControllerIndex, UUID uuid) {
      this.battleControllerIndex = battleControllerIndex;
      this.uuid = uuid;
   }

   public void fromBytes(ByteBuf buffer) {
      this.battleControllerIndex = buffer.readInt();
      this.uuid = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.battleControllerIndex);
      buffer.writeLong(this.uuid.getMostSignificantBits());
      buffer.writeLong(this.uuid.getLeastSignificantBits());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RemoveSpectator message, MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_143004_u();
         BattleControllerBase bc = BattleRegistry.getBattle(message.battleControllerIndex);
         if (bc != null) {
            bc.removeSpectator(ctx.getServerHandler().field_147369_b);
         }

      }
   }
}
