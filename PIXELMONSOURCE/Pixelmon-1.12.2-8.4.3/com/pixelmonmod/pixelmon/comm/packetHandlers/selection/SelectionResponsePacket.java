package com.pixelmonmod.pixelmon.comm.packetHandlers.selection;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.selection.PartySelectionFactory;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectionResponsePacket implements IMessage {
   private List selected;

   public SelectionResponsePacket() {
   }

   public SelectionResponsePacket(List selected) {
      this.selected = selected;
   }

   public void fromBytes(ByteBuf buf) {
      if (!buf.readBoolean()) {
         this.selected = null;
      } else {
         this.selected = Lists.newArrayList();
         int size = buf.readInt();

         for(int i = 0; i < size; ++i) {
            this.selected.add(buf.readInt());
         }

      }
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.selected != null);
      if (this.selected != null) {
         buf.writeInt(this.selected.size());
         Iterator var2 = this.selected.iterator();

         while(var2.hasNext()) {
            Integer integer = (Integer)var2.next();
            buf.writeInt(integer);
         }

      }
   }

   public static class Handler implements IMessageHandler {
      public SelectionResponsePacket onMessage(SelectionResponsePacket message, MessageContext ctx) {
         if (!PartySelectionFactory.inSelection(ctx.getServerHandler().field_147369_b)) {
            return null;
         } else {
            PartySelectionFactory.SelectionData selectionConsumer = PartySelectionFactory.getSelectionConsumer(ctx.getServerHandler().field_147369_b);
            if (selectionConsumer == null) {
               return null;
            } else if (message.selected == null) {
               if (selectionConsumer.getConsumer() != null) {
                  selectionConsumer.getConsumer().accept(ctx.getServerHandler().field_147369_b, Collections.emptyList());
               }

               PartySelectionFactory.removeSelection(ctx.getServerHandler().field_147369_b);
               return null;
            } else {
               List pokemon = selectionConsumer.getPokemon();
               List selected = Lists.newArrayList();
               Iterator var6 = message.selected.iterator();

               while(var6.hasNext()) {
                  Integer integer = (Integer)var6.next();
                  selected.add(pokemon.get(integer));
               }

               if (selectionConsumer.getConsumer() != null) {
                  selectionConsumer.getConsumer().accept(ctx.getServerHandler().field_147369_b, selected);
               }

               PartySelectionFactory.removeSelection(ctx.getServerHandler().field_147369_b);
               return null;
            }
         }
      }
   }
}
