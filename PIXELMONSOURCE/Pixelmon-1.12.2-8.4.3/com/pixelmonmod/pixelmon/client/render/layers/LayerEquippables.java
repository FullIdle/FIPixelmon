package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.items.IEquippable;
import com.pixelmonmod.pixelmon.items.ItemBadge;
import com.pixelmonmod.pixelmon.items.ItemBadgeCase;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.items.ItemSymbol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerEquippables implements LayerRenderer {
   private final RenderPlayer renderer;
   private final Map equippableModels = new HashMap();
   private static final float[][] bcTranslations = new float[][]{{-0.225F, -0.085F}, {-0.225F, -0.11F}, {-0.225F, -0.145F}, {-0.225F, -0.175F}};
   private static final double[][] bcPositions = new double[][]{{0.08, 0.13}, {0.15, 0.13}, {0.08, 0.2}, {0.15, 0.2}, {0.08, 0.27}, {0.15, 0.27}, {0.08, 0.34}, {0.15, 0.34}};

   public LayerEquippables(RenderPlayer renderer) {
      this.renderer = renderer;
      this.equippableModels.put("safety_goggles", new GenericModelHolder("playeritems/equippables/safety_goggles.pqc"));
   }

   public static boolean hasEquippables(EntityLivingBase entity) {
      Iterator var1 = entity.func_184193_aE().iterator();

      ItemStack stack;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         stack = (ItemStack)var1.next();
      } while(stack.func_190926_b() || !(stack.func_77973_b() instanceof IEquippable));

      return true;
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      ModelRenderer left = this.renderer.func_177087_b().field_178724_i;
      ModelRenderer body = this.renderer.func_177087_b().field_78115_e;
      ModelRenderer head = this.renderer.func_177087_b().field_78116_c;
      HashSet equippables = new HashSet();
      Iterator var13 = player.func_184193_aE().iterator();

      while(var13.hasNext()) {
         ItemStack stack = (ItemStack)var13.next();
         if (!stack.func_190926_b() && stack.func_77973_b() instanceof IEquippable) {
            equippables.add((IEquippable)stack.func_77973_b());
         }
      }

      if (!equippables.isEmpty()) {
         Minecraft mc = Minecraft.func_71410_x();
         mc.field_71424_I.func_76320_a("layer_equippables");

         for(Iterator var25 = equippables.iterator(); var25.hasNext(); GlStateManager.func_179121_F()) {
            IEquippable equippable = (IEquippable)var25.next();
            GlStateManager.func_179094_E();
            Item item = equippable.getEquippableItem();
            if (item == PixelmonItemsHeld.safetyGoggles) {
               GenericModelHolder model = (GenericModelHolder)this.equippableModels.get(equippable.getEquippableModelKey());
               ResourceLocation texture = equippable.getEquippableTexture();
               mc.field_71446_o.func_110577_a(texture);
               if (player.func_70093_af()) {
                  GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
               }

               head.func_78794_c(1.0F);
               GlStateManager.func_179109_b(head.field_82906_o, head.field_82908_p, head.field_82907_q);
               GlStateManager.func_179109_b(0.0F, 1.54F, 0.0F);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179091_B();
               GlStateManager.func_179109_b(0.0F, -1.82F, -0.15F);
               GlStateManager.func_179139_a(0.62, 0.62, 0.62);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               model.render();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            } else if (!(item instanceof ItemBadge) && !(item instanceof ItemSymbol)) {
               ItemStack stack;
               if (!(item instanceof ItemBadgeCase)) {
                  if (item instanceof ItemPixelmonSprite) {
                     stack = player.func_184582_a(EntityEquipmentSlot.HEAD);
                     if (stack.func_77942_o() && stack.func_77978_p().func_74765_d("ndex") == EnumSpecies.Joltik.getNationalPokedexInteger()) {
                        ModelBase model = PixelmonModelRegistry.getModel(EnumSpecies.Joltik, EnumNoForm.NoForm);
                        ResourceLocation texture = Entity2Client.getTextureFor(EnumSpecies.Joltik, EnumNoForm.NoForm, Gender.Male, "", stack.func_77978_p().func_74767_n("Shiny"));
                        if (model != null && texture != null) {
                           mc.field_71446_o.func_110577_a(texture);
                           if (player.func_70093_af()) {
                              GlStateManager.func_179109_b(0.0F, -0.73F, 0.0F);
                           }

                           head.func_78794_c(1.0F);
                           GlStateManager.func_179109_b(head.field_82906_o, head.field_82908_p, head.field_82907_q);
                           GlStateManager.func_179109_b(0.0F, -1.2F, -0.07F);
                           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                           GlStateManager.func_179091_B();
                           double sf = 0.03;
                           GlStateManager.func_179139_a(sf, sf, sf);
                           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                           model.func_78088_a((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 1.0F);
                           GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                        }
                     }
                  }
               } else {
                  stack = player.func_184582_a(EntityEquipmentSlot.CHEST);
                  if (stack.func_77942_o()) {
                     ItemBadgeCase.BadgeCase badgeCase = ItemBadgeCase.BadgeCase.readFromItemStack(stack);
                     if (badgeCase != null) {
                        GlStateManager.func_179140_f();
                        int i = 0;

                        for(Iterator var20 = badgeCase.badges.iterator(); var20.hasNext(); ++i) {
                           ItemStack badge = (ItemStack)var20.next();
                           if (i >= 8) {
                              break;
                           }

                           GlStateManager.func_179094_E();
                           double[] pos = bcPositions[i];
                           GlStateManager.func_179137_b(pos[0], pos[1], -0.11);
                           GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
                           GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                           if (player.func_70093_af()) {
                              float[] translation = bcTranslations[i / 2];
                              GlStateManager.func_179109_b(0.0F, translation[0], translation[1]);
                              GlStateManager.func_179114_b((float)((double)(body.field_78795_f * 180.0F) / Math.PI), 1.0F, 0.0F, 0.0F);
                           }

                           float s = 0.075F;
                           GlStateManager.func_179152_a(s, s, 1.0F);
                           Minecraft.func_71410_x().func_175597_ag().func_178099_a(player, badge, TransformType.NONE);
                           GlStateManager.func_179121_F();
                        }

                        GlStateManager.func_179145_e();
                     }
                  }
               }
            } else {
               GlStateManager.func_179140_f();
               GlStateManager.func_179137_b(0.1, 0.2, -0.11);
               GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
               if (player.func_70093_af()) {
                  GlStateManager.func_179109_b(0.0F, -0.22F, -0.11F);
                  GlStateManager.func_179114_b((float)((double)(body.field_78795_f * 180.0F) / Math.PI), 1.0F, 0.0F, 0.0F);
               }

               float s = 0.15F;
               GlStateManager.func_179152_a(s, s, 1.0F);
               Minecraft.func_71410_x().func_175597_ag().func_178099_a(player, new ItemStack(equippable.getEquippableItem()), TransformType.NONE);
               GlStateManager.func_179145_e();
            }
         }

         mc.field_71424_I.func_76319_b();
      }

   }

   public boolean func_177142_b() {
      return true;
   }
}
