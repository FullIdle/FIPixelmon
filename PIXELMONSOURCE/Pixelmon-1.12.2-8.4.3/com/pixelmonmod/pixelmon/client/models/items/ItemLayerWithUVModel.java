package com.pixelmonmod.pixelmon.client.models.items;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public final class ItemLayerWithUVModel implements IModel {
   public static final ItemLayerWithUVModel INSTANCE = new ItemLayerWithUVModel(ImmutableList.of(), 16, 16, 0, 0);
   private static final EnumFacing[] HORIZONTALS;
   private static final EnumFacing[] VERTICALS;
   private final ImmutableList textures;
   private final ItemOverrideList overrides;
   private final int w;
   private final int h;
   private final int u;
   private final int v;

   public ItemLayerWithUVModel(ImmutableList textures, int w, int h, int u, int v) {
      this(textures, ItemOverrideList.field_188022_a, w, h, u, v);
   }

   public ItemLayerWithUVModel(ImmutableList textures, ItemOverrideList overrides, int w, int h, int u, int v) {
      this.textures = textures;
      this.overrides = overrides;
      this.w = w;
      this.h = h;
      this.u = u;
      this.v = v;
   }

   public ItemLayerWithUVModel(ModelBlock model, int x, int y, int u, int v) {
      this(getTextures(model), model.func_187967_g(), x, y, u, v);
   }

   private static ImmutableList getTextures(ModelBlock model) {
      ImmutableList.Builder builder = ImmutableList.builder();

      for(int i = 0; model.func_178300_b("layer" + i); ++i) {
         builder.add(new ResourceLocation(model.func_178308_c("layer" + i)));
      }

      return builder.build();
   }

   public Collection getTextures() {
      return this.textures;
   }

   public ItemLayerWithUVModel retexture(ImmutableMap textures, int w, int h, int u, int v) {
      ImmutableList.Builder builder = ImmutableList.builder();

      for(int i = 0; i < textures.size() + this.textures.size(); ++i) {
         if (textures.containsKey("layer" + i)) {
            builder.add(new ResourceLocation((String)textures.get("layer" + i)));
         } else if (i < this.textures.size()) {
            builder.add(this.textures.get(i));
         }
      }

      return new ItemLayerWithUVModel(builder.build(), this.overrides, w, h, u, v);
   }

   public IBakedModel bake(IModelState state, VertexFormat format, Function bakedTextureGetter) {
      ImmutableList.Builder builder = ImmutableList.builder();
      Optional transform = state.apply(Optional.empty());
      boolean identity = !transform.isPresent() || ((TRSRTransformation)transform.get()).isIdentity();

      for(int i = 0; i < this.textures.size(); ++i) {
         TextureAtlasSprite sprite = (TextureAtlasSprite)bakedTextureGetter.apply(this.textures.get(i));
         builder.addAll(getQuadsForSprite(i, sprite, format, this.w, this.h, this.u, this.v, transform));
      }

      TextureAtlasSprite particle = (TextureAtlasSprite)bakedTextureGetter.apply(this.textures.isEmpty() ? new ResourceLocation("missingno") : (ResourceLocation)this.textures.get(0));
      ImmutableMap map = PerspectiveMapWrapper.getTransforms(state);
      return new BakedItemModel(builder.build(), particle, map, this.overrides, identity);
   }

   public static ImmutableList getQuadsForSprite(int tint, TextureAtlasSprite sprite, VertexFormat format, int wIn, int hIn, int uIn, int vIn, Optional transform) {
      ImmutableList.Builder builder = ImmutableList.builder();
      int uMax = sprite.func_94211_a();
      int vMax = sprite.func_94216_b();
      float diffU = sprite.func_94212_f() - sprite.func_94209_e();
      float diffV = sprite.func_94210_h() - sprite.func_94206_g();
      float sW = (float)wIn / (float)sprite.func_94211_a();
      float sH = (float)hIn / (float)sprite.func_94216_b();
      float sU = (float)uIn / (float)sprite.func_94211_a();
      float sV = (float)vIn / (float)sprite.func_94216_b();
      FaceData faceData = new FaceData(uMax, vMax);
      boolean translucent = false;
      if (uIn == 0 && vIn == 0 && wIn == uMax && hIn == vMax) {
         int u;
         int vStart;
         int vEnd;
         boolean building;
         for(int f = 0; f < sprite.func_110970_k(); ++f) {
            int[] pixels = sprite.func_147965_a(f)[0];
            boolean[] ptv = new boolean[uMax];
            Arrays.fill(ptv, true);

            for(u = 0; u < vMax; ++u) {
               boolean ptu = true;

               for(vStart = 0; vStart < uMax; ++vStart) {
                  vEnd = getAlpha(pixels, uMax, vMax, vStart, u);
                  building = (float)vEnd / 255.0F <= 0.1F;
                  if (!building && vEnd < 255) {
                     translucent = true;
                  }

                  if (ptu && !building) {
                     faceData.set(EnumFacing.WEST, vStart, u);
                  }

                  if (!ptu && building) {
                     faceData.set(EnumFacing.EAST, vStart - 1, u);
                  }

                  if (ptv[vStart] && !building) {
                     faceData.set(EnumFacing.UP, vStart, u);
                  }

                  if (!ptv[vStart] && building) {
                     faceData.set(EnumFacing.DOWN, vStart, u - 1);
                  }

                  ptu = building;
                  ptv[vStart] = building;
               }

               if (!ptu) {
                  faceData.set(EnumFacing.EAST, uMax - 1, u);
               }
            }

            for(u = 0; u < uMax; ++u) {
               if (!ptv[u]) {
                  faceData.set(EnumFacing.DOWN, u, vMax - 1);
               }
            }
         }

         EnumFacing[] var30 = HORIZONTALS;
         int var31 = var30.length;

         int v;
         boolean face;
         int off;
         int var32;
         EnumFacing facing;
         for(var32 = 0; var32 < var31; ++var32) {
            facing = var30[var32];

            for(u = 0; u < vMax; ++u) {
               vStart = 0;
               vEnd = uMax;
               building = false;

               for(v = 0; v < uMax; ++v) {
                  face = faceData.get(facing, v, u);
                  if (!translucent) {
                     if (face) {
                        if (!building) {
                           building = true;
                           vStart = v;
                        }

                        vEnd = v + 1;
                     }
                  } else if (building && !face) {
                     off = facing == EnumFacing.DOWN ? 1 : 0;
                     builder.add(buildSideQuad(format, transform, facing, tint, sprite, vStart, u + off, v - vStart));
                     building = false;
                  } else if (!building && face) {
                     building = true;
                     vStart = v;
                  }
               }

               if (building) {
                  v = facing == EnumFacing.DOWN ? 1 : 0;
                  builder.add(buildSideQuad(format, transform, facing, tint, sprite, vStart, u + v, vEnd - vStart));
               }
            }
         }

         var30 = VERTICALS;
         var31 = var30.length;

         for(var32 = 0; var32 < var31; ++var32) {
            facing = var30[var32];

            for(u = 0; u < uMax; ++u) {
               vStart = 0;
               vEnd = vMax;
               building = false;

               for(v = 0; v < vMax; ++v) {
                  face = faceData.get(facing, u, v);
                  if (!translucent) {
                     if (face) {
                        if (!building) {
                           building = true;
                           vStart = v;
                        }

                        vEnd = v + 1;
                     }
                  } else if (building && !face) {
                     off = facing == EnumFacing.EAST ? 1 : 0;
                     builder.add(buildSideQuad(format, transform, facing, tint, sprite, u + off, vStart, v - vStart));
                     building = false;
                  } else if (!building && face) {
                     building = true;
                     vStart = v;
                  }
               }

               if (building) {
                  v = facing == EnumFacing.EAST ? 1 : 0;
                  builder.add(buildSideQuad(format, transform, facing, tint, sprite, u + v, vStart, vEnd - vStart));
               }
            }
         }
      }

      builder.add(buildQuad(format, transform, EnumFacing.NORTH, sprite, tint, 0.0F, 0.0F, 0.46875F, sprite.func_94209_e() + diffU * sU, sprite.func_94206_g() + diffV * (sV + sH), 0.0F, 1.0F, 0.46875F, sprite.func_94209_e() + diffU * sU, sprite.func_94206_g() + diffV * sV, 1.0F, 1.0F, 0.46875F, sprite.func_94209_e() + diffU * (sU + sW), sprite.func_94206_g() + diffV * sV, 1.0F, 0.0F, 0.46875F, sprite.func_94209_e() + diffU * (sU + sW), sprite.func_94206_g() + diffV * (sV + sH)));
      builder.add(buildQuad(format, transform, EnumFacing.SOUTH, sprite, tint, 0.0F, 0.0F, 0.53125F, sprite.func_94209_e() + diffU * sU, sprite.func_94206_g() + diffV * (sV + sH), 1.0F, 0.0F, 0.53125F, sprite.func_94209_e() + diffU * (sU + sW), sprite.func_94206_g() + diffV * (sV + sH), 1.0F, 1.0F, 0.53125F, sprite.func_94209_e() + diffU * (sU + sW), sprite.func_94206_g() + diffV * sV, 0.0F, 1.0F, 0.53125F, sprite.func_94209_e() + diffU * sU, sprite.func_94206_g() + diffV * sV));
      return builder.build();
   }

   private static int getAlpha(int[] pixels, int uMax, int vMax, int u, int v) {
      return pixels[u + (vMax - 1 - v) * uMax] >> 24 & 255;
   }

   private static BakedQuad buildSideQuad(VertexFormat format, Optional transform, EnumFacing side, int tint, TextureAtlasSprite sprite, int u, int v, int size) {
      float eps = 0.01F;
      int width = sprite.func_94211_a();
      int height = sprite.func_94216_b();
      float x0 = (float)u / (float)width;
      float y0 = (float)v / (float)height;
      float x1 = x0;
      float y1 = y0;
      float z0 = 0.46875F;
      float z1 = 0.53125F;
      switch (side) {
         case WEST:
            z0 = 0.53125F;
            z1 = 0.46875F;
         case EAST:
            y1 = (float)(v + size) / (float)height;
            break;
         case DOWN:
            z0 = 0.53125F;
            z1 = 0.46875F;
         case UP:
            x1 = (float)(u + size) / (float)width;
            break;
         default:
            throw new IllegalArgumentException("can't handle z-oriented side");
      }

      float dx = (float)side.func_176730_m().func_177958_n() * 0.01F / (float)width;
      float dy = (float)side.func_176730_m().func_177956_o() * 0.01F / (float)height;
      float u0 = 16.0F * (x0 - dx);
      float u1 = 16.0F * (x1 - dx);
      float v0 = 16.0F * (1.0F - y0 - dy);
      float v1 = 16.0F * (1.0F - y1 - dy);
      return buildQuad(format, transform, remap(side), sprite, tint, x0, y0, z0, sprite.func_94214_a((double)u0), sprite.func_94207_b((double)v0), x1, y1, z0, sprite.func_94214_a((double)u1), sprite.func_94207_b((double)v1), x1, y1, z1, sprite.func_94214_a((double)u1), sprite.func_94207_b((double)v1), x0, y0, z1, sprite.func_94214_a((double)u0), sprite.func_94207_b((double)v0));
   }

   private static EnumFacing remap(EnumFacing side) {
      return side.func_176740_k() == Axis.Y ? side.func_176734_d() : side;
   }

   private static BakedQuad buildQuad(VertexFormat format, Optional transform, EnumFacing side, TextureAtlasSprite sprite, int tint, float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3) {
      UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
      builder.setQuadTint(tint);
      builder.setQuadOrientation(side);
      builder.setTexture(sprite);
      boolean hasTransform = transform.isPresent() && !((TRSRTransformation)transform.get()).isIdentity();
      IVertexConsumer consumer = hasTransform ? new TRSRTransformer(builder, (TRSRTransformation)transform.get()) : builder;
      putVertex((IVertexConsumer)consumer, format, side, x0, y0, z0, u0, v0);
      putVertex((IVertexConsumer)consumer, format, side, x1, y1, z1, u1, v1);
      putVertex((IVertexConsumer)consumer, format, side, x2, y2, z2, u2, v2);
      putVertex((IVertexConsumer)consumer, format, side, x3, y3, z3, u3, v3);
      return builder.build();
   }

   private static void putVertex(IVertexConsumer consumer, VertexFormat format, EnumFacing side, float x, float y, float z, float u, float v) {
      for(int e = 0; e < format.func_177345_h(); ++e) {
         switch (format.func_177348_c(e).func_177375_c()) {
            case POSITION:
               consumer.put(e, new float[]{x, y, z, 1.0F});
               break;
            case COLOR:
               consumer.put(e, new float[]{1.0F, 1.0F, 1.0F, 1.0F});
               break;
            case NORMAL:
               float offX = (float)side.func_82601_c();
               float offY = (float)side.func_96559_d();
               float offZ = (float)side.func_82599_e();
               consumer.put(e, new float[]{offX, offY, offZ, 0.0F});
               break;
            case UV:
               if (format.func_177348_c(e).func_177369_e() == 0) {
                  consumer.put(e, new float[]{u, v, 0.0F, 1.0F});
                  break;
               }
            default:
               consumer.put(e, new float[0]);
         }
      }

   }

   static {
      HORIZONTALS = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
      VERTICALS = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST};
   }

   public static enum Loader implements ICustomModelLoader {
      INSTANCE;

      public void func_110549_a(IResourceManager resourceManager) {
      }

      public boolean accepts(ResourceLocation modelLocation) {
         return modelLocation.func_110624_b().equals("forge") && (modelLocation.func_110623_a().equals("item-layer") || modelLocation.func_110623_a().equals("models/block/item-layer") || modelLocation.func_110623_a().equals("models/item/item-layer"));
      }

      public IModel loadModel(ResourceLocation modelLocation) {
         return ItemLayerWithUVModel.INSTANCE;
      }
   }

   private static class FaceData {
      private final EnumMap data = new EnumMap(EnumFacing.class);
      private final int vMax;

      FaceData(int uMax, int vMax) {
         this.vMax = vMax;
         this.data.put(EnumFacing.WEST, new BitSet(uMax * vMax));
         this.data.put(EnumFacing.EAST, new BitSet(uMax * vMax));
         this.data.put(EnumFacing.UP, new BitSet(uMax * vMax));
         this.data.put(EnumFacing.DOWN, new BitSet(uMax * vMax));
      }

      public void set(EnumFacing facing, int u, int v) {
         ((BitSet)this.data.get(facing)).set(this.getIndex(u, v));
      }

      public boolean get(EnumFacing facing, int u, int v) {
         return ((BitSet)this.data.get(facing)).get(this.getIndex(u, v));
      }

      private int getIndex(int u, int v) {
         return v * this.vMax + u;
      }
   }
}
