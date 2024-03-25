package com.pixelmonmod.pixelmon.storage.playerData;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportPosition implements ISaveData {
   private byte dimension;
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;

   public BlockPos getPosition() {
      return new BlockPos(this.x, this.y, this.z);
   }

   public int getDimension() {
      return this.dimension;
   }

   public void writeToNBT(NBTTagCompound nbt) {
      if (this.dimension != 0) {
         nbt.func_74774_a("tpDimension", this.dimension);
      }

      nbt.func_74780_a("tpPosX", this.x);
      nbt.func_74780_a("tpPosY", this.y);
      nbt.func_74780_a("tpPosZ", this.z);
      nbt.func_74776_a("tpRotY", this.yaw);
      nbt.func_74776_a("tpRotP", this.pitch);
   }

   public void readFromNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("tpDimension")) {
         this.dimension = nbt.func_74771_c("tpDimension");
      } else {
         this.dimension = 0;
      }

      this.x = nbt.func_74769_h("tpPosX");
      this.y = nbt.func_74769_h("tpPosY");
      this.z = nbt.func_74769_h("tpPosZ");
      this.yaw = nbt.func_74760_g("tpRotY");
      this.pitch = nbt.func_74760_g("tpRotP");
   }

   public void store(int dimension, double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
      this.dimension = (byte)dimension;
      this.x = posX;
      this.y = posY;
      this.z = posZ;
      this.yaw = rotationYaw;
      this.pitch = rotationPitch;
   }

   public void teleport(EntityPlayerMP player) {
      if (this.x != 0.0 && this.y != 0.0 && this.z != 0.0) {
         player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187534_aX, SoundCategory.NEUTRAL, 1.0F, 1.0F);
         MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
         if (player.func_71121_q().field_73011_w.getDimension() != this.dimension) {
            server.func_184103_al().transferPlayerToDimension(player, this.dimension, new UltraSpaceTeleporter(server.func_71218_a(this.dimension)));
         }

         player.field_71135_a.func_147364_a(this.x, this.y, this.z, this.yaw, this.pitch);
         player.field_70170_p.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187534_aX, SoundCategory.NEUTRAL, 1.0F, 1.0F);
      }

   }
}
