package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class OpenPackGuiPacket implements IMessage {
   private ImmutableCard[] cards;

   public OpenPackGuiPacket() {
   }

   public OpenPackGuiPacket(ImmutableCard[] cards) {
      this.cards = cards;
   }

   public void fromBytes(ByteBuf buf) {
      int size = buf.readInt();
      this.cards = new ImmutableCard[size];

      for(int i = 0; i < size; ++i) {
         this.cards[i] = ByteBufTCG.readCard(buf);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.cards.length);
      ImmutableCard[] var2 = this.cards;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ImmutableCard card = var2[var4];
         ByteBufTCG.writeCard(buf, card);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(OpenPackGuiPacket m, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71439_g != null) {
               if (TCGConfig.getInstance() != null && mc != null) {
                  TCGConfig.getInstance().savedUIScale = mc.field_71474_y.field_74335_Z;
                  mc.field_71474_y.field_74335_Z = 4;
               }

               TCG.proxy.displayGuiPack(mc, 0, m.cards);
            }
         }

         return null;
      }
   }
}
