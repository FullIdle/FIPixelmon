package com.pixelmonmod.tcg.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RulebookUpdatePacket implements IMessage {
   private int deckSize;
   private int prizeCount;
   private int timeLimit;
   private int eloMinimum;
   private String startingCommand;
   private String endingCommand;
   private String startingMessage;
   private String endingMessage;

   public RulebookUpdatePacket() {
   }

   public RulebookUpdatePacket(NBTTagCompound tag, int slot) {
      this.deckSize = tag.func_74762_e("DeckSize");
      this.prizeCount = tag.func_74762_e("PrizeCount");
      this.timeLimit = tag.func_74762_e("TimeLimit");
      this.eloMinimum = tag.func_74762_e("EloMinimum");
      this.startingCommand = tag.func_74779_i("StartingCommand");
      this.endingCommand = tag.func_74779_i("EndingCommand");
      this.startingMessage = tag.func_74779_i("StartingMessage");
      this.endingMessage = tag.func_74779_i("EndingMessage");
   }

   public void fromBytes(ByteBuf buf) {
      this.deckSize = buf.readInt();
      this.prizeCount = buf.readInt();
      this.timeLimit = buf.readInt();
      this.eloMinimum = buf.readInt();
      this.startingCommand = ByteBufUtils.readUTF8String(buf);
      this.endingCommand = ByteBufUtils.readUTF8String(buf);
      this.startingMessage = ByteBufUtils.readUTF8String(buf);
      this.endingMessage = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.deckSize);
      buf.writeInt(this.prizeCount);
      buf.writeInt(this.timeLimit);
      buf.writeInt(this.eloMinimum);
      ByteBufUtils.writeUTF8String(buf, this.startingCommand);
      ByteBufUtils.writeUTF8String(buf, this.endingCommand);
      ByteBufUtils.writeUTF8String(buf, this.startingMessage);
      ByteBufUtils.writeUTF8String(buf, this.endingMessage);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RulebookUpdatePacket message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            if (player.field_71071_by != null && player.field_71071_by.func_70448_g() != ItemStack.field_190927_a) {
               ItemStack book = player.field_71071_by.func_70448_g();
               if (!book.func_77942_o()) {
                  book.func_77982_d(new NBTTagCompound());
               }

               NBTTagCompound tag = book.func_77978_p();
               tag.func_74768_a("DeckSize", message.deckSize);
               tag.func_74768_a("PrizeCount", message.prizeCount);
               tag.func_74768_a("TimeLimit", message.timeLimit);
               tag.func_74768_a("EloMinimum", message.eloMinimum);
               tag.func_74778_a("StartingCommand", message.startingCommand);
               tag.func_74778_a("EndingCommand", message.endingCommand);
               tag.func_74778_a("StartingMessage", message.startingMessage);
               tag.func_74778_a("EndingMessage", message.endingMessage);
            }

         });
         return null;
      }
   }
}
