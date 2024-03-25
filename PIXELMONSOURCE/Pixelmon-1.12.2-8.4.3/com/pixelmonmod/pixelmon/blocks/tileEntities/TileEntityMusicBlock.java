package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.blocks.BlockMusic;
import com.pixelmonmod.pixelmon.client.music.BattleMusic;
import com.pixelmonmod.pixelmon.client.music.VoidMusicTicker;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMusicBlock extends TileEntity implements ITickable {
   private ResourceLocation track = new ResourceLocation("pixelmon", "pixelmon.music.pokecenter");
   private BlockPos offset = new BlockPos(0, 0, 0);
   private BlockPos size = new BlockPos(1, 1, 1);
   @SideOnly(Side.CLIENT)
   private BlockSoundRecord soundRecord;

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         this.clientUpdate();
      }

   }

   @SideOnly(Side.CLIENT)
   private void clientUpdate() {
      if (this.field_145850_b.func_82737_E() % 8L == 1L) {
         AxisAlignedBB aabb = (new AxisAlignedBB(this.field_174879_c)).func_186670_a(this.offset);
         aabb = aabb.func_72321_a((double)this.size.func_177958_n(), (double)this.size.func_177956_o(), (double)this.size.func_177952_p());
         aabb = aabb.func_72321_a((double)(-this.size.func_177958_n()), (double)(-this.size.func_177956_o()), (double)(-this.size.func_177952_p()));
         if (this.soundRecord == null && this.track != null) {
            BlockPos pos = this.func_174877_v();
            this.soundRecord = new BlockSoundRecord(this.track, SoundCategory.MUSIC, 0, pos, aabb);
         }

         Minecraft mc = Minecraft.func_71410_x();
         if (aabb.func_72326_a(mc.field_71439_g.func_174813_aQ()) && !mc.func_147118_V().func_147692_c(this.soundRecord) && !BattleMusic.isPlaying()) {
            this.soundRecord = new BlockSoundRecord(this.track, SoundCategory.MUSIC, 0, this.field_174879_c, aabb);
            mc.func_147118_V().func_147682_a(this.soundRecord);
            VoidMusicTicker.replaceMusicTicker();
         }

      }
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_74764_b("track")) {
         this.track = new ResourceLocation(compound.func_74779_i("track"));
      }

      if (compound.func_74764_b("offsetX") || compound.func_74764_b("offsetY") || compound.func_74764_b("offsetZ")) {
         this.offset = new BlockPos(compound.func_74762_e("offsetX"), compound.func_74762_e("offsetY"), compound.func_74762_e("offsetZ"));
      }

      if (compound.func_74764_b("sizeX") || compound.func_74764_b("sizeY") || compound.func_74764_b("sizeZ")) {
         this.size = new BlockPos(compound.func_74762_e("sizeX"), compound.func_74762_e("sizeY"), compound.func_74762_e("sizeZ"));
      }

      if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
         this.field_145850_b.func_184138_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.field_145850_b.func_180495_p(this.field_174879_c), 3);
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      compound.func_74778_a("track", this.track.toString());
      if (this.offset.func_177986_g() != 0L) {
         compound.func_74768_a("offsetX", this.offset.func_177958_n());
         compound.func_74768_a("offsetY", this.offset.func_177956_o());
         compound.func_74768_a("offsetZ", this.offset.func_177952_p());
      }

      compound.func_74768_a("sizeX", this.size.func_177958_n());
      compound.func_74768_a("sizeY", this.size.func_177956_o());
      compound.func_74768_a("sizeZ", this.size.func_177952_p());
      return super.func_189515_b(compound);
   }

   public NBTTagCompound func_189517_E_() {
      return this.func_189515_b(new NBTTagCompound());
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.func_174877_v(), 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
   }

   public boolean hasFastRenderer() {
      return super.hasFastRenderer();
   }

   public static class BlockSoundRecord extends PositionedSoundRecord implements ITickableSound {
      BlockPos pos;
      AxisAlignedBB aabb;
      int endTick;

      public BlockSoundRecord(ResourceLocation soundId, SoundCategory categoryIn, int repeatDelayIn, BlockPos pos, AxisAlignedBB aabb) {
         super(soundId, categoryIn, 0.1F, 1.0F, true, repeatDelayIn, AttenuationType.NONE, (float)pos.func_177958_n() + 0.5F, (float)pos.func_177956_o() + 0.5F, (float)pos.func_177952_p() + 0.5F);
         this.pos = pos;
         this.aabb = aabb;
      }

      public void func_73660_a() {
         Minecraft mc = Minecraft.func_71410_x();
         if (this.aabb.func_72326_a(mc.field_71439_g.func_174813_aQ()) && mc.field_71441_e.func_180495_p(this.pos).func_177230_c() instanceof BlockMusic) {
            this.endTick = 0;
            if (this.field_147662_b != 1.0F) {
               this.field_147662_b = Math.min(1.0F, this.field_147662_b + 0.05F);
            }
         } else {
            ++this.endTick;
            this.field_147662_b = Math.max(0.0F, 1.0F - (float)this.endTick / 100.0F);
         }

      }

      public boolean func_147667_k() {
         if (this.endTick < 100 && !BattleMusic.isPlaying()) {
            return false;
         } else {
            if (!BattleMusic.isPlaying()) {
               VoidMusicTicker.restoreMusicTicker();
            }

            return true;
         }
      }
   }
}
