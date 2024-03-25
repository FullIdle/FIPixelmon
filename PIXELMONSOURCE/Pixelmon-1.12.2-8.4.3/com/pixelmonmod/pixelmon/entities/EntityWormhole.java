package com.pixelmonmod.pixelmon.entities;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import com.pixelmonmod.pixelmon.util.helpers.DimensionHelper;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWormhole extends Entity {
   private int maxAge;
   private int color;
   private int soundTimer;
   private int yaw;
   private int pitch;

   public EntityWormhole(World worldIn) {
      super(worldIn);
      this.maxAge = -1;
      this.color = 0;
      this.soundTimer = 0;
      this.yaw = 0;
      this.pitch = 0;
      this.maxAge = 800 + this.field_70146_Z.nextInt(1400);
      this.func_70105_a(1.0F, 1.0F);
      this.color = this.field_70146_Z.nextInt(6);
      this.yaw = this.field_70146_Z.nextInt(360);
      this.pitch = this.field_70146_Z.nextInt(20) - 10;
   }

   public EntityWormhole(World worldIn, int maxAge) {
      this(worldIn);
      this.maxAge = maxAge;
   }

   public EntityWormhole(World worldIn, double x, double y, double z, int maxAge) {
      this(worldIn, maxAge);
      this.func_70107_b(x, y + 7.0, z);
   }

   public EntityWormhole(World worldIn, double x, double y, double z, int maxAge, int color, int yaw, int pitch) {
      this(worldIn, maxAge);
      this.func_70107_b(x, y, z);
      this.color = Math.max(0, Math.min(color, 6));
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public void func_70107_b(double x, double y, double z) {
      this.field_70165_t = x;
      this.field_70163_u = y;
      this.field_70161_v = z;
      float f = this.field_70130_N / 2.0F;
      float f1 = this.field_70131_O;
      this.func_174826_a(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double distance) {
      return true;
   }

   protected void func_70037_a(NBTTagCompound compound) {
      if (compound.func_74764_b("MaxAge")) {
         this.maxAge = compound.func_74762_e("MaxAge");
      }

      if (compound.func_74764_b("posX")) {
         this.func_70107_b(compound.func_74769_h("posX"), compound.func_74769_h("posY"), compound.func_74769_h("posZ"));
      }

      if (compound.func_74764_b("color")) {
         this.color = compound.func_74762_e("color");
      }

      if (compound.func_74764_b("pitch")) {
         this.pitch = compound.func_74762_e("pitch");
      }

      if (compound.func_74764_b("yaw")) {
         this.yaw = compound.func_74762_e("yaw");
      }

   }

   protected void func_70014_b(NBTTagCompound compound) {
      compound.func_74768_a("MaxAge", this.maxAge);
      compound.func_74780_a("posX", this.field_70165_t);
      compound.func_74780_a("posY", this.field_70163_u);
      compound.func_74780_a("posZ", this.field_70161_v);
      compound.func_74768_a("color", this.color);
      compound.func_74768_a("pitch", this.pitch);
      compound.func_74768_a("yaw", this.yaw);
   }

   protected void func_70088_a() {
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
      this.func_70107_b(x, y, z);
      this.func_70101_b(yaw, pitch);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_184177_bl() {
      return TileEntity.INFINITE_EXTENT_AABB;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K) {
         if (this.maxAge != -1 && this.field_70173_aa > this.maxAge) {
            this.func_70106_y();
         }

         if (this.soundTimer <= 0) {
            this.field_70170_p.func_184133_a((EntityPlayer)null, this.func_180425_c(), PixelSounds.ultraWormhole, SoundCategory.BLOCKS, 0.5F, 1.0F);
            this.soundTimer = 380;
         } else {
            --this.soundTimer;
         }
      }

   }

   public int getColor() {
      return this.color;
   }

   public void func_70100_b_(EntityPlayer entityIn) {
      entityIn.field_70170_p.func_184133_a((EntityPlayer)null, entityIn.func_180425_c(), SoundEvents.field_187812_eh, SoundCategory.MASTER, 0.5F, 1.0F);
      if (!this.field_70170_p.field_72995_K && this.func_184102_h() != null && entityIn instanceof EntityPlayerMP && PixelmonConfig.ultraSpace) {
         EntityPlayerMP player = (EntityPlayerMP)entityIn;
         if (this.field_70170_p.field_73011_w.getDimension() != UltraSpace.DIM_ID) {
            if (!DimensionManager.isDimensionRegistered(UltraSpace.DIM_ID)) {
               this.func_70106_y();
               return;
            }

            player.func_184210_p();
            double[] destination = new double[]{this.field_70165_t, this.field_70163_u, this.field_70161_v};
            wrapIntoWorldBorder(destination, UltraSpace.DIM_ID);
            player.getEntityData().func_74780_a("PortalX", player.field_70165_t);
            player.getEntityData().func_74780_a("PortalY", player.field_70163_u);
            player.getEntityData().func_74780_a("PortalZ", player.field_70161_v);
            player.getEntityData().func_74768_a("PortalD", player.field_71093_bK);
            DimensionHelper.teleport(player, UltraSpace.DIM_ID, destination[0], this.field_70163_u, destination[2]);
         } else {
            double x;
            double y;
            double z;
            int d;
            if (player.getEntityData().func_74764_b("PortalX") && player.getEntityData().func_74764_b("PortalY") && player.getEntityData().func_74764_b("PortalZ") && player.getEntityData().func_74764_b("PortalD")) {
               x = player.getEntityData().func_74769_h("PortalX");
               y = player.getEntityData().func_74769_h("PortalY");
               z = player.getEntityData().func_74769_h("PortalZ");
               d = player.getEntityData().func_74762_e("PortalD");
            } else {
               x = this.field_70165_t;
               y = this.field_70163_u;
               z = this.field_70161_v;
               d = player.getSpawnDimension();
            }

            double[] destination = new double[]{x, y, z};
            wrapIntoWorldBorder(destination, d);
            DimensionHelper.teleport(player, d, destination[0], y, destination[1]);
         }

         this.func_70106_y();
      }

   }

   public static void wrapIntoWorldBorder(double[] pos, int dimension) {
      WorldServer world = DimensionManager.getWorld(dimension);
      if (world != null && world.func_175723_af() != null && !world.func_175723_af().func_177746_a(new BlockPos(pos[0], pos[1], pos[2]))) {
         WorldBorder border = world.func_175723_af();
         pos[0] = wrapOrdinate(pos[0], border.func_177731_f() - border.func_177741_h() / 2.0, border.func_177731_f() + border.func_177741_h() / 2.0);
         pos[2] = wrapOrdinate(pos[2], border.func_177721_g() - border.func_177741_h() / 2.0, border.func_177721_g() + border.func_177741_h() / 2.0);
      }
   }

   private static double wrapOrdinate(double ordinate, double min, double max) {
      if (ordinate > min && ordinate < max) {
         return ordinate;
      } else if (ordinate == min) {
         return ordinate + 2.0;
      } else if (ordinate == max) {
         return ordinate - 2.0;
      } else {
         double dist = ordinate < min ? min - ordinate : ordinate - max;
         int oscillations = (int)Math.floor(dist / (max - min));
         if (ordinate < 0.0) {
            ++oscillations;
         }

         double remainder = dist % (max - min);
         if (oscillations % 2 == 0) {
            ordinate = max - remainder;
         } else {
            ordinate = min + remainder;
         }

         return ordinate;
      }
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public boolean func_70075_an() {
      return false;
   }

   public float func_70047_e() {
      return 0.0F;
   }

   public int getPitch() {
      return this.pitch;
   }

   public int getYaw() {
      return this.yaw;
   }
}
