package com.pixelmonmod.pixelmon.comm.packetHandlers.ranch;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.client.gui.ranchblock.GuiExtendRanch;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RanchBlockClientPacket implements IMessage {
   private EnumRanchClientPacketMode mode;
   private BlockPos position;
   private List pokemon;
   private EnumSpecies egg;
   private boolean[] extendDirections = new boolean[4];

   public RanchBlockClientPacket() {
   }

   public RanchBlockClientPacket(TileEntityRanchBlock ranch, EnumRanchClientPacketMode mode) {
      this.mode = mode;
      this.position = ranch.func_174877_v();
      if (mode == EnumRanchClientPacketMode.ViewBlock) {
         this.pokemon = ranch.getPokemonData();
         this.egg = ranch.getEggSpecies();
      } else if (mode == EnumRanchClientPacketMode.UpgradeBlock) {
         this.extendDirections[0] = ranch.getBounds().canExtend(1, 0, 0, 0);
         this.extendDirections[1] = ranch.getBounds().canExtend(0, 1, 0, 0);
         this.extendDirections[2] = ranch.getBounds().canExtend(0, 0, 1, 0);
         this.extendDirections[3] = ranch.getBounds().canExtend(0, 0, 0, 1);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.position.func_177958_n());
      buf.writeInt(this.position.func_177956_o());
      buf.writeInt(this.position.func_177952_p());
      buf.writeByte(this.mode.ordinal());
      if (this.mode == EnumRanchClientPacketMode.ViewBlock) {
         buf.writeByte(this.pokemon.size());
         Iterator var2 = this.pokemon.iterator();

         while(var2.hasNext()) {
            TileEntityRanchBlock.RanchPoke poke = (TileEntityRanchBlock.RanchPoke)var2.next();
            buf.writeLong(poke.uuid.getMostSignificantBits()).writeLong(poke.uuid.getLeastSignificantBits());
            poke.pos.encode(buf);
         }

         buf.writeBoolean(this.egg != null);
         if (this.egg != null) {
            buf.writeShort(this.egg.getNationalPokedexInteger());
         }
      } else if (this.mode == EnumRanchClientPacketMode.UpgradeBlock) {
         for(int i = 0; i < 4; ++i) {
            buf.writeBoolean(this.extendDirections[i]);
         }
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.position = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.mode = EnumRanchClientPacketMode.values()[buf.readByte()];
      int i;
      if (this.mode == EnumRanchClientPacketMode.ViewBlock) {
         this.pokemon = Lists.newArrayList();
         i = buf.readByte();

         for(int i = 0; i < i; ++i) {
            UUID uuid = new UUID(buf.readLong(), buf.readLong());
            StoragePosition position = StoragePosition.decode(buf);
            TileEntityRanchBlock.RanchPoke p = new TileEntityRanchBlock.RanchPoke(uuid, position);
            this.pokemon.add(p);
         }

         if (buf.readBoolean()) {
            this.egg = EnumSpecies.getFromDex(buf.readShort());
         }
      } else if (this.mode == EnumRanchClientPacketMode.UpgradeBlock) {
         for(i = 0; i < 4; ++i) {
            this.extendDirections[i] = buf.readBoolean();
         }
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RanchBlockClientPacket message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            TileEntityRanchBlock ranch = (TileEntityRanchBlock)Minecraft.func_71410_x().field_71439_g.field_70170_p.func_175625_s(message.position);
            Preconditions.checkArgument(ranch != null, "The server sent an invalid ranch tile");
            if (message.mode == EnumRanchClientPacketMode.ViewBlock) {
               ranch.setPokemonData(message.pokemon);
               ranch.setEggSpecies(message.egg);
            } else if (message.mode == EnumRanchClientPacketMode.UpgradeBlock) {
               GuiExtendRanch.extendDirections = message.extendDirections;
            }

         });
         return null;
      }
   }
}
