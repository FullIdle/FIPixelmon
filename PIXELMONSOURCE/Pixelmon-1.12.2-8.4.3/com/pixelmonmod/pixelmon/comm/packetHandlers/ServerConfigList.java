package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.pokemon.SpecFlag;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerConfigList implements IMessage {
   int computerBoxCount;
   public boolean allowCapturingOutsideBattle;
   public int maxLevel;
   public float ridingSpeedMultiplier;
   public boolean afkHandlerOn;
   public int afkTimerActivateSeconds;
   public boolean renderWildLevels;
   public boolean allowShinyCharmFromPokedex;
   public boolean allowExternalMoves;
   public boolean limitShopKeeperStackSize;
   public boolean universalTMs;
   public boolean superUniversalTMs;
   public ArrayList flags = new ArrayList();

   public ServerConfigList() {
      this.computerBoxCount = PixelmonConfig.computerBoxes;
      this.allowCapturingOutsideBattle = PixelmonConfig.allowCapturingOutsideBattle;
      this.renderWildLevels = PixelmonConfig.renderWildLevels;
      this.maxLevel = PixelmonConfig.maxLevel;
      this.ridingSpeedMultiplier = PixelmonConfig.ridingSpeedMultiplier;
      this.afkHandlerOn = PixelmonConfig.afkHandlerOn;
      this.afkTimerActivateSeconds = PixelmonConfig.afkTimerActivateSeconds;
      this.allowShinyCharmFromPokedex = PixelmonConfig.allowShinyCharmFromPokedex;
      this.allowExternalMoves = PixelmonConfig.allowExternalMoves;
      this.limitShopKeeperStackSize = PixelmonConfig.limitShopKeeperStackSize;
      this.universalTMs = PixelmonConfig.universalTMs;
      this.superUniversalTMs = PixelmonConfig.superUniversalTMs;
      if (PokemonSpec.extraSpecTypes != null) {
         Iterator var1 = PokemonSpec.extraSpecTypes.iterator();

         while(var1.hasNext()) {
            ISpecType specType = (ISpecType)var1.next();
            if (specType instanceof SpecFlag) {
               SpecFlag flag = (SpecFlag)specType;
               this.flags.add(flag);
            }
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.computerBoxCount = buf.readInt();
      this.allowCapturingOutsideBattle = buf.readBoolean();
      this.renderWildLevels = buf.readBoolean();
      this.maxLevel = buf.readInt();
      this.ridingSpeedMultiplier = buf.readFloat();
      this.afkHandlerOn = buf.readBoolean();
      this.afkTimerActivateSeconds = buf.readInt();
      this.allowShinyCharmFromPokedex = buf.readBoolean();
      this.allowExternalMoves = buf.readBoolean();
      this.limitShopKeeperStackSize = buf.readBoolean();
      this.universalTMs = buf.readBoolean();
      this.superUniversalTMs = buf.readBoolean();
      int n = buf.readUnsignedByte();

      for(int i = 0; i < n; ++i) {
         int m = buf.readUnsignedByte();
         ArrayList aliases = new ArrayList();

         for(int j = 0; j < m; ++j) {
            aliases.add(ByteBufUtils.readUTF8String(buf));
         }

         this.flags.add(new SpecFlag((String[])aliases.toArray(new String[m])));
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.computerBoxCount);
      buf.writeBoolean(this.allowCapturingOutsideBattle);
      buf.writeBoolean(this.renderWildLevels);
      buf.writeInt(this.maxLevel);
      buf.writeFloat(this.ridingSpeedMultiplier);
      buf.writeBoolean(this.afkHandlerOn);
      buf.writeInt(this.afkTimerActivateSeconds);
      buf.writeBoolean(this.allowShinyCharmFromPokedex);
      buf.writeBoolean(this.allowExternalMoves);
      buf.writeBoolean(this.limitShopKeeperStackSize);
      buf.writeBoolean(this.universalTMs);
      buf.writeBoolean(this.superUniversalTMs);
      buf.writeByte(this.flags.size());
      Iterator var2 = this.flags.iterator();

      while(var2.hasNext()) {
         SpecFlag flag = (SpecFlag)var2.next();
         buf.writeByte(flag.aliases.size());
         Iterator var4 = flag.aliases.iterator();

         while(var4.hasNext()) {
            String alias = (String)var4.next();
            ByteBufUtils.writeUTF8String(buf, alias);
         }
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerConfigList message, MessageContext ctx) {
         PixelmonServerConfig.updateFromServer(message);
         ClientStorageManager.load();
         GuiPixelmonOverlay.hideSpectateMessage((UUID)null);
         Iterator var3 = message.flags.iterator();

         while(var3.hasNext()) {
            SpecFlag flag = (SpecFlag)var3.next();
            if (PokemonSpec.getSpecForKey(flag.key) == null) {
               PokemonSpec.extraSpecTypes.add(flag);
            }
         }

      }
   }
}
