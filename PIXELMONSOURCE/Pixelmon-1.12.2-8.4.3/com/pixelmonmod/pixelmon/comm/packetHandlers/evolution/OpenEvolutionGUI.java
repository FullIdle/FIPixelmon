package com.pixelmonmod.pixelmon.comm.packetHandlers.evolution;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiEvolve;
import com.pixelmonmod.pixelmon.client.gui.GuiItemDrops;
import com.pixelmonmod.pixelmon.client.gui.GuiTrading;
import com.pixelmonmod.pixelmon.client.gui.battles.EvoInfo;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenEvolutionGUI implements IMessage {
   UUID pokemonUUID;
   String name;

   public OpenEvolutionGUI() {
   }

   public OpenEvolutionGUI(UUID pokemonUUID, String name) {
      this.pokemonUUID = pokemonUUID;
      this.name = name;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.name = ByteBufUtils.readUTF8String(buffer);
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      ByteBufUtils.writeUTF8String(buffer, this.name);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenEvolutionGUI message, MessageContext ctx) {
         this.checkEvolution(message);
      }

      @SideOnly(Side.CLIENT)
      private void checkEvolution(OpenEvolutionGUI message) {
         boolean exists = false;

         for(int i = 0; i < ClientProxy.battleManager.evolveList.size(); ++i) {
            if (((EvoInfo)ClientProxy.battleManager.evolveList.get(i)).pokemonUUID.equals(message.pokemonUUID)) {
               exists = true;
            }
         }

         if (!exists) {
            ClientProxy.battleManager.evolveList.add(new EvoInfo(message.pokemonUUID, message.name));
         }

         if (ClientProxy.battleManager.battleEnded && !(Minecraft.func_71410_x().field_71462_r instanceof GuiBattle) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiTrading) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiItemDrops)) {
            Minecraft.func_71410_x().func_147108_a(new GuiEvolve());
         }

      }
   }
}
