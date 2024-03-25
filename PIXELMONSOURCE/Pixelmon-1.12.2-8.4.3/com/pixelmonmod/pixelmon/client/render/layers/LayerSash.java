package com.pixelmonmod.pixelmon.client.render.layers;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerSash implements LayerRenderer {
   private final ResourceLocation normalSash = new ResourceLocation("pixelmon:textures/playeritems/otherstuff.png");
   private final ResourceLocation boostSash = new ResourceLocation("pixelmon:textures/playeritems/boost_sash.png");
   private final ResourceLocation adminSash = new ResourceLocation("pixelmon:textures/playeritems/admin.png");
   private final ResourceLocation developerSash = new ResourceLocation("pixelmon:textures/playeritems/developer.png");
   private final ResourceLocation moddelerSash = new ResourceLocation("pixelmon:textures/playeritems/modeler.png");
   private final ResourceLocation supportSash = new ResourceLocation("pixelmon:textures/playeritems/support.png");
   private final ModelHolder SASH_MODEL = new GenericModelHolder("playeritems/sash/sash.pqc");
   private final RenderPlayer renderer;
   private final HashMap pokeballModels = Maps.newHashMap();
   private final ResourceLocation locationSashBG;
   private final DynamicTexture sashBG;

   public LayerSash(RenderPlayer renderer) {
      this.renderer = renderer;
      this.sashBG = new DynamicTexture(1, 1);
      this.sashBG.func_110565_c()[0] = -1;
      this.sashBG.func_110564_a();
      this.locationSashBG = Minecraft.func_71410_x().func_110434_K().func_110578_a("sashBG2", this.sashBG);
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      PixelExtrasData extras = PlayerExtraDataStore.get(player);
      if (extras.isReady() && extras.isEnabled(PixelExtrasData.Category.SASH) && extras.getSashType() != PixelExtrasData.SashType.NONE && !player.func_82150_aj()) {
         Minecraft.func_71410_x().field_71424_I.func_76320_a("layer_sash");
         ModelRenderer body = this.renderer.func_177087_b().field_78115_e;
         GlStateManager.func_179094_E();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         boolean needsColouring = false;
         ResourceLocation texture = this.normalSash;
         switch (extras.getSashType()) {
            case RANK_ADMIN:
            case RANK_JR:
               texture = this.adminSash;
               break;
            case RANK_DEV:
               texture = this.developerSash;
               break;
            case RANK_MODELER:
               texture = this.moddelerSash;
               break;
            case RANK_SUPPORT:
               texture = this.supportSash;
               break;
            case BOOSTER:
               texture = this.boostSash;
               break;
            default:
               needsColouring = true;
         }

         scale = 1.0F;
         if (player.func_70093_af()) {
            GlStateManager.func_179114_b(3.0F, 0.0F, 0.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         body.func_78794_c(scale);
         GlStateManager.func_179109_b(body.field_82906_o, body.field_82908_p, body.field_82907_q);
         GlStateManager.func_179137_b(0.0, 1.5, 0.0);
         if (((ItemStack)player.field_71071_by.field_70460_b.get(2)).func_190926_b() && !extras.isEnabled(PixelExtrasData.Category.ROBE)) {
            GlStateManager.func_179152_a(scale, scale, scale);
         } else {
            GlStateManager.func_179152_a(scale, scale, scale + 0.31F);
         }

         int k;
         int l;
         if (extras.getSashType() == PixelExtrasData.SashType.RAINBOW) {
            this.renderer.func_110776_a(this.locationSashBG);
            int i = player.field_70173_aa / 25 + player.func_145782_y();
            int j = EnumDyeColor.values().length;
            k = i % j;
            l = (i + 1) % j;
            float f7 = ((float)(player.field_70173_aa % 25) + partialTicks) / 25.0F;
            float[] afloat1 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(k));
            float[] afloat2 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(l));
            float r = afloat1[0] * (1.0F - f7) + afloat2[0] * f7;
            float g = afloat1[1] * (1.0F - f7) + afloat2[1] * f7;
            float b = afloat1[2] * (1.0F - f7) + afloat2[2] * f7;
            this.sashBG.func_110565_c()[0] = -16777216 | (int)((double)(r * 255.0F) + 0.5) << 16 | (int)((double)(g * 255.0F) + 0.5) << 8 | (int)((double)(b * 255.0F) + 0.5);
            this.sashBG.func_110564_a();
            ((GenericSmdModel)this.SASH_MODEL.getModel()).renderModel(1.0F);
            GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
            this.renderer.func_110776_a(this.normalSash);
            needsColouring = false;
         }

         if (needsColouring) {
            this.renderer.func_110776_a(this.locationSashBG);
            if (extras.getColours(PixelExtrasData.Category.SASH)[0] != -1) {
               this.sashBG.func_110565_c()[0] = -16777216 | extras.getColours(PixelExtrasData.Category.SASH)[0] << 16 | extras.getColours(PixelExtrasData.Category.SASH)[1] << 8 | extras.getColours(PixelExtrasData.Category.SASH)[2];
            } else {
               this.sashBG.func_110565_c()[0] = -4773323;
            }

            this.sashBG.func_110564_a();
            ((GenericSmdModel)this.SASH_MODEL.getModel()).renderModel(1.0F);
            GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
            this.renderer.func_110776_a(this.normalSash);
         } else {
            this.renderer.func_110776_a(texture);
         }

         ((GenericSmdModel)this.SASH_MODEL.getModel()).renderModel(1.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179137_b(-0.175, 1.44, 0.15);
         int[] balls = EntityPlayerExtension.getPlayerPokeballs(player);
         int[] var24 = balls;
         k = balls.length;

         for(l = 0; l < k; ++l) {
            int ball = var24[l];
            if (ball >= 0) {
               EnumPokeballs pokeball = EnumPokeballs.getFromIndex(ball);
               if (this.pokeballModels.get(pokeball) == null) {
                  this.pokeballModels.put(pokeball, pokeball.getModel());
               }

               this.renderer.func_110776_a(new ResourceLocation(pokeball.getTextureDirectory() + pokeball.getTexture()));
               GlStateManager.func_179137_b(0.0275, -0.05, 0.0);
               ((ModelPokeballs)this.pokeballModels.get(pokeball)).render((Entity)null, 6.0E-4F);
            }
         }

         GlStateManager.func_179121_F();
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }
   }

   public boolean func_177142_b() {
      return true;
   }
}
