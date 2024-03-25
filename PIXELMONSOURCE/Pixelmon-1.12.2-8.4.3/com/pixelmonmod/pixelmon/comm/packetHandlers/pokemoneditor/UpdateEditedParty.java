package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class UpdateEditedParty implements IMessage {
   public List party = new ArrayList(6);

   protected UpdateEditedParty() {
   }

   protected UpdateEditedParty(List party) {
      Iterator var2 = party.iterator();

      while(var2.hasNext()) {
         Pokemon data = (Pokemon)var2.next();
         this.party.add(data == null ? null : this.createPokemonPacket(data));
      }

   }

   protected abstract UpdateEditedPokemon createPokemonPacket(Pokemon var1);

   public void toBytes(ByteBuf buf) {
      Iterator var2 = this.party.iterator();

      while(var2.hasNext()) {
         UpdateEditedPokemon data = (UpdateEditedPokemon)var2.next();
         if (data == null) {
            buf.writeBoolean(false);
         } else {
            buf.writeBoolean(true);
            data.toBytes(buf);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      for(int i = 0; i < 6; ++i) {
         if (buf.readBoolean()) {
            this.party.add(this.readPokemonData(buf));
         }
      }

   }

   protected abstract UpdateEditedPokemon readPokemonData(ByteBuf var1);
}
