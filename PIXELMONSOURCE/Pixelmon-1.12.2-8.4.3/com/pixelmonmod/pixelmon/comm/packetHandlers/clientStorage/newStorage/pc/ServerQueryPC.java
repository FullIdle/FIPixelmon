package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.StringUtils;

public class ServerQueryPC implements IMessage {
   private int searchId;
   private String query;

   public ServerQueryPC() {
   }

   public ServerQueryPC(int searchId, String query) {
      this.searchId = searchId;
      this.query = query;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.searchId);
      ByteBufUtils.writeUTF8String(buf, this.query);
   }

   public void fromBytes(ByteBuf buf) {
      this.searchId = buf.readShort();
      this.query = ByteBufUtils.readUTF8String(buf);
   }

   public static List search(PokemonStorage storage, String query) {
      String name;
      Object list;
      if (query.contains(" ")) {
         name = query.substring(0, query.indexOf(" "));
         PokemonSpec spec = new PokemonSpec(query.substring(query.indexOf(" ") + 1));
         if (spec.name != null) {
            return new ArrayList();
         }

         if (!"eggs".contains(name)) {
            spec.egg = false;
         }

         list = storage.findAll(spec);
      } else if (query.equals("eggs")) {
         name = "all";
         list = storage.findAll(Pokemon::isEgg);
      } else {
         name = query;
         list = new ArrayList(Arrays.asList(storage.getAll()));
         ((List)list).removeIf(Objects::isNull);
      }

      if (!name.equals("all")) {
         ((List)list).removeIf((poke) -> {
            return poke.isEgg() && !"eggs".contains(name) || !poke.getDisplayName().toLowerCase().contains(name) && !poke.getSpecies().name.toLowerCase().contains(name) && !poke.getSpecies().name.contains(name);
         });
      }

      return (List)list;
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerQueryPC message, MessageContext ctx) {
         String query = message.query.replaceAll(" +", " ").toLowerCase();
         if (!StringUtils.isBlank(query)) {
            PCStorage pc = Pixelmon.storageManager.getPCForPlayer(ctx.getServerHandler().field_147369_b);
            List results = ServerQueryPC.search(pc, query);
            Pixelmon.network.sendTo(new ClientQueryResultsPC(message.searchId, results), ctx.getServerHandler().field_147369_b);
         }

      }
   }
}
