package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.pc.GuiPC;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientQueryResultsPC implements IMessage {
   private int searchId;
   private List results;

   public ClientQueryResultsPC() {
   }

   public ClientQueryResultsPC(int searchId, List results) {
      this.searchId = searchId;
      this.results = results;
   }

   public void toBytes(ByteBuf buf) {
      try {
         buf.writeShort(this.searchId);
         buf.writeShort(this.results.size());
         int box = -1;

         for(int i = 0; i < this.results.size(); ++i) {
            Pokemon pokemon = (Pokemon)this.results.get(i);
            if (box != pokemon.getPosition().box) {
               box = pokemon.getPosition().box;
               buf.writeShort(-box - 1);
            }

            buf.writeShort(pokemon.getPosition().order);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.searchId = buf.readShort();
         this.results = new ArrayList();
         int length = buf.readShort();
         int box = -1;

         for(int i = 0; i < length; ++i) {
            int x = buf.readShort();
            int order = x;
            if (x < 0) {
               box = -x - 1;
               order = buf.readShort();
            }

            this.results.add(ClientStorageManager.openPC.get(box, order));
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ClientQueryResultsPC message, MessageContext ctx) {
         try {
            Minecraft.func_71410_x().func_152344_a(() -> {
               if (GuiPC.search != null && GuiPC.search.searchId == message.searchId) {
                  GuiPC.search.rearrangeBoxes(message.results);
               }

            });
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         return null;
      }
   }
}
