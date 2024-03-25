package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.gui.battles.GuiAcceptDeny;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BattleQueryPacket implements IMessage {
   public UUID opponentUUID;
   public int queryIndex;
   public int[] pokeballs = new int[]{-1, -1, -1, -1, -1, -1};

   public BattleQueryPacket() {
   }

   public BattleQueryPacket(int queryIndex, UUID opponentUUID, PlayerPartyStorage opponentStorage) {
      this.queryIndex = queryIndex;
      this.opponentUUID = opponentUUID;

      for(int i = 0; i < 6; ++i) {
         if (opponentStorage.getAll()[i] != null) {
            this.pokeballs[i] = opponentStorage.getAll()[i].getCaughtBall().getIndex();
            if (opponentStorage.getAll()[i].getHealth() < 1) {
               this.pokeballs[i] = this.pokeballs[i] * -1 - 1;
            }
         } else {
            this.pokeballs[i] = -999;
         }
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.queryIndex = buffer.readInt();
      this.opponentUUID = UUID.fromString(ByteBufUtils.readUTF8String(buffer));

      for(int i = 0; i < 6; ++i) {
         this.pokeballs[i] = buffer.readShort();
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.queryIndex);
      ByteBufUtils.writeUTF8String(buffer, this.opponentUUID.toString());

      for(int i = 0; i < 6; ++i) {
         buffer.writeShort(this.pokeballs[i]);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(BattleQueryPacket message, MessageContext ctx) {
         GuiAcceptDeny.opponent = message;
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(BattleQueryPacket message) {
         Minecraft.func_71410_x().func_147108_a(new GuiAcceptDeny(message.queryIndex));
      }
   }
}
