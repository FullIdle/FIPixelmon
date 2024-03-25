package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.animations.EnumArm;
import com.pixelmonmod.pixelmon.client.models.blocks.GenericSmdModel;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PixelmonLayerRobe implements LayerRenderer {
   private static final ResourceLocation WIZARD_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/wizard.png");
   private static final ResourceLocation HWEEN_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/hween.png");
   private static final ResourceLocation ALTER_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/alter.png");
   private static final ResourceLocation STRIKE_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/strike.png");
   private static final ResourceLocation ASHEN_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/ashen.png");
   private static final ResourceLocation ABOMASNOW_CLOAK_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/abomasnow_cloak.png");
   private static final ResourceLocation FROSLASS_YUKATA_TEXTURE = new ResourceLocation("pixelmon", "textures/playeritems/robe/yukata.png");
   private static final ModelHolder WIZARD_ROBE = new GenericModelHolder("playeritems/robe/robe.pqc");
   private static final ModelHolder WIZARD_ROBE_ARM = new GenericModelHolder("playeritems/robe/robe_arm.pqc");
   private static final ModelHolder HWEEN_ROBE = new GenericModelHolder("playeritems/robe/hween_robe.pqc");
   private static final ModelHolder HWEEN_ROBE_LEFT = new GenericModelHolder("playeritems/robe/hween_robe_left_arm.pqc");
   private static final ModelHolder HWEEN_ROBE_RIGHT = new GenericModelHolder("playeritems/robe/hween_robe_right_arm.pqc");
   private static final ModelHolder ABOMASNOW_CLOAK = new GenericModelHolder("playeritems/robe/abomasnow_cloak.pqc");
   private static final ModelHolder ABOMASNOW_CLOAK_ARM = new GenericModelHolder("playeritems/robe/abomasnow_cloak_arm.pqc");
   private static final ModelHolder YUKATA_ROBE = new GenericModelHolder("playeritems/robe/yukata.pqc");
   private static final ModelHolder YUKATA_LEFT_ARM = new GenericModelHolder("playeritems/robe/yukata_left_arm.pqc");
   private static final ModelHolder YUKATA_RIGHT_ARM = new GenericModelHolder("playeritems/robe/yukata_right_arm.pqc");
   private final RenderPlayer renderer;
   ModelRenderer leftArm;
   ModelRenderer rightArm;

   public PixelmonLayerRobe(RenderPlayer renderer) {
      this.renderer = renderer;
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      PixelExtrasData extras = PlayerExtraDataStore.get(player);
      if (extras.isReady() && extras.isEnabled(PixelExtrasData.Category.ROBE) && extras.getRobeType() != PixelExtrasData.RobeType.NONE && !player.func_82150_aj()) {
         PixelExtrasData.RobeType robeType = extras.getRobeType();
         this.leftArm = this.renderer.func_177087_b().field_178724_i;
         this.rightArm = this.renderer.func_177087_b().field_178723_h;
         ModelRenderer body = this.renderer.func_177087_b().field_78115_e;
         Minecraft.func_71410_x().field_71424_I.func_76320_a("layer_robe");
         GlStateManager.func_179094_E();
         if (player.func_70093_af()) {
            GlStateManager.func_179114_b(3.0F, 0.0F, 0.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         body.func_78794_c(1.0F);
         GlStateManager.func_179109_b(body.field_82906_o, body.field_82908_p, body.field_82907_q);
         GlStateManager.func_179109_b(0.0F, 1.22F, 0.0F);
         GlStateManager.func_179139_a(0.36, 0.45, 0.45);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179091_B();
         this.bindTexture(robeType);
         if (robeType != PixelExtrasData.RobeType.WIZARD && robeType != PixelExtrasData.RobeType.ALTER && robeType != PixelExtrasData.RobeType.STRIKE && robeType != PixelExtrasData.RobeType.ASHEN) {
            if (robeType == PixelExtrasData.RobeType.DROWNED) {
               ((GenericSmdModel)HWEEN_ROBE.getModel()).renderModel(1.0F);
            } else if (robeType == PixelExtrasData.RobeType.WINTER_CLOAK) {
               ((GenericSmdModel)ABOMASNOW_CLOAK.getModel()).renderModel(1.0F);
            } else if (robeType == PixelExtrasData.RobeType.FROSLASS_YUKATA) {
               ((GenericSmdModel)YUKATA_ROBE.getModel()).renderModel(1.0F);
            }
         } else {
            ((GenericSmdModel)WIZARD_ROBE.getModel()).renderModel(1.0F);
         }

         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         if (player.func_70093_af()) {
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         this.renderArm(robeType, EnumArm.Right);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         if (player.func_70093_af()) {
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         this.renderArm(robeType, EnumArm.Left);
         GlStateManager.func_179121_F();
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }
   }

   public boolean func_177142_b() {
      return true;
   }

   private void bindTexture(PixelExtrasData.RobeType type) {
      if (type == PixelExtrasData.RobeType.WIZARD) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(WIZARD_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.DROWNED) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(HWEEN_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.ALTER) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(ALTER_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.STRIKE) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(STRIKE_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.ASHEN) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(ASHEN_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.FROSLASS_YUKATA) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(FROSLASS_YUKATA_TEXTURE);
      } else if (type == PixelExtrasData.RobeType.WINTER_CLOAK) {
         Minecraft.func_71410_x().func_175598_ae().field_78724_e.func_110577_a(ABOMASNOW_CLOAK_TEXTURE);
      }

   }

   private void renderArm(PixelExtrasData.RobeType type, EnumArm arm) {
      if (type != PixelExtrasData.RobeType.WIZARD && type != PixelExtrasData.RobeType.ALTER && type != PixelExtrasData.RobeType.STRIKE && type != PixelExtrasData.RobeType.ASHEN) {
         if (type == PixelExtrasData.RobeType.DROWNED) {
            if (arm == EnumArm.Right) {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.RIGHT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.rightArm.field_82906_o, this.rightArm.field_82908_p, this.rightArm.field_82907_q);
               GlStateManager.func_179137_b(0.1, 0.0, 0.0);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)HWEEN_ROBE_RIGHT.getModel()).renderModel(1.0F);
            } else {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.LEFT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.leftArm.field_82906_o, this.leftArm.field_82908_p, this.leftArm.field_82907_q);
               GlStateManager.func_179137_b(-0.1, 0.0, 0.0);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)HWEEN_ROBE_LEFT.getModel()).renderModel(1.0F);
            }
         } else if (type == PixelExtrasData.RobeType.WINTER_CLOAK) {
            if (arm == EnumArm.Right) {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.RIGHT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.rightArm.field_82906_o, this.rightArm.field_82908_p, this.rightArm.field_82907_q);
               GlStateManager.func_179137_b(0.05, 0.06, 0.0);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)ABOMASNOW_CLOAK_ARM.getModel()).renderModel(1.0F);
            } else {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.LEFT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.leftArm.field_82906_o, this.leftArm.field_82908_p, this.leftArm.field_82907_q);
               GlStateManager.func_179137_b(-0.09, 0.04, 0.0);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)ABOMASNOW_CLOAK_ARM.getModel()).renderModel(1.0F);
            }
         } else if (type == PixelExtrasData.RobeType.FROSLASS_YUKATA) {
            if (arm == EnumArm.Right) {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.RIGHT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.rightArm.field_82906_o, this.rightArm.field_82908_p, this.rightArm.field_82907_q);
               GlStateManager.func_179137_b(0.05, 0.06, 0.0);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)YUKATA_RIGHT_ARM.getModel()).renderModel(1.0F);
            } else {
               this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.LEFT);
               GlStateManager.func_179139_a(0.43, 0.43, 0.46);
               GlStateManager.func_179109_b(this.leftArm.field_82906_o, this.leftArm.field_82908_p, this.leftArm.field_82907_q);
               GlStateManager.func_179137_b(-0.09, 0.06, 0.0);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               ((GenericSmdModel)YUKATA_LEFT_ARM.getModel()).renderModel(1.0F);
            }
         }
      } else {
         if (arm == EnumArm.Right) {
            this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.RIGHT);
            GlStateManager.func_179139_a(0.43, 0.43, 0.46);
            GlStateManager.func_179109_b(this.rightArm.field_82906_o, this.rightArm.field_82908_p, this.rightArm.field_82907_q);
         } else {
            this.renderer.func_177087_b().func_187073_a(0.08F, EnumHandSide.LEFT);
            GlStateManager.func_179139_a(0.43, 0.43, 0.46);
            GlStateManager.func_179109_b(this.leftArm.field_82906_o, this.leftArm.field_82908_p, this.leftArm.field_82907_q);
            GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179091_B();
         ((GenericSmdModel)WIZARD_ROBE_ARM.getModel()).renderModel(1.0F);
      }

   }
}
