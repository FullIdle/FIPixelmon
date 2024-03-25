package com.pixelmonmod.pixelmon.client.render.layers.npc;

import com.pixelmonmod.pixelmon.client.models.IPixelmonModel;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumOldGenMode;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class LayerMegaItemsNPC implements LayerRenderer {
   private final ResourceLocation texture = new ResourceLocation("pixelmon:textures/playeritems/megabraceletoras.png");
   private final ResourceLocation necklaceTexture = new ResourceLocation("pixelmon:textures/playeritems/boost_necklace.png");
   private final ResourceLocation glassesTexture = new ResourceLocation("pixelmon:textures/playeritems/mega_glasses.png");
   private final ResourceLocation anchorTexture = new ResourceLocation("pixelmon:textures/playeritems/mega_anchor.png");
   private final ResourceLocation dynamaxTexture = new ResourceLocation("pixelmon:textures/playeritems/dynamax_band.png");
   private final IPixelmonModel braceletModel;
   private final IPixelmonModel stoneModel;
   private final IPixelmonModel necklace;
   private final IPixelmonModel glasses;
   private final IPixelmonModel anchor;
   private final IPixelmonModel dynamax;
   private final RenderNPC renderer;

   public LayerMegaItemsNPC(RenderNPC renderer) {
      this.braceletModel = (IPixelmonModel)EnumCustomModel.MegaBraceletORAS.getModel();
      this.stoneModel = (IPixelmonModel)EnumCustomModel.MegaBraceletORASStone.getModel();
      this.necklace = (IPixelmonModel)EnumCustomModel.MegaBoostNecklace.getModel();
      this.glasses = (IPixelmonModel)EnumCustomModel.MegaGlasses.getModel();
      this.anchor = (IPixelmonModel)EnumCustomModel.MegaAnchor.getModel();
      this.dynamax = (IPixelmonModel)EnumCustomModel.DynamaxBand.getModel();
      this.renderer = renderer;
   }

   public void doRenderLayer(EntityNPC npc, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (npc instanceof NPCTrainer) {
         if (this.renderer.func_177087_b() instanceof ModelPlayer) {
            NPCTrainer trainer = (NPCTrainer)npc;
            boolean oldGenWorld = PixelmonConfig.oldGenDimensions.contains(npc.field_71093_bK);
            EnumOldGenMode oldGenMode = trainer.getOldGen();
            EnumMegaItemsUnlocked itemInUse = trainer.getMegaItem();
            EnumMegaItem megaItem = EnumMegaItem.None;
            if (oldGenMode == EnumOldGenMode.World) {
               if (oldGenWorld && itemInUse.canMega()) {
                  megaItem = EnumMegaItem.BraceletORAS;
               } else if (!oldGenWorld && itemInUse.canDynamax()) {
                  megaItem = EnumMegaItem.DynamaxBand;
               }
            } else if (oldGenMode == EnumOldGenMode.Mega && itemInUse.canMega()) {
               megaItem = EnumMegaItem.BraceletORAS;
            } else if (oldGenMode == EnumOldGenMode.Dynamax && itemInUse.canDynamax()) {
               megaItem = EnumMegaItem.DynamaxBand;
            }

            if (megaItem != EnumMegaItem.None && !trainer.func_82150_aj()) {
               ModelPlayer biped = (ModelPlayer)this.renderer.func_177087_b();
               ModelRenderer left = biped.field_178724_i;
               ModelRenderer body = biped.field_78115_e;
               ModelRenderer head = biped.field_78116_c;
               GlStateManager.func_179094_E();
               if (megaItem == EnumMegaItem.BraceletORAS) {
                  if (npc.func_70093_af()) {
                     GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
                  }

                  if (((ModelBox)left.field_78804_l.get(0)).field_78248_d == 2.0F) {
                     scale = 0.17F;
                  } else {
                     scale = 0.19F;
                  }

                  biped.func_187073_a(0.0625F, EnumHandSide.LEFT);
                  GlStateManager.func_179109_b(0.0F, 0.4F, 0.0F);
                  GlStateManager.func_179152_a(scale, scale, scale);
                  GlStateManager.func_179109_b(left.field_82906_o, left.field_82908_p, left.field_82907_q);
                  GlStateManager.func_179109_b(0.35F, 0.0F, 0.0F);
                  this.renderer.func_110776_a(this.texture);
                  GlStateManager.func_179089_o();
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.braceletModel.renderAll(0.0F);
                  GlStateManager.func_179147_l();
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.4F);
                  GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                  this.stoneModel.renderAll(0.0F);
                  GlStateManager.func_179084_k();
                  GlStateManager.func_179145_e();
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               } else {
                  if (npc.func_70093_af()) {
                     GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
                  }

                  scale = 1.05F;
                  if (((ModelBox)left.field_78804_l.get(0)).field_78248_d == 2.0F) {
                     scale = 1.02F;
                  }

                  biped.func_187073_a(0.0625F, EnumHandSide.LEFT);
                  GlStateManager.func_179109_b(0.0F, 0.4F, 0.0F);
                  GlStateManager.func_179152_a(scale, scale, scale);
                  GlStateManager.func_179109_b(left.field_82906_o, left.field_82908_p, left.field_82907_q);
                  GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
                  GlStateManager.func_179109_b(-0.3F, 0.0F, 0.875F);
                  this.renderer.func_110776_a(this.dynamaxTexture);
                  GlStateManager.func_179089_o();
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                  this.dynamax.renderAll(0.0F);
                  GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               }

               GlStateManager.func_179121_F();
            }
         }
      }
   }

   public boolean func_177142_b() {
      return true;
   }
}
