package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.DeoxysStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumDeoxys;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerExtrasPacket implements IMessage {
   private int slot;

   public PlayerExtrasPacket() {
   }

   private PlayerExtrasPacket(int slot) {
      this.slot = slot;
   }

   public static PlayerExtrasPacket getSetTexturePacket(int slot) {
      return new PlayerExtrasPacket(slot);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.slot);
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readByte();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(PlayerExtrasPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
         if (message.slot >= 1 && message.slot <= 6) {
            Pokemon pokemon = party.get(message.slot - 1);
            if (pokemon != null) {
               PixelExtrasData data = PixelExtrasStorage.getData(player.func_110124_au());
               if (pokemon.getSpecies() == EnumSpecies.Deoxys) {
                  if (pokemon.getFormEnum() == EnumDeoxys.Sus) {
                     pokemon.setForm(EnumDeoxys.Normal);
                     ((DeoxysStats)pokemon.getExtraStats(DeoxysStats.class)).setSus(false);
                  } else {
                     pokemon.setForm(EnumDeoxys.Sus);
                     ((DeoxysStats)pokemon.getExtraStats(DeoxysStats.class)).setSus(true);
                  }
               } else if (pokemon.getFormEnum() == EnumSpecial.Online) {
                  pokemon.setForm(EnumSpecial.Online.getBaseFromCosmetic(pokemon));
               } else if (data.canSeeTexture(pokemon.getSpecies())) {
                  pokemon.setForm(EnumSpecial.Online);
               }
            }

         }
      }
   }
}
