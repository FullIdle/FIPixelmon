package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class CheckRulesVersion implements IMessage {
   int version;
   IMessage packet;
   private static CheckRulesVersion currentPacket;

   protected CheckRulesVersion() {
   }

   public CheckRulesVersion(int version, IMessage packet) {
      this.version = version;
      this.packet = packet;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.version);
      this.packet.toBytes(buf);
   }

   public void fromBytes(ByteBuf buf) {
      this.version = buf.readInt();
      this.readPacket(buf);
   }

   protected abstract void readPacket(ByteBuf var1);

   public abstract void processPacket(MessageContext var1);

   protected void onMessage(MessageContext ctx) {
      int currentVersion = BattleClauseRegistry.getClauseVersion();
      if (currentVersion != 0 && currentVersion != this.version) {
         currentPacket = this;
         Pixelmon.network.sendToServer(new RequestCustomRulesUpdate());
      } else {
         this.processPacket(ctx);
      }

   }

   static void processStoredPacket(MessageContext ctx) {
      if (currentPacket != null) {
         currentPacket.processPacket(ctx);
      }

   }
}
