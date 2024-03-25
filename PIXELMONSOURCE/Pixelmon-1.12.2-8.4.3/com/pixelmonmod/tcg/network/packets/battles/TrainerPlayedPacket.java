package com.pixelmonmod.tcg.network.packets.battles;

import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TrainerPlayedPacket implements IMessage {
   private String playerName;
   private TrainerCardState trainer;

   public TrainerPlayedPacket() {
   }

   public TrainerPlayedPacket(String playerName, TrainerCardState trainer) {
      this.playerName = playerName;
      this.trainer = trainer;
   }

   public void fromBytes(ByteBuf buf) {
      this.playerName = ByteBufUtils.readUTF8String(buf);
      this.trainer = new TrainerCardState(ByteBufTCG.readCard(buf));
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.playerName);
      ByteBufTCG.writeCard(buf, this.trainer.getData());
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(TrainerPlayedPacket msg, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e != null && mc.field_71462_r instanceof GuiTCG) {
               GuiTCG gui = (GuiTCG)mc.field_71462_r;
               gui.getInspectingCard().set(msg.trainer, false, BoardLocation.Trainer, 0, true, String.format("%s just played", msg.playerName));
            }
         }

         return null;
      }
   }
}
