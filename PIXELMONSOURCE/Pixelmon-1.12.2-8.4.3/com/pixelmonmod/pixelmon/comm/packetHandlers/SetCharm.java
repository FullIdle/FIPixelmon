package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetCharm implements IMessage {
   private EnumCharms charm;
   private EnumFeatureState state;

   public SetCharm() {
   }

   public SetCharm(EnumCharms charm, EnumFeatureState state) {
      this.charm = charm;
      this.state = state;
   }

   public void fromBytes(ByteBuf buffer) {
      short i = buffer.readShort();
      short j = buffer.readShort();
      this.charm = i >= 0 && i < EnumCharms.values().length ? EnumCharms.values()[i] : null;
      this.state = j >= 0 && j < EnumFeatureState.values().length ? EnumFeatureState.values()[j] : null;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.charm.ordinal());
      buffer.writeShort(this.state.ordinal());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetCharm message, MessageContext ctx) {
         if (message.charm != null && message.state != null) {
            EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
            PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
            switch (message.charm) {
               case Shiny:
                  if (storage.getShinyCharm() != EnumFeatureState.Disabled) {
                     storage.setShinyCharm(message.state);
                  }
                  break;
               case Oval:
                  if (storage.getOvalCharm() != EnumFeatureState.Disabled) {
                     storage.setOvalCharm(message.state);
                  }
                  break;
               case Exp:
                  if (storage.getExpCharm() != EnumFeatureState.Disabled) {
                     storage.setExpCharm(message.state);
                  }
                  break;
               case Catching:
                  if (storage.getCatchingCharm() != EnumFeatureState.Disabled) {
                     storage.setCatchingCharm(message.state);
                  }
                  break;
               case Mark:
                  if (storage.getMarkCharm() != EnumFeatureState.Disabled) {
                     storage.setMarkCharm(message.state);
                  }
            }
         }

         return null;
      }
   }
}
