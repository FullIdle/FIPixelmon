package com.pixelmonmod.pixelmon.client.render.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

public class ItemRendererShrineOrb extends PerspectiveMapWrapper {
   public static ModelResourceLocation uno = new ModelResourceLocation("pixelmon:uno_orb", "inventory");
   public static ModelResourceLocation dos = new ModelResourceLocation("pixelmon:dos_orb", "inventory");
   public static ModelResourceLocation tres = new ModelResourceLocation("pixelmon:tres_orb", "inventory");
   IBakedModel model;
   private float percent;
   private float height;
   Item orbType;

   public ItemRendererShrineOrb(IBakedModel model) {
      super(model, TRSRTransformation.identity());
      this.model = model;
   }

   public boolean func_188618_c() {
      return false;
   }

   public TextureAtlasSprite func_177554_e() {
      return this.model.func_177554_e();
   }

   public boolean func_177556_c() {
      return this.model.func_177556_c();
   }

   public ItemCameraTransforms func_177552_f() {
      return this.model.func_177552_f();
   }

   public ItemOverrideList func_188617_f() {
      return new OverrideList();
   }

   public boolean func_177555_b() {
      return this.model.func_177555_b();
   }

   public List func_188616_a(IBlockState state, EnumFacing side, long rand) {
      List quadList = new ArrayList(6);
      TextureAtlasSprite orbTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:items/back");
      this.addQuad(quadList, orbTexture, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 16.0F, 16.0F);
      if (this.orbType == PixelmonItems.unoOrb) {
         orbTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:items/unoorb");
      } else if (this.orbType == PixelmonItems.dosOrb) {
         orbTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:items/dosorb");
      } else if (this.orbType == PixelmonItems.tresOrb) {
         orbTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:items/tresorb");
      }

      this.addQuad(quadList, orbTexture, 1.0F, this.height, 0.001F, 0.0F, 16.0F - this.percent, 16.0F, 16.0F);
      orbTexture = Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:items/front");
      this.addQuad(quadList, orbTexture, 1.0F, 1.0F, 0.002F, 0.0F, 0.0F, 16.0F, 16.0F);
      return quadList;
   }

   private void addQuad(List quadList, TextureAtlasSprite texture, float xEnd, float yEnd, float zEnd, float uStart, float vStart, float uEnd, float vEnd) {
      int x = Float.floatToRawIntBits(xEnd);
      int y = Float.floatToRawIntBits(yEnd);
      int z = Float.floatToRawIntBits(zEnd);
      int us = Float.floatToRawIntBits(texture.func_94214_a((double)uStart));
      int ue = Float.floatToRawIntBits(texture.func_94214_a((double)uEnd));
      int vs = Float.floatToIntBits(texture.func_94207_b((double)vStart));
      int ve = Float.floatToIntBits(texture.func_94207_b((double)vEnd));
      int white = Color.white.getRGB();
      BakedQuad orbQuad = new BakedQuad(new int[]{x, 0, z, white, ue, ve, 0, x, y, z, white, ue, vs, 0, 0, y, z, white, us, vs, 0, 0, 0, z, white, us, ve, 0}, 0, EnumFacing.SOUTH, texture, false, DefaultVertexFormats.field_176599_b);
      quadList.add(orbQuad);
      int zn = Float.floatToRawIntBits(-zEnd);
      orbQuad = new BakedQuad(new int[]{0, 0, zn, white, ue, ve, 0, 0, y, zn, white, ue, vs, 0, x, y, zn, white, us, vs, 0, x, 0, zn, white, us, ve, 0}, 0, EnumFacing.NORTH, texture, false, DefaultVertexFormats.field_176599_b);
      quadList.add(orbQuad);
   }

   public Pair handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
      ImmutableMap map = PerspectiveMapWrapper.getTransforms(TRSRTransformation.identity());
      return handlePerspective(this, map, cameraTransformType);
   }

   private class OverrideList extends ItemOverrideList {
      public OverrideList() {
         super(Lists.newArrayList());
      }

      public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
         if (originalModel instanceof ItemRendererShrineOrb) {
            ItemRendererShrineOrb orb = (ItemRendererShrineOrb)originalModel;
            int pokemonKilled = stack.func_77952_i();
            orb.height = (float)pokemonKilled / 375.0F;
            orb.percent = orb.height * 16.0F;
            orb.orbType = stack.func_77973_b();
         }

         return originalModel;
      }
   }
}
