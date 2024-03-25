package com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SendSpawnData implements IMessage {
   List spawnData = Lists.newArrayList();
   int npn;

   public SendSpawnData() {
   }

   public SendSpawnData(int npn, List data) {
      this.npn = npn;
      this.spawnData = data;
   }

   public void toBytes(ByteBuf byteBuf) {
      byteBuf.writeInt(this.npn);
      byteBuf.writeInt(this.spawnData.size());
      Iterator var2 = this.spawnData.iterator();

      while(var2.hasNext()) {
         Pokedex.PokedexSpawnData data = (Pokedex.PokedexSpawnData)var2.next();
         data.encryptTo(byteBuf);
      }

   }

   public void fromBytes(ByteBuf byteBuf) {
      this.npn = byteBuf.readInt();
      int size = byteBuf.readInt();

      for(int i = 0; i < size; ++i) {
         this.spawnData.add(Pokedex.PokedexSpawnData.decryptFrom(byteBuf));
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SendSpawnData message, MessageContext ctx) {
         ClientStorageManager.pokedex.update(message.npn, message.spawnData);
      }
   }
}
