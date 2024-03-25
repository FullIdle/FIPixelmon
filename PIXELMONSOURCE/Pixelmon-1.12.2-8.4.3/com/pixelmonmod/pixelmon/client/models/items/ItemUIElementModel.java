package com.pixelmonmod.pixelmon.client.models.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ItemUIElementModel implements IBakedModel {
   private static final float[] WHITE = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("pixelmon:ui_element", "inventory");
   IBakedModel iBakedModel;
   final IBakedModel defaultModel;

   public ItemUIElementModel(IBakedModel iBakedModel) {
      this.iBakedModel = iBakedModel;
      this.defaultModel = iBakedModel;
   }

   public List func_188616_a(IBlockState state, EnumFacing side, long rand) {
      return this.iBakedModel.func_188616_a(state, side, rand);
   }

   public boolean func_177555_b() {
      return this.iBakedModel.func_177555_b();
   }

   public boolean func_177556_c() {
      return false;
   }

   public boolean func_188618_c() {
      return false;
   }

   public TextureAtlasSprite func_177554_e() {
      return this.iBakedModel.func_177554_e();
   }

   public ItemCameraTransforms func_177552_f() {
      return this.iBakedModel.func_177552_f();
   }

   public ItemOverrideList func_188617_f() {
      return new OverrideList();
   }

   class OverrideList extends ItemOverrideList {
      public OverrideList() {
         super(Lists.newArrayList());
      }

      public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
         if (stack.func_77942_o() && ItemUIElement.isBakedModel(stack)) {
            Optional rl = ItemUIElement.getImage(stack);
            if (rl.isPresent()) {
               int w = (Integer)ItemUIElement.getImageWidth(stack).orElse(0);
               int h = (Integer)ItemUIElement.getImageHeight(stack).orElse(0);
               int u = ((Float)ItemUIElement.getImageU(stack).orElse(0.0F)).intValue();
               int v = ((Float)ItemUIElement.getImageV(stack).orElse(0.0F)).intValue();
               Function textureGetter = (location) -> {
                  return Minecraft.func_71410_x().func_147117_R().func_110572_b(location.toString());
               };
               String sprite = ((ResourceLocation)rl.get()).toString().replace("textures/", "").replace(".png", "");
               ImmutableMap texMap = ImmutableMap.of("layer0", sprite);
               ItemLayerWithUVModel iModel = ItemLayerWithUVModel.INSTANCE.retexture(texMap, w, h, u, v);
               Optional trsrTransformation = TRSRTransformation.identity().apply(Optional.empty());
               ItemUIElementModel.this.iBakedModel = iModel.bake((IModelState)trsrTransformation.get(), DefaultVertexFormats.field_176599_b, textureGetter);
            } else {
               ItemUIElementModel.this.iBakedModel = ItemUIElementModel.this.defaultModel;
            }
         } else {
            ItemUIElementModel.this.iBakedModel = ItemUIElementModel.this.defaultModel;
         }

         return ItemUIElementModel.this.iBakedModel;
      }
   }
}
