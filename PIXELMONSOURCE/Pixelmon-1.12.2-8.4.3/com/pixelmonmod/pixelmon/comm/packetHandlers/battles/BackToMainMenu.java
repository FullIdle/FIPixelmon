package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BackToMainMenu implements IMessage {
   boolean canSwitch;
   boolean canFlee;
   ArrayList pokemonToChoose;

   public BackToMainMenu() {
   }

   public BackToMainMenu(boolean canSwitch, boolean canFlee, ArrayList pokemonToChoose) {
      this.canSwitch = canSwitch;
      this.canFlee = canFlee;
      this.pokemonToChoose = new ArrayList();
      this.pokemonToChoose.addAll((Collection)pokemonToChoose.stream().map((p) -> {
         return p.getPokemonUUID();
      }).collect(Collectors.toList()));
   }

   public void fromBytes(ByteBuf buffer) {
      this.canSwitch = buffer.readBoolean();
      this.canFlee = buffer.readBoolean();
      int size = buffer.readShort();
      this.pokemonToChoose = new ArrayList();

      for(int i = 0; i < size; ++i) {
         this.pokemonToChoose.add(new UUID(buffer.readLong(), buffer.readLong()));
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.canSwitch);
      buffer.writeBoolean(this.canFlee);
      buffer.writeShort(this.pokemonToChoose.size());
      Iterator var2 = this.pokemonToChoose.iterator();

      while(var2.hasNext()) {
         UUID uuid = (UUID)var2.next();
         PixelmonMethods.toBytesUUID(buffer, uuid);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(BackToMainMenu message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientProxy.battleManager.startPicking(message.canSwitch, message.canFlee, message.pokemonToChoose);
         });
         return null;
      }
   }
}
