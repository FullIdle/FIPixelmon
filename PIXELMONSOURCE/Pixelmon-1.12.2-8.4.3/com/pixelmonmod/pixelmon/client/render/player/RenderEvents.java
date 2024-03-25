package com.pixelmonmod.pixelmon.client.render.player;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.render.custom.FontRendererPixelmon;
import com.pixelmonmod.pixelmon.client.render.layers.LayerEquippables;
import com.pixelmonmod.pixelmon.comm.packetHandlers.UIKeyPressPacket;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.enums.TextJustification;
import com.pixelmonmod.pixelmon.items.IEquippable;
import com.pixelmonmod.pixelmon.items.ItemRelicCrown;
import com.pixelmonmod.pixelmon.items.ItemUIElement;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class RenderEvents {
   private ModelPlayer oldModel = null;
   private ModelBiped oldModelArmor = null;
   private ModelBiped oldModelLeggings = null;
   private static final float[] WHITE = new float[]{1.0F, 1.0F, 1.0F, 1.0F};

   @SubscribeEvent
   public void onRenderTooltip(RenderTooltipEvent.Pre event) {
      event.setFontRenderer(FontRendererPixelmon.getInstance());
      if (event.getStack().func_77973_b() instanceof ItemUIElement && event.getStack().func_82833_r().isEmpty()) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void onKeyPress(GuiScreenEvent.KeyboardInputEvent.Pre event) {
      if (event.getGui() instanceof GuiContainer) {
         GuiContainer container = (GuiContainer)event.getGui();
         Iterator var3 = container.field_147002_h.field_75151_b.iterator();

         while(var3.hasNext()) {
            Slot slot = (Slot)var3.next();
            if (slot.func_75216_d()) {
               ItemStack stack = slot.func_75211_c();
               if (stack.func_77973_b() instanceof ItemUIElement) {
                  int code = Keyboard.getEventKey();
                  char key = Keyboard.getEventCharacter();
                  Pixelmon.network.sendToServer(new UIKeyPressPacket(code, key));
                  if (Minecraft.func_71410_x().field_71474_y.field_151445_Q.func_151463_i() == code) {
                     event.setCanceled(true);
                  }
                  break;
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onRenderContainer(GuiContainerEvent.DrawForeground event) {
      Iterator var2 = event.getGuiContainer().field_147002_h.field_75151_b.iterator();

      while(var2.hasNext()) {
         Slot slot = (Slot)var2.next();
         if (slot.func_75216_d()) {
            ItemStack stack = slot.func_75211_c();
            if (stack.func_77973_b() instanceof ItemUIElement && !ItemUIElement.isBakedModel(stack)) {
               boolean hover = event.getGuiContainer().getSlotUnderMouse() == slot;
               int x = (Integer)ItemUIElement.getXOverride(stack).orElse(slot.field_75223_e - 1) + (Integer)ItemUIElement.getXOffset(stack).orElse(0);
               int y = (Integer)ItemUIElement.getYOverride(stack).orElse(slot.field_75221_f - 1) + (Integer)ItemUIElement.getYOffset(stack).orElse(0);
               int z = (Integer)ItemUIElement.getZLevel(stack).orElse(0);
               float[] rgba = (float[])ItemUIElement.getColor(stack, hover).orElse(WHITE);
               GlStateManager.func_179094_E();
               GlStateManager.func_179147_l();
               GlStateManager.func_179141_d();
               GlStateManager.func_179140_f();
               GlStateManager.func_179112_b(770, 771);
               GlStateManager.func_179131_c(rgba[0], rgba[1], rgba[2], rgba[3]);
               GlStateManager.func_179109_b(0.0F, 0.0F, (float)z);
               ItemUIElement.getImage(stack, hover).ifPresent((rl) -> {
                  int w = (Integer)ItemUIElement.getImageWidth(stack).orElse(18);
                  int h = (Integer)ItemUIElement.getImageHeight(stack).orElse(18);
                  float tw = (Float)ItemUIElement.getTextureWidth(stack).orElse(18.0F);
                  float th = (Float)ItemUIElement.getTextureHeight(stack).orElse(18.0F);
                  float u = (Float)ItemUIElement.getImageU(stack).orElse(0.0F);
                  float v = (Float)ItemUIElement.getImageV(stack).orElse(0.0F);
                  Minecraft.func_71410_x().field_71446_o.func_110577_a(rl);
                  Gui.func_146110_a(x, y, u, v, w, h, tw, th);
               });
               ItemUIElement.getText(stack, hover).ifPresent((text) -> {
                  TextJustification j = ItemUIElement.getTextJustification(stack);
                  float s = (float)ItemUIElement.getTextScale(stack);
                  switch (j) {
                     case RIGHT:
                        GuiHelper.drawScaledString(text, (float)x, (float)y, (new Color(rgba[0], rgba[1], rgba[2], rgba[3])).getRGB(), s);
                        break;
                     case CENTER:
                        GuiHelper.drawScaledCenteredString(text, (float)x, (float)y, (new Color(rgba[0], rgba[1], rgba[2], rgba[3])).getRGB(), s);
                        break;
                     case LEFT:
                        GuiHelper.drawScaledStringRightAligned(text, (float)x, (float)y, (new Color(rgba[0], rgba[1], rgba[2], rgba[3])).getRGB(), false, s);
                  }

               });
               GlStateManager.func_179145_e();
               GlStateManager.func_179121_F();
            }
         }
      }

   }

   @SubscribeEvent
   public void onRenderArmorStand(RenderLivingEvent.Pre event) {
      if (event.getRenderer() instanceof RenderArmorStand && LayerEquippables.hasEquippables(event.getEntity())) {
         ListIterator i = event.getRenderer().field_177097_h.listIterator();
         this.renderBlockOnArmorStand(event, event.getEntity());

         while(i.hasNext()) {
            LayerRenderer layer = (LayerRenderer)i.next();
            if (layer instanceof LayerCustomHead) {
               i.remove();
            }
         }
      }

   }

   private void renderBlockOnArmorStand(RenderLivingEvent.Pre event, EntityLivingBase entity) {
      Entity view = Minecraft.func_71410_x().func_175606_aa();
      if (view != null) {
         HashSet equippables = new HashSet();
         Iterator var5 = entity.func_184193_aE().iterator();

         while(var5.hasNext()) {
            ItemStack stack = (ItemStack)var5.next();
            if (!stack.func_190926_b() && stack.func_77973_b() instanceof IEquippable) {
               equippables.add((IEquippable)stack.func_77973_b());
            }
         }

         if (!equippables.isEmpty()) {
            var5 = equippables.iterator();

            while(var5.hasNext()) {
               IEquippable equippable = (IEquippable)var5.next();
               if (equippable.getEquippableItem() instanceof ItemRelicCrown && !entity.func_184613_cA() && entity.func_184187_bx() == null) {
                  Minecraft.func_71410_x().field_71446_o.func_110577_a(TextureMap.field_110575_b);
                  GlStateManager.func_179094_E();
                  GlStateManager.func_179140_f();
                  Tessellator tessellator = Tessellator.func_178181_a();
                  BufferBuilder bufferbuilder = tessellator.func_178180_c();
                  IBlockState state = PixelmonBlocks.relicCrown.func_176223_P();
                  bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_176600_a);
                  BlockPos blockpos = entity.func_180425_c().func_177982_a(0, 2, 0);
                  float yaw = entity.field_70758_at + (entity.field_70759_as - entity.field_70758_at) * event.getPartialRenderTick();
                  float pitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * event.getPartialRenderTick();
                  double r = 0.335;
                  if (entity.func_70093_af()) {
                     GlStateManager.func_179109_b(0.0F, -0.38F, 0.0F);
                  }

                  double theta = (double)pitch * Math.PI / 180.0;
                  double x = 2.0 * r * Math.sin(theta / 2.0);
                  double gamma = 90.0 - (180.0 - theta) / 2.0;
                  double a = x * Math.sin(gamma);
                  double b = x * Math.cos(gamma);
                  GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
                  GlStateManager.func_179137_b(0.0, 1.75, 0.0);
                  GlStateManager.func_179114_b(-yaw, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179114_b(pitch, 1.0F, 0.0F, 0.0F);
                  GlStateManager.func_179137_b(-0.5, 0.0, -0.5);
                  GlStateManager.func_179137_b(0.0, a, b);
                  GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
                  double viewX = view.field_70169_q + (view.field_70165_t - view.field_70169_q) * (double)event.getPartialRenderTick();
                  double viewY = view.field_70167_r + (view.field_70163_u - view.field_70167_r) * (double)event.getPartialRenderTick();
                  double viewZ = view.field_70166_s + (view.field_70161_v - view.field_70166_s) * (double)event.getPartialRenderTick();
                  double playerX = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)event.getPartialRenderTick();
                  double playerY = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)event.getPartialRenderTick();
                  double playerZ = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)event.getPartialRenderTick();
                  GlStateManager.func_179137_b(viewX - playerX, viewY - playerY, viewZ - playerZ);
                  BlockRendererDispatcher blockrendererdispatcher = Minecraft.func_71410_x().func_175602_ab();
                  blockrendererdispatcher.func_175019_b().func_187493_a(Minecraft.func_71410_x().field_71441_e, blockrendererdispatcher.func_184389_a(state), state, BlockPos.field_177992_a, bufferbuilder, false, MathHelper.func_180186_a(blockpos));
                  tessellator.func_78381_a();
                  GlStateManager.func_179145_e();
                  GlStateManager.func_179121_F();
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onRenderPlayer(RenderPlayerEvent.Pre event) {
      EntityPlayer player = event.getEntityPlayer();
      if (event.getRenderer() instanceof PixelRenderPlayer) {
         PixelRenderPlayer renderPlayer = (PixelRenderPlayer)event.getRenderer();
         this.renderBlockOnPlayer(event, event.getEntityPlayer());
         ListIterator i;
         LayerRenderer layer;
         if (LayerEquippables.hasEquippables(player)) {
            i = event.getRenderer().field_177097_h.listIterator();

            while(i.hasNext()) {
               layer = (LayerRenderer)i.next();
               if (layer instanceof LayerCustomHead) {
                  i.remove();
               }
            }
         }

         if (player.func_184187_bx() instanceof EntityBike) {
            this.oldModel = renderPlayer.func_177087_b();
            renderPlayer.setModel(new ModelPlayerOnBike(0.0F, renderPlayer.isAlex));
            i = event.getRenderer().field_177097_h.listIterator();

            while(true) {
               while(i.hasNext()) {
                  layer = (LayerRenderer)i.next();
                  if (layer instanceof LayerArmorBase && (LayerArmorBase)layer instanceof LayerBipedArmor) {
                     LayerBipedArmor layerArmor = (LayerBipedArmor)((LayerArmorBase)layer);
                     this.oldModelArmor = (ModelBiped)layerArmor.field_177186_d;
                     this.oldModelLeggings = (ModelBiped)layerArmor.field_177189_c;
                     layerArmor.field_177186_d = new ModelBikeArmor(1.0F);
                     layerArmor.field_177189_c = new ModelBikeArmor(0.5F);
                  } else if (LayerElytra.class.isInstance(layer)) {
                     i.set((new LayerWrapper(layer)).setTranslates(0.0, 0.03, -0.25));
                  } else if (LayerCustomHead.class.isInstance(layer)) {
                     i.set((new LayerWrapper(new LayerCustomHead(renderPlayer.func_177087_b().field_78116_c))).setTranslates(0.0, 0.05, -0.20000000298023224));
                  } else if (LayerArrow.class.isInstance(layer) || LayerEntityOnShoulder.class.isInstance(layer) || LayerHeldItem.class.isInstance(layer)) {
                     i.set((new LayerWrapper(layer)).setEnabled(false));
                  }
               }

               return;
            }
         }
      }

   }

   private void renderBlockOnPlayer(RenderPlayerEvent event, EntityPlayer player) {
      Entity view = Minecraft.func_71410_x().func_175606_aa();
      if (view != null) {
         HashSet equippables = new HashSet();
         Iterator var5 = player.func_184193_aE().iterator();

         while(var5.hasNext()) {
            ItemStack stack = (ItemStack)var5.next();
            if (!stack.func_190926_b() && stack.func_77973_b() instanceof IEquippable) {
               equippables.add((IEquippable)stack.func_77973_b());
            }
         }

         if (!equippables.isEmpty()) {
            var5 = equippables.iterator();

            while(var5.hasNext()) {
               IEquippable equippable = (IEquippable)var5.next();
               if (equippable.getEquippableItem() instanceof ItemRelicCrown && !player.func_184613_cA() && player.func_184187_bx() == null) {
                  Minecraft.func_71410_x().field_71446_o.func_110577_a(TextureMap.field_110575_b);
                  GlStateManager.func_179094_E();
                  GlStateManager.func_179140_f();
                  Tessellator tessellator = Tessellator.func_178181_a();
                  BufferBuilder bufferbuilder = tessellator.func_178180_c();
                  IBlockState state = PixelmonBlocks.relicCrown.func_176223_P();
                  bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_176600_a);
                  BlockPos blockpos = player.func_180425_c().func_177982_a(0, 2, 0);
                  float yaw = player.field_70758_at + (player.field_70759_as - player.field_70758_at) * event.getPartialRenderTick();
                  float pitch = player.field_70127_C + (player.field_70125_A - player.field_70127_C) * event.getPartialRenderTick();
                  double r = 0.335;
                  if (player.func_70093_af()) {
                     GlStateManager.func_179109_b(0.0F, -0.38F, 0.0F);
                  }

                  double theta = (double)pitch * Math.PI / 180.0;
                  double x = 2.0 * r * Math.sin(theta / 2.0);
                  double gamma = 90.0 - (180.0 - theta) / 2.0;
                  double a = x * Math.sin(gamma);
                  double b = x * Math.cos(gamma);
                  GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
                  GlStateManager.func_179137_b(0.0, 1.75, 0.0);
                  GlStateManager.func_179114_b(-yaw, 0.0F, 1.0F, 0.0F);
                  GlStateManager.func_179114_b(pitch, 1.0F, 0.0F, 0.0F);
                  GlStateManager.func_179137_b(-0.5, 0.0, -0.5);
                  GlStateManager.func_179137_b(0.0, a, b);
                  GlStateManager.func_179137_b(event.getX(), event.getY(), event.getZ());
                  double viewX = view.field_70169_q + (view.field_70165_t - view.field_70169_q) * (double)event.getPartialRenderTick();
                  double viewY = view.field_70167_r + (view.field_70163_u - view.field_70167_r) * (double)event.getPartialRenderTick();
                  double viewZ = view.field_70166_s + (view.field_70161_v - view.field_70166_s) * (double)event.getPartialRenderTick();
                  double playerX = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)event.getPartialRenderTick();
                  double playerY = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)event.getPartialRenderTick();
                  double playerZ = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)event.getPartialRenderTick();
                  GlStateManager.func_179137_b(viewX - playerX, viewY - playerY, viewZ - playerZ);
                  BlockRendererDispatcher blockrendererdispatcher = Minecraft.func_71410_x().func_175602_ab();
                  blockrendererdispatcher.func_175019_b().func_187493_a(Minecraft.func_71410_x().field_71441_e, blockrendererdispatcher.func_184389_a(state), state, BlockPos.field_177992_a, bufferbuilder, false, MathHelper.func_180186_a(blockpos));
                  tessellator.func_78381_a();
                  GlStateManager.func_179145_e();
                  GlStateManager.func_179121_F();
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void onRenderPlayer(RenderPlayerEvent.Post event) {
      boolean hasCustomHead = false;
      ListIterator i2 = event.getRenderer().field_177097_h.listIterator();

      while(i2.hasNext()) {
         LayerRenderer layer = (LayerRenderer)i2.next();
         if (layer instanceof LayerCustomHead) {
            hasCustomHead = true;
            break;
         }
      }

      if (!hasCustomHead) {
         event.getRenderer().func_177094_a(new LayerCustomHead(event.getRenderer().func_177087_b().field_78116_c));
      }

      if (this.oldModel != null) {
         if (event.getRenderer() instanceof PixelRenderPlayer) {
            PixelRenderPlayer renderPlayer = (PixelRenderPlayer)event.getRenderer();
            renderPlayer.setModel(this.oldModel);
            ListIterator i = event.getRenderer().field_177097_h.listIterator();

            while(true) {
               while(i.hasNext()) {
                  LayerRenderer layer = (LayerRenderer)i.next();
                  if (this.oldModelArmor != null && layer instanceof LayerArmorBase && (LayerArmorBase)layer instanceof LayerBipedArmor) {
                     LayerBipedArmor layerArmor = (LayerBipedArmor)((LayerArmorBase)layer);
                     layerArmor.field_177186_d = this.oldModelArmor;
                     layerArmor.field_177189_c = this.oldModelLeggings;
                  } else if (layer instanceof LayerWrapper) {
                     LayerRenderer renderer = ((LayerWrapper)layer).renderer;
                     if (renderer instanceof LayerCustomHead) {
                        LayerRenderer customHead = new LayerCustomHead(renderPlayer.func_177087_b().field_78116_c);
                        i.set(customHead);
                     } else {
                        i.set(renderer);
                     }
                  }
               }

               this.oldModelArmor = null;
               this.oldModelLeggings = null;
               break;
            }
         }

         this.oldModel = null;
      }

   }
}
