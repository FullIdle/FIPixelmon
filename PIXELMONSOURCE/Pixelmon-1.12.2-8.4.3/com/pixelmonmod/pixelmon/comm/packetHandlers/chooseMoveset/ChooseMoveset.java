package com.pixelmonmod.pixelmon.comm.packetHandlers.chooseMoveset;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChooseMoveset implements IMessage {
   public static final Map choosingMoveset = new ConcurrentHashMap();
   UUID uuid;
   ArrayList attackIds = new ArrayList(600);

   public ChooseMoveset() {
   }

   public ChooseMoveset(Pokemon pokemon, ArrayList chosenAttackList) {
      this.uuid = pokemon.getUUID();
      this.attackIds.addAll((Collection)chosenAttackList.stream().map((a) -> {
         return a.getActualMove().getAttackId();
      }).collect(Collectors.toList()));
   }

   public void fromBytes(ByteBuf buf) {
      this.uuid = new UUID(buf.readLong(), buf.readLong());
      int num = buf.readShort();

      for(int i = 0; i < num; ++i) {
         this.attackIds.add(buf.readInt());
      }

   }

   public void toBytes(ByteBuf buf) {
      PixelmonMethods.toBytesUUID(buf, this.uuid);
      buf.writeShort(this.attackIds.size());
      this.attackIds.forEach(buf::writeInt);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ChooseMoveset message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (player != null && ChooseMoveset.choosingMoveset.containsKey(player.func_110124_au())) {
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            Pokemon pokemon = party.find(message.uuid);
            pokemon.getMoveset().replaceWith(message.attackIds);
            ChoosingMovesetData d = (ChoosingMovesetData)ChooseMoveset.choosingMoveset.get(player.func_110124_au());
            if (d != null) {
               d.next();
               if (d.pokemonList.isEmpty()) {
                  ChooseMoveset.choosingMoveset.remove(player.func_110124_au());
               }
            }
         }

      }
   }
}
