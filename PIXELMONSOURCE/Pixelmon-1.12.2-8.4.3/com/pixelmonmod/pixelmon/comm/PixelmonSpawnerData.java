package com.pixelmonmod.pixelmon.comm;

import com.pixelmonmod.pixelmon.blocks.spawning.TileEntityPixelmonSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PixelmonSpawnerData {
   int x;
   int y;
   int z;
   NBTTagCompound nbt;

   public PixelmonSpawnerData(BlockPos pos, NBTTagCompound nbt) {
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.nbt = nbt;
   }

   public PixelmonSpawnerData() {
   }

   public void writePacketData(ByteBuf data) {
      data.writeInt(this.x);
      data.writeInt(this.y);
      data.writeInt(this.z);
      ByteBufUtils.writeTag(data, this.nbt);
   }

   public void readPacketData(ByteBuf data) {
      this.x = data.readInt();
      this.y = data.readInt();
      this.z = data.readInt();
      this.nbt = ByteBufUtils.readTag(data);
   }

   public void updateTileEntity(World world) {
      if (world.func_73046_m().func_152345_ab()) {
         BlockPos pos = new BlockPos(this.x, this.y, this.z);
         if (world.func_175625_s(pos) instanceof TileEntityPixelmonSpawner) {
            TileEntityPixelmonSpawner t = (TileEntityPixelmonSpawner)world.func_175625_s(pos);
            if (t == null) {
               return;
            }

            t.func_145839_a(this.nbt);
            t.finishEdit();
         }
      } else {
         world.func_73046_m().func_152344_a(() -> {
            this.updateTileEntity(world);
         });
      }

   }
}
