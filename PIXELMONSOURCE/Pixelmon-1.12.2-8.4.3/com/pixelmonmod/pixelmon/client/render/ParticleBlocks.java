package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.Pixelmon;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleBlocks extends Particle {
   private static Random random = new Random();

   public ParticleBlocks(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state) {
      super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      Item item = state.func_177230_c().func_180660_a(state, random, 0);
      if (item != null) {
         TextureAtlasSprite atlasSprite = Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178082_a(item);
         this.func_187117_a(atlasSprite);
      } else {
         Pixelmon.LOGGER.warn("Cannot find dropped item for " + state.func_177230_c().func_149739_a());
      }

      this.field_70545_g = state.func_177230_c().field_149763_I;
      this.field_70552_h = this.field_70553_i = this.field_70551_j = 0.6F;
      this.field_70544_f /= 2.0F;
   }

   public int func_70537_b() {
      return 1;
   }

   public void func_180434_a(BufferBuilder vertexbuffer, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      float f6 = ((float)this.field_94054_b + this.field_70548_b / 4.0F) / 16.0F;
      float f7 = f6 + 0.015609375F;
      float f8 = ((float)this.field_94055_c + this.field_70549_c / 4.0F) / 16.0F;
      float f9 = f8 + 0.015609375F;
      float f10 = 0.1F * this.field_70544_f;
      if (this.field_187119_C != null) {
         f6 = this.field_187119_C.func_94214_a((double)(this.field_70548_b / 4.0F * 16.0F));
         f7 = this.field_187119_C.func_94214_a((double)((this.field_70548_b + 1.0F) / 4.0F * 16.0F));
         f8 = this.field_187119_C.func_94207_b((double)(this.field_70549_c / 4.0F * 16.0F));
         f9 = this.field_187119_C.func_94207_b((double)((this.field_70549_c + 1.0F) / 4.0F * 16.0F));
      }

      float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - field_70556_an);
      float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - field_70554_ao);
      float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - field_70555_ap);
      int i = this.func_189214_a(partialTicks);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      vertexbuffer.func_181662_b((double)(f11 - p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      vertexbuffer.func_181662_b((double)(f11 - p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      vertexbuffer.func_181662_b((double)(f11 + p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
      vertexbuffer.func_181662_b((double)(f11 + p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
   }
}
