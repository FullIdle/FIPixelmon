package com.pixelmonmod.tcg.listener;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.personalization.CardBack;
import com.pixelmonmod.tcg.api.card.personalization.Coin;
import com.pixelmonmod.tcg.api.registries.CardBackRegistry;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.api.registries.CoinRegistry;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.gui.GuiTCGCraftingMenu;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.item.ItemCard;
import com.pixelmonmod.tcg.item.ItemCardBack;
import com.pixelmonmod.tcg.item.ItemCoin;
import com.pixelmonmod.tcg.proxy.TCGClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {
   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onItemTooltip(ItemTooltipEvent event) {
      if (!event.getItemStack().func_190926_b()) {
         ItemStack itemStack = event.getItemStack();
         if (event.getEntityPlayer() != null && event.getEntityPlayer().field_70170_p != null && !FMLCommonHandler.instance().getSide().isServer()) {
            Minecraft mc = Minecraft.func_71410_x();
            ScaledResolution res;
            int x;
            if (itemStack.func_77973_b() instanceof ItemCard && mc.field_71462_r != null) {
               if (itemStack.func_77978_p() != null) {
                  ImmutableCard card = CardRegistry.fromNBT(itemStack.func_77978_p());
                  if (card != null) {
                     res = new ScaledResolution(mc);
                     x = Mouse.getX() / res.func_78325_e() - mc.field_71462_r.field_146294_l / 2 - 65;
                     GL11.glPushMatrix();
                     GL11.glTranslatef((float)x, 0.0F, 1000.0F);
                     CommonCardState state = new CommonCardState(card);
                     CardHelper.drawCard(state, mc, mc.field_71462_r.field_146294_l, mc.field_71462_r.field_146295_m, 0.0F, 1.0F, 0.0, (PlayerClientMyState)null, (PlayerClientOpponentState)null);
                     GL11.glEnable(2929);
                     GL11.glEnable(2896);
                     GL11.glDepthMask(true);
                     GL11.glPopMatrix();
                     mc.field_71466_p.func_78264_a(false);
                  }
               }
            } else if (itemStack.func_77973_b() instanceof ItemCardBack && mc.field_71462_r != null) {
               if (itemStack.func_77978_p() != null) {
                  CardBack cardBack = CardBackRegistry.fromNBT(itemStack.func_77978_p());
                  if (cardBack != null) {
                     res = new ScaledResolution(mc);
                     x = Mouse.getX() / res.func_78325_e() - mc.field_71462_r.field_146294_l / 2 - 65;
                     GL11.glPushMatrix();
                     GL11.glTranslatef((float)x, 0.0F, 1000.0F);
                     CardHelper.drawCardBack(mc, mc.field_71462_r.field_146294_l, mc.field_71462_r.field_146295_m, 0.0F, cardBack);
                     GL11.glEnable(2929);
                     GL11.glEnable(2896);
                     GL11.glDepthMask(true);
                     GL11.glPopMatrix();
                     mc.field_71466_p.func_78264_a(false);
                  }
               }
            } else if (itemStack.func_77973_b() instanceof ItemCoin && mc.field_71462_r != null && itemStack.func_77978_p() != null) {
               try {
                  Coin coin = CoinRegistry.fromNBT(itemStack.func_77978_p());
                  res = new ScaledResolution(mc);
                  x = Mouse.getX() / res.func_78325_e() - mc.field_71462_r.field_146294_l / 2 - 40;
                  GL11.glPushMatrix();
                  GL11.glTranslatef((float)x, 0.0F, 1000.0F);
                  GlStateManager.func_179140_f();
                  mc.field_71446_o.func_110577_a(coin.getHeads());
                  GuiHelper.drawImageQuad((double)(mc.field_71462_r.field_146294_l / 2 - 10), (double)(mc.field_71462_r.field_146295_m / 2 - 10), 40.0, 40.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
                  GlStateManager.func_179145_e();
                  GL11.glEnable(2929);
                  GL11.glEnable(2896);
                  GL11.glDepthMask(true);
                  GL11.glPopMatrix();
                  mc.field_71466_p.func_78264_a(false);
               } catch (NullPointerException var8) {
                  var8.printStackTrace();
               }
            }

         }
      }
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onItemFrameRender(RenderItemInFrameEvent event) {
      if (event.getItem().func_77973_b() == TCG.itemCard) {
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onKeyInput(InputEvent.KeyInputEvent event) {
      if (TCGClientProxy.openTCGCraftingMenu.func_151468_f() && TCGConfig.getInstance().essenceForPacks) {
         Minecraft.func_71410_x().func_147108_a(new GuiTCGCraftingMenu());
      }

   }
}
