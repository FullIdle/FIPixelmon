package com.pixelmonmod.pixelmon.client.models.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ItemPixelmonSpriteModel implements IBakedModel {
   public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("pixelmon:pixelmon_sprite", "inventory");
   IBakedModel iBakedModel;

   public ItemPixelmonSpriteModel(IBakedModel iBakedModel) {
      this.iBakedModel = iBakedModel;
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
      private WeakHashMap weakSpriteExistenceCheck = new WeakHashMap();

      public OverrideList() {
         super(Lists.newArrayList());
      }

      public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
         if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("SpriteName")) {
            Function textureGetterx = (location) -> {
               return Minecraft.func_71410_x().func_147117_R().func_110572_b(location.toString());
            };
            ImmutableMap texMapx = ImmutableMap.of("layer0", stack.func_77978_p().func_74779_i("SpriteName"));
            ItemLayerModel iModelx = ItemLayerModel.INSTANCE.retexture(texMapx);
            Optional trsrTransformationx = TRSRTransformation.identity().apply(Optional.empty());
            ItemPixelmonSpriteModel.this.iBakedModel = iModelx.bake((IModelState)trsrTransformationx.get(), DefaultVertexFormats.field_176599_b, textureGetterx);
         } else if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("ndex")) {
            NBTTagCompound compound = stack.func_77978_p();
            EnumSpecies species = EnumSpecies.getFromDex(compound.func_74765_d("ndex"));
            int form = compound.func_74771_c("form");
            boolean shiny = compound.func_74767_n("Shiny");
            int eggCycles = compound.func_74767_n("isEgg") ? species.getBaseStats(species.getFormEnum(form)).getEggCycles() : compound.func_74762_e("eggCycles");
            Gender gender = Gender.getGender((short)compound.func_74771_c("gender"));
            String customTexture = compound.func_74779_i("CustomTexture");
            String sprite;
            if (eggCycles > 0) {
               sprite = GuiResources.getEggSprite(species, eggCycles).func_110623_a().replace(".png", "").replace("textures/", "");
            } else {
               sprite = GuiResources.getSpritePath(species, form, gender, customTexture, shiny);
            }

            boolean exists;
            if (!this.weakSpriteExistenceCheck.containsKey(sprite)) {
               exists = Pixelmon.proxy.resourceLocationExists(new ResourceLocation("pixelmon", "textures/" + sprite + ".png"));
               exists = exists && Minecraft.func_71410_x().func_147117_R().func_110572_b("pixelmon:" + sprite) != Minecraft.func_71410_x().func_147117_R().func_174944_f();
               this.weakSpriteExistenceCheck.put(sprite, exists);
            }

            exists = (Boolean)this.weakSpriteExistenceCheck.get(sprite);
            if (!exists) {
               sprite = GuiResources.getSpritePath(species, ((IEnumForm)species.getDefaultForms().get(0)).getForm(), gender, "", shiny);
            }

            Function textureGetter = (location) -> {
               return Minecraft.func_71410_x().func_147117_R().func_110572_b(location.toString());
            };
            ImmutableMap texMap = ImmutableMap.of("layer0", "pixelmon:" + sprite);
            ItemLayerModel iModel = ItemLayerModel.INSTANCE.retexture(texMap);
            Optional trsrTransformation = TRSRTransformation.identity().apply(Optional.empty());
            ItemPixelmonSpriteModel.this.iBakedModel = iModel.bake((IModelState)trsrTransformation.get(), DefaultVertexFormats.field_176599_b, textureGetter);
         }

         return ItemPixelmonSpriteModel.this.iBakedModel;
      }
   }
}
