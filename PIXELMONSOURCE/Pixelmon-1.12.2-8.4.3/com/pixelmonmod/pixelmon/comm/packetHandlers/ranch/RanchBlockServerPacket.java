package com.pixelmonmod.pixelmon.comm.packetHandlers.ranch;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityRanchBlock;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.items.ItemRanchUpgrade;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RanchBlockServerPacket implements IMessage {
   private EnumRanchServerPacketMode mode;
   private BlockPos ranchPosition;
   private UUID pokemonUUID;
   private StoragePosition pokemonPosition;
   private boolean[] extendDirection;

   public RanchBlockServerPacket() {
   }

   public RanchBlockServerPacket(BlockPos ranchPosition, EnumRanchServerPacketMode mode) {
      this.ranchPosition = ranchPosition;
      this.mode = mode;
   }

   public RanchBlockServerPacket(BlockPos ranchPosition, UUID pokemonUUID, StoragePosition pokemonPosition, EnumRanchServerPacketMode mode) {
      this(ranchPosition, mode);
      this.pokemonUUID = pokemonUUID;
      this.pokemonPosition = pokemonPosition;
   }

   public RanchBlockServerPacket(BlockPos ranchPosition, EnumRanchServerPacketMode mode, boolean[] extendDirection) {
      this(ranchPosition, mode);
      this.extendDirection = extendDirection;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.ranchPosition.func_177958_n());
      buf.writeInt(this.ranchPosition.func_177956_o());
      buf.writeInt(this.ranchPosition.func_177952_p());
      buf.writeShort(this.mode.ordinal());
      switch (this.mode) {
         case RemovePokemon:
         case AddPokemon:
            buf.writeLong(this.pokemonUUID.getMostSignificantBits()).writeLong(this.pokemonUUID.getLeastSignificantBits());
            this.pokemonPosition.encode(buf);
            break;
         case ExtendRanch:
            buf.writeBoolean(this.extendDirection[0]);
            buf.writeBoolean(this.extendDirection[1]);
            buf.writeBoolean(this.extendDirection[2]);
            buf.writeBoolean(this.extendDirection[3]);
         case CollectEgg:
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.ranchPosition = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.mode = EnumRanchServerPacketMode.values()[buf.readShort()];
      switch (this.mode) {
         case RemovePokemon:
         case AddPokemon:
            this.pokemonUUID = new UUID(buf.readLong(), buf.readLong());
            this.pokemonPosition = StoragePosition.decode(buf);
            break;
         case ExtendRanch:
            this.extendDirection = new boolean[]{buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean()};
         case CollectEgg:
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RanchBlockServerPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (BlockHelper.validateReach(player, message.ranchPosition)) {
            TileEntityRanchBlock ranch = (TileEntityRanchBlock)player.field_70170_p.func_175625_s(message.ranchPosition);
            if (ranch != null) {
               if (player.func_110124_au().equals(ranch.getOwnerUUID())) {
                  switch (message.mode) {
                     case RemovePokemon:
                        ranch.removePokemon(player, message.pokemonUUID, message.pokemonPosition);
                        break;
                     case AddPokemon:
                        ranch.addPokemon(player, message.pokemonUUID, message.pokemonPosition);
                        break;
                     case ExtendRanch:
                        if (player.func_184812_l_() || player.func_184614_ca().func_77973_b() instanceof ItemRanchUpgrade) {
                           if (!player.func_184812_l_()) {
                              player.func_184614_ca().func_190918_g(1);
                           }

                           ranch.getBounds().extend(player, message.extendDirection[0] ? 1 : 0, message.extendDirection[1] ? 1 : 0, message.extendDirection[2] ? 1 : 0, message.extendDirection[3] ? 1 : 0);
                        }
                        break;
                     case CollectEgg:
                        if (ranch.claimEgg(player)) {
                           player.func_71053_j();
                        }

                        return;
                  }

                  Pixelmon.network.sendTo(new RanchBlockClientPacket(ranch, EnumRanchClientPacketMode.ViewBlock), player);
               }
            }
         }
      }
   }
}
