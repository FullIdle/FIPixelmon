package com.pixelmonmod.pixelmon.comm.packetHandlers.zygarde;

import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeAssembly;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ZygardeReassemblyPacket implements IMessage {
   BlockPos pos;
   TileEntityZygardeAssembly.Mode mode;
   StoragePosition position;
   int slot;

   public ZygardeReassemblyPacket() {
   }

   public ZygardeReassemblyPacket(BlockPos pos, TileEntityZygardeAssembly.Mode mode, StoragePosition position, int slot) {
      this.pos = pos;
      this.mode = mode;
      this.position = position;
      this.slot = slot;
   }

   public void fromBytes(ByteBuf buf) {
      this.pos = BlockPos.func_177969_a(buf.readLong());
      this.mode = TileEntityZygardeAssembly.Mode.fromOrdinal(buf.readByte());
      if (buf.readBoolean()) {
         this.position = StoragePosition.decode(buf);
      } else {
         this.position = null;
      }

      this.slot = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeLong(this.pos.func_177986_g());
      buf.writeByte(this.mode.ordinal());
      buf.writeBoolean(this.position != null);
      if (this.position != null) {
         this.position.encode(buf);
      }

      buf.writeInt(this.slot);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ZygardeReassemblyPacket message, MessageContext ctx) {
         World world = ctx.getServerHandler().field_147369_b.field_70170_p;
         TileEntityZygardeAssembly assembly = (TileEntityZygardeAssembly)BlockHelper.getTileEntity(TileEntityZygardeAssembly.class, world, message.pos);
         if (assembly != null) {
            assembly.onSelection(ctx.getServerHandler().field_147369_b, message.mode, message.position, message.slot);
         }

      }
   }
}
