package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class LayerMegaItems implements LayerRenderer {
   private final ResourceLocation texture = new ResourceLocation("pixelmon:textures/playeritems/megabraceletoras.png");
   private final ResourceLocation necklaceTexture = new ResourceLocation("pixelmon:textures/playeritems/boost_necklace.png");
   private final ResourceLocation glassesTexture = new ResourceLocation("pixelmon:textures/playeritems/mega_glasses.png");
   private final ResourceLocation anchorTexture = new ResourceLocation("pixelmon:textures/playeritems/mega_anchor.png");
   private final ResourceLocation dynamaxTexture = new ResourceLocation("pixelmon:textures/playeritems/dynamax_band.png");
   private final ResourceLocation tiaraTexture = new ResourceLocation("pixelmon:textures/playeritems/mega_tiara.png");
   private final ModelHolder braceletModel = new GenericModelHolder("playeritems/megaitems/megabraceletoras.pqc");
   private final ModelHolder stoneModel = new GenericModelHolder("playeritems/megaitems/megabraceletorasstone.pqc");
   private final ModelHolder necklace = new GenericModelHolder("playeritems/megaitems/boost_necklace.pqc");
   private final ModelHolder glasses = new GenericModelHolder("playeritems/megaitems/mega_glasses.pqc");
   private final ModelHolder anchor = new GenericModelHolder("playeritems/megaitems/mega_anchor.pqc");
   private final ModelHolder dynamax = new GenericModelHolder("playeritems/megaitems/dynamax_band.pqc");
   private final ModelHolder tiara = new GenericModelHolder("playeritems/megaitems/mega_tiara.pqc");
   private final RenderPlayer renderer;

   public LayerMegaItems(RenderPlayer renderer) {
      this.renderer = renderer;
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      EnumMegaItem megaItem = EntityPlayerExtension.getPlayerMegaItem(player);
      if (megaItem != EnumMegaItem.Disabled && megaItem != EnumMegaItem.None && !player.func_82150_aj()) {
         if (megaItem == EnumMegaItem.BoostNecklace) {
            PixelExtrasData extras = PlayerExtraDataStore.get(player);
            if (!extras.isReady()) {
               return;
            }

            if (!extras.hasBoostedNecklace()) {
               megaItem = EnumMegaItem.BraceletORAS;
            }
         }

         ModelRenderer left = this.renderer.func_177087_b().field_178724_i;
         ModelRenderer body = this.renderer.func_177087_b().field_78115_e;
         ModelRenderer head = this.renderer.func_177087_b().field_78116_c;
         Minecraft.func_71410_x().field_71424_I.func_76320_a("layer_mega_wear");
         GlStateManager.func_179094_E();
         if (megaItem == EnumMegaItem.BraceletORAS) {
            this.renderBracelet(left, EnumHandSide.LEFT, player.func_70093_af());
         } else if (megaItem == EnumMegaItem.DynamaxBand) {
            if (player.func_70093_af()) {
               GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
            }

            scale = 1.05F;
            if (((ModelBox)left.field_78804_l.get(0)).field_78248_d == 2.0F) {
               scale = 1.02F;
            }

            this.renderer.func_177087_b().func_187073_a(0.0625F, EnumHandSide.LEFT);
            GlStateManager.func_179109_b(0.0F, 0.4F, 0.0F);
            GlStateManager.func_179152_a(scale, scale, scale);
            GlStateManager.func_179109_b(left.field_82906_o, left.field_82908_p, left.field_82907_q);
            GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179109_b(-0.3F, 0.0F, 0.875F);
            this.renderer.func_110776_a(this.dynamaxTexture);
            GlStateManager.func_179089_o();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            ((GenericSmdModel)this.dynamax.getModel()).renderModel(1.0F);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         } else {
            PixelExtrasData extras;
            float offset;
            float scaleBoost;
            if (megaItem == EnumMegaItem.BoostNecklace) {
               extras = PlayerExtraDataStore.get(player);
               offset = -0.01F;
               scaleBoost = -0.03F;
               scale = 1.0F;
               if (extras.getSashType() != PixelExtrasData.SashType.NONE) {
                  offset = -0.02F;
                  scaleBoost = 0.0F;
               }

               if (extras.getRobeType() != PixelExtrasData.RobeType.NONE) {
                  scaleBoost = 0.12F;
                  offset = -0.03F;
               }

               if (player.func_70093_af()) {
                  GlStateManager.func_179114_b(3.0F, 0.0F, 0.0F, 0.0F);
                  GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
               }

               body.func_78794_c(1.0F);
               GlStateManager.func_179109_b(body.field_82906_o, body.field_82908_p, body.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 0.0F, offset);
               GlStateManager.func_179152_a(scale, scale, scale + scaleBoost);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               this.renderer.func_177068_d().field_78724_e.func_110577_a(this.necklaceTexture);
               ((GenericSmdModel)this.necklace.getModel()).renderModel(1.0F);
            } else if (megaItem == EnumMegaItem.MegaAnchor) {
               extras = PlayerExtraDataStore.get(player);
               offset = -0.01F;
               scaleBoost = -0.03F;
               scale = 1.0F;
               if (extras.getSashType() != PixelExtrasData.SashType.NONE) {
                  offset = -0.02F;
                  scaleBoost = 0.0F;
               }

               if (extras.getRobeType() != PixelExtrasData.RobeType.NONE) {
                  scaleBoost = 0.12F;
                  offset = -0.03F;
               }

               if (player.func_70093_af()) {
                  GlStateManager.func_179114_b(3.0F, 0.0F, 0.0F, 0.0F);
                  GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
               }

               body.func_78794_c(1.0F);
               GlStateManager.func_179109_b(body.field_82906_o, body.field_82908_p, body.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 0.0F, offset);
               GlStateManager.func_179152_a(scale, scale, scale + scaleBoost);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               this.renderer.func_177068_d().field_78724_e.func_110577_a(this.anchorTexture);
               ((GenericSmdModel)this.anchor.getModel()).renderModel(1.0F);
            } else if (megaItem == EnumMegaItem.MegaGlasses) {
               if (player.func_70093_af()) {
                  GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
               }

               head.func_78794_c(1.0F);
               GlStateManager.func_179109_b(head.field_82906_o, head.field_82908_p, head.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 1.5F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               GlStateManager.func_179109_b(0.0F, -1.72F, -0.15F);
               GlStateManager.func_179139_a(0.62, 0.62, 0.62);
               Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(this.glassesTexture);
               ((GenericSmdModel)this.glasses.getModel()).renderModel(1.0F);
            } else if (megaItem == EnumMegaItem.MegaTiara) {
               if (player.func_70093_af()) {
                  GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
               }

               head.func_78794_c(1.0F);
               GlStateManager.func_179109_b(head.field_82906_o, head.field_82908_p, head.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 1.5F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(this.tiaraTexture);
               ((GenericSmdModel)this.tiara.getModel()).renderModel(1.0F);
            }
         }

         GlStateManager.func_179121_F();
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }
   }

   void renderBracelet(ModelRenderer arm, EnumHandSide side, boolean sneak) {
      GlStateManager.func_179094_E();
      if (sneak) {
         GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
      }

      float scale;
      if (((ModelBox)arm.field_78804_l.get(0)).field_78248_d == 2.0F) {
         scale = 0.17F;
      } else {
         scale = 0.19F;
      }

      this.renderer.func_177087_b().func_187073_a(0.0625F, side);
      GlStateManager.func_179109_b(0.0F, 0.4F, 0.0F);
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(arm.field_82906_o, arm.field_82908_p, arm.field_82907_q);
      GlStateManager.func_179109_b(0.35F, 0.0F, 0.0F);
      this.renderer.func_110776_a(this.texture);
      GlStateManager.func_179089_o();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      ((GenericSmdModel)this.braceletModel.getModel()).renderModel(1.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179140_f();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.4F);
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      ((GenericSmdModel)this.stoneModel.getModel()).renderModel(1.0F);
      GlStateManager.func_179084_k();
      GlStateManager.func_179145_e();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   public boolean func_177142_b() {
      return true;
   }
}
