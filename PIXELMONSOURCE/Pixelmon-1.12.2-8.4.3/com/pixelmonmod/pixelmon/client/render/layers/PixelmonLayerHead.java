package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PixelmonLayerHead implements LayerRenderer {
   private final ResourceLocation TOP_HAT_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/tophat.png");
   private final ResourceLocation FEZ_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/fez.png");
   private final ResourceLocation FEDORA_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/fedora.png");
   private final ResourceLocation TRAINERHAT_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/trainerhat.png");
   private final ResourceLocation PIKAHOOD_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/pikahood.png");
   private final ResourceLocation EEVEEHOOD_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/eeveehood.png");
   private final ResourceLocation SCORHOOD_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/scorhood.png");
   private final ResourceLocation ESPEON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/espeon_scarf.png");
   private final ResourceLocation FLAREON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/flareon_scarf.png");
   private final ResourceLocation GLACEON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/glaceon_scarf.png");
   private final ResourceLocation JOLTEON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/jolteon_scarf.png");
   private final ResourceLocation LEAFEON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/leafeon_scarf.png");
   private final ResourceLocation SYLVEON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/sylveon_scarf.png");
   private final ResourceLocation UMBREON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/umbreon_scarf.png");
   private final ResourceLocation VAPOREON_SCARF_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/vaporeon_scarf.png");
   private final ResourceLocation WIKI_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/grad_cap.png");
   private final ResourceLocation GOLD_MONOCLE_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/monocle_gold.png");
   private final ResourceLocation BLACK_MONOCLE_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/monocle_black.png");
   private final ResourceLocation QUAGSIRE_MASK_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/quagsire_mask.png");
   private final ResourceLocation QUARANTINE_MASK_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/quarantine_mask.png");
   private final ResourceLocation WELKIN_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/welkin.png");
   private final ResourceLocation SABLEYE_HOOD_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/hats/sableye_hood.png");
   private final ModelHolder TOPHAT_MODEL = new GenericModelHolder("playeritems/hats/tophat.pqc");
   private final ModelHolder FEZ_MODEL = new GenericModelHolder("playeritems/hats/fez.pqc");
   private final ModelHolder FEDORA_MODEL = new GenericModelHolder("playeritems/hats/fedora.pqc");
   private final ModelHolder TRAINERHAT_MODEL = new GenericModelHolder("playeritems/hats/trainerhat.pqc");
   private final ModelHolder PIKAHOOD_MODEL = new GenericModelHolder("playeritems/hats/pikahood.pqc");
   private final ModelHolder ESPEON_SCARF = new GenericModelHolder("playeritems/hats/espeon_scarf.pqc");
   private final ModelHolder FLAREON_SCARF = new GenericModelHolder("playeritems/hats/flareon_scarf.pqc");
   private final ModelHolder GLACEON_SCARF = new GenericModelHolder("playeritems/hats/glaceon_scarf.pqc");
   private final ModelHolder JOLTEON_SCARF = new GenericModelHolder("playeritems/hats/jolteon_scarf.pqc");
   private final ModelHolder LEAFEON_SCARF = new GenericModelHolder("playeritems/hats/leafeon_scarf.pqc");
   private final ModelHolder SYLVEON_SCARF = new GenericModelHolder("playeritems/hats/sylveon_scarf.pqc");
   private final ModelHolder UMBREON_SCARF = new GenericModelHolder("playeritems/hats/umbreon_scarf.pqc");
   private final ModelHolder VAPOREON_SCARF = new GenericModelHolder("playeritems/hats/vaporeon_scarf.pqc");
   private final ModelHolder WIKI_MODEL = new GenericModelHolder("playeritems/hats/grad_cap.pqc");
   private final ModelHolder MONOCLE_MODEL = new GenericModelHolder("playeritems/hats/monocle.pqc");
   private final ModelHolder QUAGSIRE_MASK_MODEL = new GenericModelHolder("playeritems/hats/quagsire_mask.pqc");
   private final ModelHolder QUARANTINE_MASK_MODEL = new GenericModelHolder("playeritems/hats/quarantine_mask.pqc");
   private final ModelHolder WELKIN_MODEL = new GenericModelHolder("playeritems/hats/welkin.pqc");
   private final ModelHolder SABLEYE_HOOD_HEAD_MODEL = new GenericModelHolder("playeritems/hats/sableye_hood_head.pqc");
   private final ModelHolder SABLEYE_HOOD_BODY_MODEL = new GenericModelHolder("playeritems/hats/sableye_hood_body.pqc");
   private final ResourceLocation SPHEAL_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/sphealhat.png");
   private final ResourceLocation SHINYSPHEAL_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/shinysphealhat.png");
   private final ResourceLocation SPHEAL_STITCHED_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/sphealhatstitches.png");
   private final ResourceLocation SHINYSPHEAL_STICHED_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/shinysphealhatstitches.png");
   private final ModelHolder SPHEALHAT_MODEL = new GenericModelHolder("playeritems/hats/sphealhat.pqc");
   private ModelRenderer head;
   private final RenderPlayer renderer;
   private final ResourceLocation locationCapBG;
   private final DynamicTexture capBG;

   public PixelmonLayerHead(RenderPlayer renderer) {
      this.renderer = renderer;
      this.capBG = new DynamicTexture(1, 1);
      this.capBG.func_110565_c()[0] = -1;
      this.capBG.func_110564_a();
      this.locationCapBG = Minecraft.func_71410_x().func_110434_K().func_110578_a("capBG", this.capBG);
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      PixelExtrasData extras = PlayerExtraDataStore.get(player);
      if (extras.isReady() && !player.func_82150_aj()) {
         this.head = this.renderer.func_177087_b().field_78116_c;
         Minecraft.func_71410_x().field_71424_I.func_76320_a("layer_hat");
         if (extras.isEnabled(PixelExtrasData.Category.HAT) && extras.getHatType() != PixelExtrasData.HatType.NONE) {
            this.renderHat(player, extras, partialTicks);
            if (extras.getHatType() == PixelExtrasData.HatType.SABLEYE_HOOD) {
               GlStateManager.func_179094_E();
               if (player.func_70093_af()) {
                  GlStateManager.func_179114_b(3.0F, 0.0F, 0.0F, 0.0F);
                  GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
               }

               ModelRenderer body = this.renderer.func_177087_b().field_78115_e;
               body.func_78794_c(1.0F);
               GlStateManager.func_179109_b(body.field_82906_o, body.field_82908_p, body.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 1.42F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(this.SABLEYE_HOOD_TEXTURE);
               ((GenericSmdModel)this.SABLEYE_HOOD_BODY_MODEL.getModel()).renderModel(1.0F);
               GlStateManager.func_179121_F();
            }
         }

         if (extras.isEnabled(PixelExtrasData.Category.MONOCLE) && (extras.getMonocleType() == PixelExtrasData.MonocleType.QUAGSIRE_MASK || extras.getMonocleType() == PixelExtrasData.MonocleType.QUARANTINE_MASK)) {
            this.renderMask(player, extras, partialTicks);
         } else if (extras.isEnabled(PixelExtrasData.Category.MONOCLE) && extras.getMonocleType() != PixelExtrasData.MonocleType.NONE) {
            this.renderMonocle(player, extras, partialTicks);
         }

         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }
   }

   public void renderHat(EntityPlayer player, PixelExtrasData data, float partialTicks) {
      ModelHolder hat;
      ResourceLocation path;
      label44:
      switch (data.getHatType()) {
         case TOP_HAT:
            hat = this.TOPHAT_MODEL;
            path = this.TOP_HAT_TEXTURE;
            break;
         case FEZ:
            hat = this.FEZ_MODEL;
            path = this.FEZ_TEXTURE;
            break;
         case FEDORA:
            hat = this.FEDORA_MODEL;
            path = this.FEDORA_TEXTURE;
            break;
         case TRAINER_CAP:
            hat = this.TRAINERHAT_MODEL;
            path = this.TRAINERHAT_TEXTURE;
            break;
         case PIKA_HOOD:
            hat = this.PIKAHOOD_MODEL;
            path = this.PIKAHOOD_TEXTURE;
            break;
         case EEVEE_HOOD:
            hat = this.PIKAHOOD_MODEL;
            path = this.EEVEEHOOD_TEXTURE;
            break;
         case SCOR_HOOD:
            hat = this.PIKAHOOD_MODEL;
            path = this.SCORHOOD_TEXTURE;
            break;
         case WIKI:
            hat = this.WIKI_MODEL;
            path = this.WIKI_TEXTURE;
            break;
         case WELKIN_HAT:
            hat = this.WELKIN_MODEL;
            path = this.WELKIN_TEXTURE;
            break;
         case SABLEYE_HOOD:
            hat = this.SABLEYE_HOOD_HEAD_MODEL;
            path = this.SABLEYE_HOOD_TEXTURE;
            break;
         case ESPEON_SCARF:
            hat = this.ESPEON_SCARF;
            path = this.ESPEON_SCARF_TEXTURE;
            break;
         case FLAREON_SCARF:
            hat = this.FLAREON_SCARF;
            path = this.FLAREON_SCARF_TEXTURE;
            break;
         case GLACEON_SCARF:
            hat = this.GLACEON_SCARF;
            path = this.GLACEON_SCARF_TEXTURE;
            break;
         case JOLTEON_SCARF:
            hat = this.JOLTEON_SCARF;
            path = this.JOLTEON_SCARF_TEXTURE;
            break;
         case LEAFEON_SCARF:
            hat = this.LEAFEON_SCARF;
            path = this.LEAFEON_SCARF_TEXTURE;
            break;
         case SYLVEON_SCARF:
            hat = this.SYLVEON_SCARF;
            path = this.SYLVEON_SCARF_TEXTURE;
            break;
         case UMBREON_SCARF:
            hat = this.UMBREON_SCARF;
            path = this.UMBREON_SCARF_TEXTURE;
            break;
         case VAPOREON_SCARF:
            hat = this.VAPOREON_SCARF;
            path = this.VAPOREON_SCARF_TEXTURE;
            break;
         case SPHEAL_HAT:
            hat = this.SPHEALHAT_MODEL;
            switch (data.getSphealType()) {
               case DEFAULT:
               default:
                  path = this.SPHEAL_TEXTURE;
                  break label44;
               case SHINY:
                  path = this.SHINYSPHEAL_TEXTURE;
                  break label44;
               case STITCHED:
                  path = this.SPHEAL_STITCHED_TEXTURE;
                  break label44;
               case SHINY_STITCHED:
                  path = this.SHINYSPHEAL_STICHED_TEXTURE;
                  break label44;
            }
         default:
            return;
      }

      GlStateManager.func_179094_E();
      if (player.func_70093_af()) {
         GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
      }

      this.head.func_78794_c(1.0F);
      GlStateManager.func_179109_b(this.head.field_82906_o, this.head.field_82908_p, this.head.field_82907_q);
      GlStateManager.func_179109_b(0.0F, 1.5F, 0.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179091_B();
      if (data.getHatType() == PixelExtrasData.HatType.TRAINER_CAP) {
         if (data.getColours(PixelExtrasData.Category.HAT)[0] == -1) {
            this.renderer.func_110776_a(this.locationCapBG);
            int i = player.field_70173_aa / 25 + player.func_145782_y();
            int j = EnumDyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f7 = ((float)(player.field_70173_aa % 25) + partialTicks) / 25.0F;
            float[] afloat1 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(k));
            float[] afloat2 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(l));
            float r = afloat1[0] * (1.0F - f7) + afloat2[0] * f7;
            float g = afloat1[1] * (1.0F - f7) + afloat2[1] * f7;
            float b = afloat1[2] * (1.0F - f7) + afloat2[2] * f7;
            this.capBG.func_110565_c()[0] = -16777216 | (int)((double)(r * 255.0F) + 0.5) << 16 | (int)((double)(g * 255.0F) + 0.5) << 8 | (int)((double)(b * 255.0F) + 0.5);
            this.capBG.func_110564_a();
         } else {
            this.capBG.func_110565_c()[0] = -16777216 | data.getColours(PixelExtrasData.Category.HAT)[0] << 16 | data.getColours(PixelExtrasData.Category.HAT)[1] << 8 | data.getColours(PixelExtrasData.Category.HAT)[2];
            this.capBG.func_110564_a();
         }

         ((GenericSmdModel)hat.getModel()).renderModel(1.0F);
         GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      }

      Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(path);
      ((GenericSmdModel)hat.getModel()).renderModel(1.0F);
      GlStateManager.func_179121_F();
   }

   public void renderMonocle(EntityPlayer player, PixelExtrasData data, float partialTicks) {
      GlStateManager.func_179094_E();
      if (player.func_70093_af()) {
         GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
      }

      this.head.func_78794_c(1.0F);
      GlStateManager.func_179109_b(this.head.field_82906_o, this.head.field_82908_p, this.head.field_82907_q);
      GlStateManager.func_179109_b(0.0F, 1.5F, 0.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179091_B();
      GlStateManager.func_179109_b(0.15F, -1.45F, -0.29F);
      GlStateManager.func_179139_a(0.3, 0.3, 0.3);
      ResourceLocation texture;
      if (data.getMonocleType() == PixelExtrasData.MonocleType.GOLD_MONOCLE) {
         texture = this.GOLD_MONOCLE_TEXTURE;
      } else if (data.getMonocleType() == PixelExtrasData.MonocleType.BLACK_MONOCLE) {
         texture = this.BLACK_MONOCLE_TEXTURE;
      } else {
         texture = this.GOLD_MONOCLE_TEXTURE;
      }

      Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(texture);
      ((GenericSmdModel)this.MONOCLE_MODEL.getModel()).renderModel(1.0F);
      GlStateManager.func_179121_F();
   }

   public void renderMask(EntityPlayer player, PixelExtrasData data, float partialTicks) {
      GlStateManager.func_179094_E();
      if (player.func_70093_af()) {
         GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
      }

      this.head.func_78794_c(1.0F);
      GlStateManager.func_179109_b(this.head.field_82906_o, this.head.field_82908_p, this.head.field_82907_q);
      GlStateManager.func_179109_b(0.0F, 1.7F, -1.735F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179091_B();
      if (data.getMonocleType() == PixelExtrasData.MonocleType.QUAGSIRE_MASK) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(this.QUAGSIRE_MASK_TEXTURE);
         ((GenericSmdModel)this.QUAGSIRE_MASK_MODEL.getModel()).renderModel(1.0F);
      } else {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(this.QUARANTINE_MASK_TEXTURE);
         ((GenericSmdModel)this.QUARANTINE_MASK_MODEL.getModel()).renderModel(1.0F);
      }

      GlStateManager.func_179121_F();
   }

   public boolean func_177142_b() {
      return false;
   }
}
