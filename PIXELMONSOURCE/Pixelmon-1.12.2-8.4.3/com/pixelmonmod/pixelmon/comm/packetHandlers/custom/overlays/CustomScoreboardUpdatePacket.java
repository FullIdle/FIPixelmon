package com.pixelmonmod.pixelmon.comm.packetHandlers.custom.overlays;

import com.pixelmonmod.pixelmon.client.gui.custom.overlays.CustomScoreboardOverlay;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardLocation;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomScoreboardUpdatePacket implements IMessage {
   private String title;
   private Collection lines;
   private Collection scores;

   public CustomScoreboardUpdatePacket() {
   }

   public CustomScoreboardUpdatePacket(String title, Collection lines, Collection scores) {
      this.title = title;
      this.lines = lines;
      this.scores = scores;
   }

   public void fromBytes(ByteBuf buf) {
      this.title = ByteBufUtils.readUTF8String(buf);
      int lineAmount = buf.readByte();
      this.lines = new ArrayList(lineAmount);

      for(int i = 0; i < lineAmount; ++i) {
         this.lines.add(ByteBufUtils.readUTF8String(buf));
      }

      int scoreAmount = buf.readShort();
      if (scoreAmount != 254) {
         this.scores = new ArrayList(scoreAmount);

         for(int i = 0; i < scoreAmount; ++i) {
            this.scores.add(ByteBufUtils.readUTF8String(buf));
         }
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.title);
      buf.writeByte(this.lines.size());
      Iterator var2 = this.lines.iterator();

      String score;
      while(var2.hasNext()) {
         score = (String)var2.next();
         ByteBufUtils.writeUTF8String(buf, score);
      }

      if (this.scores != null) {
         buf.writeShort(this.scores.size());
         var2 = this.scores.iterator();

         while(var2.hasNext()) {
            score = (String)var2.next();
            ByteBufUtils.writeUTF8String(buf, score);
         }
      } else {
         buf.writeShort(254);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CustomScoreboardUpdatePacket message, MessageContext ctx) {
         CustomScoreboardOverlay.resetBoard();
         CustomScoreboardOverlay.populate((ScoreboardLocation)null, message.title, message.lines, message.scores);
         return null;
      }
   }
}
