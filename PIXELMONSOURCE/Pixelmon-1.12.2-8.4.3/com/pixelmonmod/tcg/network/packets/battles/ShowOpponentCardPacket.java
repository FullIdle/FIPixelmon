package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShowOpponentCardPacket implements IMessage {
   private String message;
   private String playerName;
   private ImmutableCard card;

   public ShowOpponentCardPacket() {
   }

   public ShowOpponentCardPacket(String message, String playerName, ImmutableCard card) {
      this.message = message;
      this.playerName = playerName;
      this.card = card;
   }

   public void fromBytes(ByteBuf buf) {
      this.message = ByteBufUtils.readUTF8String(buf);
      this.playerName = ByteBufUtils.readUTF8String(buf);
      this.card = CardRegistry.fromCode(ByteBufUtils.readUTF8String(buf));
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.message);
      ByteBufUtils.writeUTF8String(buf, this.playerName);
      ByteBufUtils.writeUTF8String(buf, this.card.getCode());
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(ShowOpponentCardPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e != null && mc.field_71462_r instanceof GuiTCG) {
               GuiTCG gui = (GuiTCG)mc.field_71462_r;
               gui.getInspectingCard().set(new CommonCardState(msg.card), false, BoardLocation.Trainer, 0, true, String.format(msg.message, msg.playerName));
            }
         }

         return null;
      }
   }
}
