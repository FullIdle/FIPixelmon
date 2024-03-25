package com.pixelmonmod.pixelmon.comm;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientUpdatePokedex implements IMessage {
   private Map data;
   private Table data2;

   public ClientUpdatePokedex() {
   }

   public ClientUpdatePokedex(Pokedex p) {
      this.data = p.getSeenMap();
      this.data2 = p.formDex;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.data.size());
      Iterator var2 = this.data.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry pair = (Map.Entry)var2.next();
         buffer.writeInt((Integer)pair.getKey());
         buffer.writeShort(((EnumPokedexRegisterStatus)pair.getValue()).ordinal());
      }

      buffer.writeInt(this.data2.cellSet().size());
      this.data2.cellSet().forEach((cell) -> {
         buffer.writeInt((Integer)cell.getRowKey());
         buffer.writeShort((Short)cell.getColumnKey());
         buffer.writeShort((Short)cell.getValue());
      });
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = new HashMap();
      int size = buffer.readInt();

      int size2;
      for(size2 = 0; size2 < size; ++size2) {
         this.data.put(buffer.readInt(), EnumPokedexRegisterStatus.get(buffer.readShort()));
      }

      this.data2 = HashBasedTable.create();
      size2 = buffer.readInt();

      for(int i = 0; i < size2; ++i) {
         this.data2.put(buffer.readInt(), buffer.readShort(), buffer.readShort());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ClientUpdatePokedex message, MessageContext ctx) {
         ClientStorageManager.pokedex.update(message.data, message.data2);
      }
   }
}
