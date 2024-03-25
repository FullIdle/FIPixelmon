package com.pixelmonmod.pixelmon.comm.packetHandlers.battles.music;

import com.pixelmonmod.pixelmon.client.music.BattleMusic;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.sounds.BattleMusicType;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayBattleMusic implements IMessage {
   BattleMusicType type;
   int songIndex;
   long playtime;
   boolean repeat;

   public PlayBattleMusic() {
   }

   public PlayBattleMusic(BattleMusicType type, int songIndex, long playtime, boolean repeat) {
      this.type = type;
      this.songIndex = songIndex;
      this.playtime = playtime;
      this.repeat = repeat;
   }

   public void fromBytes(ByteBuf buf) {
      this.type = BattleMusicType.getFromIndex(buf.readByte());
      this.songIndex = buf.readInt();
      this.playtime = buf.readLong();
      this.repeat = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.type.ordinal());
      buf.writeInt(this.songIndex);
      buf.writeLong(this.playtime);
      buf.writeBoolean(this.repeat);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(PlayBattleMusic message, MessageContext ctx) {
         BattleMusic.startBattleMusic(message.type, message.songIndex, message.playtime, message.repeat);
      }
   }
}
