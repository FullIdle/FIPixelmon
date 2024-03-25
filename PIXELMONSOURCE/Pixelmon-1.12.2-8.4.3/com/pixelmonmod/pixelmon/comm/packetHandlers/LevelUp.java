package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LevelUp implements IMessage {
   public UUID pokemonUUID;
   public int level;
   public PixelmonStatsData statsLevel1;
   public PixelmonStatsData statsLevel2;
   public PixelmonInGui poke;

   public LevelUp() {
   }

   public LevelUp(UUID pokemonUUID, int level, PixelmonStatsData statsLevel1, PixelmonStatsData statsLevel2, PixelmonInGui poke) {
      this.pokemonUUID = pokemonUUID;
      this.level = level;
      this.statsLevel1 = statsLevel1;
      this.statsLevel2 = statsLevel2;
      this.poke = poke;
   }

   public void fromBytes(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.level = buffer.readInt();
      this.statsLevel1 = new PixelmonStatsData();
      this.statsLevel1.readPacketData(buffer);
      this.statsLevel2 = new PixelmonStatsData();
      this.statsLevel2.readPacketData(buffer);
      this.poke = new PixelmonInGui();
      this.poke.decodeFrom(buffer);
   }

   public void toBytes(ByteBuf buffer) {
      PixelmonMethods.toBytesUUID(buffer, this.pokemonUUID);
      buffer.writeInt(this.level);
      this.statsLevel1.writePacketData(buffer);
      this.statsLevel2.writePacketData(buffer);
      this.poke.encodeInto(buffer);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(LevelUp message, MessageContext ctx) {
         ClientProxy.battleManager.levelUpList.add(message);
         this.onClient(message);
      }

      @SideOnly(Side.CLIENT)
      private void onClient(LevelUp message) {
         if (!(Minecraft.func_71410_x().field_71462_r instanceof GuiBattle)) {
            if (ClientProxy.battleManager.fullOurPokemon == null) {
               ClientProxy.battleManager.fullOurPokemon = Lists.newArrayList();
            }

            ClientProxy.battleManager.fullOurPokemon.add(message.poke);
            Minecraft.func_71410_x().func_147108_a(new GuiBattle());
         }

      }
   }
}
