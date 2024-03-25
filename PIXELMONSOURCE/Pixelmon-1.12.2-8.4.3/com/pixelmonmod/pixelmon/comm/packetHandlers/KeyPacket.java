package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.KeyEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityOccupiedPokeball;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyPacket implements IMessage {
   int selectedPixelmon;
   int entityID;
   int moveIndex;
   int x;
   int y;
   int z;
   int side;
   EnumKeyPacketMode mode;
   static long lastThrownTime = -1L;

   public KeyPacket() {
   }

   public KeyPacket(int selectedPixelmon) {
      this.selectedPixelmon = selectedPixelmon;
      this.mode = EnumKeyPacketMode.SendPokemon;
   }

   public KeyPacket(int selectedPixelmon, int entityId, EnumKeyPacketMode mode) {
      this.selectedPixelmon = selectedPixelmon;
      this.entityID = entityId;
      this.mode = mode;
   }

   public KeyPacket(int selectedPixelmon, int moveIndex, int entityId) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveIndex = moveIndex;
      this.entityID = entityId;
      this.mode = EnumKeyPacketMode.ExternalMoveEntity;
   }

   public KeyPacket(int selectedPixelmon, String moveName, int entityId) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveIndex = this.moveIndex;
      this.entityID = entityId;
      this.mode = EnumKeyPacketMode.ExternalMoveEntity;
   }

   public KeyPacket(int selectedPixelmon, int moveIndex, BlockPos pos, EnumFacing side) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveIndex = moveIndex;
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.mode = EnumKeyPacketMode.ExternalMoveBlock;
      this.side = side.func_176745_a();
   }

   public KeyPacket(int selectedPixelmon, String moveName, BlockPos pos, EnumFacing side) {
      this.selectedPixelmon = selectedPixelmon;
      this.moveIndex = this.moveIndex;
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.mode = EnumKeyPacketMode.ExternalMoveBlock;
      this.side = side.func_176745_a();
   }

   public void fromBytes(ByteBuf buffer) {
      this.mode = EnumKeyPacketMode.getFromOrdinal(buffer.readByte());
      this.selectedPixelmon = buffer.readByte();
      switch (this.mode) {
         case ExternalMove:
         case ActionKeyEntity:
            this.entityID = buffer.readInt();
            break;
         case ExternalMoveEntity:
            this.entityID = buffer.readInt();
            this.moveIndex = buffer.readInt();
            break;
         case ExternalMoveBlock:
            this.moveIndex = buffer.readInt();
            this.x = buffer.readInt();
            this.y = buffer.readInt();
            this.z = buffer.readInt();
            this.side = buffer.readInt();
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.mode.ordinal());
      buffer.writeByte(this.selectedPixelmon);
      switch (this.mode) {
         case ExternalMove:
         case ActionKeyEntity:
            buffer.writeInt(this.entityID);
            break;
         case ExternalMoveEntity:
            buffer.writeInt(this.entityID);
            buffer.writeInt(this.moveIndex);
            break;
         case ExternalMoveBlock:
            buffer.writeInt(this.moveIndex);
            buffer.writeInt(this.x);
            buffer.writeInt(this.y);
            buffer.writeInt(this.z);
            buffer.writeInt(this.side);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(KeyPacket message, MessageContext ctx) {
         EnumKeyPacketMode mode = message.mode;
         KeyEvent event = new KeyEvent(ctx.getServerHandler().field_147369_b, mode);
         if (Pixelmon.EVENT_BUS.post(event)) {
            return null;
         } else if (mode == EnumKeyPacketMode.SendPokemon) {
            this.sendPokemon(message, ctx);
            return null;
         } else {
            ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
               World world = ctx.getServerHandler().field_147369_b.field_70170_p;
               EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
               PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
               Pokemon pokemon = storage.get(message.selectedPixelmon);
               if (pokemon != null && pokemon.getPixelmonIfExists() != null) {
                  EntityPixelmon pixelmon = pokemon.getPixelmonIfExists();
                  if (pixelmon != null) {
                     if (mode == EnumKeyPacketMode.ActionKeyEntity) {
                        EntityLivingBase entity = (EntityLivingBase)world.func_73045_a(message.entityID);
                        pixelmon.func_70624_b(entity);
                     }

                     if (mode.isAction()) {
                        pixelmon.update(new EnumUpdateType[]{EnumUpdateType.Target});
                     }
                  }

               }
            });
            return null;
         }
      }

      private void sendPokemon(KeyPacket message, MessageContext ctx) {
         if (Pixelmon.canSendOutPokemon) {
            ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
               EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
               PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
               Pokemon pokemon = storage.get(message.selectedPixelmon);
               if (player.func_175149_v()) {
                  ChatHandler.sendChat(player, "sendpixelmon.isspectator");
               } else if (pokemon != null && !pokemon.isEgg()) {
                  String nickname = pokemon.getDisplayName();
                  if (!Pixelmon.EVENT_BUS.post(new PixelmonSendOutEvent(player, pokemon))) {
                     if (pokemon.getHealth() <= 0) {
                        ChatHandler.sendChat(player, "sendpixelmon.cantbattle", new TextComponentTranslation(nickname, new Object[0]));
                     } else if (pokemon.getPixelmonIfExists() != null) {
                        EntityPixelmon pixelmon = pokemon.getPixelmonIfExists();
                        if (pixelmon.func_184179_bs() == player) {
                           player.func_184210_p();
                        }

                        if (pixelmon.func_70902_q() == null) {
                           pixelmon.unloadEntity();
                        } else if (pixelmon.func_70902_q() == player) {
                           pixelmon.retrieve();
                           ChatHandler.sendChat(player, "sendpixelmon.retrieved", new TextComponentTranslation(nickname, new Object[0]));
                        }
                     } else {
                        long worldTime = player.field_70170_p.func_72820_D();
                        if (KeyPacket.lastThrownTime - worldTime < 300L && storage.transientData.thrownPokeball != null && !storage.transientData.thrownPokeball.field_70128_L) {
                           return;
                        }

                        EnumPokeballs caughtBall = pokemon.getCaughtBall();
                        storage.transientData.thrownPokeball = new EntityOccupiedPokeball(player.field_70170_p, player, message.selectedPixelmon, caughtBall);
                        if (player.field_70170_p.func_72838_d(storage.transientData.thrownPokeball)) {
                           player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187737_v, SoundCategory.NEUTRAL, 0.5F, 0.4F / (player.field_70170_p.field_73012_v.nextFloat() * 0.4F + 0.8F));
                           ChatHandler.sendChat(player, "sendpixelmon.sentout", new TextComponentTranslation(nickname, new Object[0]));
                           KeyPacket.lastThrownTime = worldTime;
                        } else {
                           storage.transientData.thrownPokeball = null;
                        }
                     }

                  }
               }
            });
         }
      }
   }
}
