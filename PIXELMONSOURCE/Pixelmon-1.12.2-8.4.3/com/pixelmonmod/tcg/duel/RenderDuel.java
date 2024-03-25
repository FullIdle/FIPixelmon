package com.pixelmonmod.tcg.duel;

import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GamePhase;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.RenderState;
import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class RenderDuel {
   private static EntityPixelmon[] activePokemons = new EntityPixelmon[2];

   @SideOnly(Side.CLIENT)
   public static void drawStraightLine(double fromX, double fromY, double fromZ, double toX, double toY, double toZ, int r, int g, int b, float w) {
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f((float)r, (float)g, (float)b, 1.0F);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glLineWidth(w);
      int bright = 240;
      int brightX = bright % 65536;
      int brightY = bright / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)brightX, (float)brightY);
      GL11.glBegin(3);
      GL11.glVertex3d(fromX, fromY, fromZ);
      GL11.glVertex3d(toX, toY, toZ);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public static void draw2DLine(double fromX, double fromY, double toX, double toY, float r, float g, float b, float w) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glColor4f(r, g, b, 1.0F);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glLineWidth(w);
      int bright = 240;
      int brightX = bright % 65536;
      int brightY = bright / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)brightX, (float)brightY);
      GL11.glBegin(3);
      GL11.glVertex2d(fromX, fromY);
      GL11.glVertex2d(toX, toY);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public static void draw2DRectangle(double x1, double x2, double y1, double y2, float r, float g, float b, float a) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glColor4f(r, g, b, a);
      GL11.glBegin(5);
      GL11.glVertex2d(x1, y1);
      GL11.glVertex2d(x2, y1);
      GL11.glVertex2d(x1, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glVertex2d(x1, y1);
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public static void drawCardOutline(double x, double y, double z, boolean isHorizontal, int r, int g, int b) {
      y += 0.01;
      if (isHorizontal) {
         drawStraightLine(x, y, z, x, y, z - 2.0, r, g, b, 2.0F);
         drawStraightLine(x, y, z, x + 1.5, y, z, r, g, b, 2.0F);
         drawStraightLine(x + 1.5, y, z, x + 1.5, y, z - 2.0, r, g, b, 2.0F);
         drawStraightLine(x, y, z - 2.0, x + 1.5, y, z - 2.0, r, g, b, 2.0F);
      } else {
         drawStraightLine(x, y, z, x, y, z - 1.5, r, g, b, 2.0F);
         drawStraightLine(x, y, z, x + 2.0, y, z, r, g, b, 2.0F);
         drawStraightLine(x + 2.0, y, z, x + 2.0, y, z - 1.5, r, g, b, 2.0F);
         drawStraightLine(x, y, z - 1.5, x + 2.0, y, z - 1.5, r, g, b, 2.0F);
      }

   }

   @SideOnly(Side.CLIENT)
   public static void drawPrizeOutline(double x, double y, double z, int r, int g, int b) {
      y += 0.01;
      drawStraightLine(x, y, z, x, y, z - 1.5, r, g, b, 2.0F);
      drawStraightLine(x, y, z, x + 1.125, y, z, r, g, b, 2.0F);
      drawStraightLine(x + 1.125, y, z, x + 1.125, y, z - 1.5, r, g, b, 2.0F);
      drawStraightLine(x, y, z - 1.5, x + 1.125, y, z - 1.5, r, g, b, 2.0F);
   }

   @SideOnly(Side.CLIENT)
   public static void drawBoardOutline(TileEntityBattleController controller, double x, double y, double z, float partialTicks, FontRenderer f) {
      if (controller.getRender() != null) {
         GL11.glPushMatrix();
         GL11.glScaled((double)controller.getScale(), (double)controller.getScale(), (double)controller.getScale());
         RenderState render = controller.getRender();
         y += 0.01;
         int r = 255;
         int g = 255;
         int b = 255;
         if (controller.isShadowGame()) {
            r = 103;
            g = 0;
            b = 168;
         }

         drawStraightLine(x - 7.0, y, z, x + 8.0, y, z, r, g, b, 2.0F);
         drawStraightLine(x - 7.0, y, z, x - 7.0, y, z - 10.5, r, g, b, 2.0F);
         drawStraightLine(x - 7.0, y, z - 10.5, x + 8.0, y, z - 10.5, r, g, b, 2.0F);
         drawStraightLine(x + 8.0, y, z - 10.5, x + 8.0, y, z, r, g, b, 2.0F);
         drawStraightLine(x + 0.5, y, z - 10.5, x + 0.5, y, z - 9.5, r, g, b, 2.0F);
         drawStraightLine(x + 0.5, y, z, x + 0.5, y, z - 7.5, r, g, b, 2.0F);
         y -= 0.01;

         int i;
         for(i = 0; i < 5; ++i) {
            drawCardOutline(x - 6.5, y, z - 0.5 - (double)(i * 2), false, r, g, b);
            drawCardOutline(x + 5.5, y, z - 0.5 - (double)(i * 2), false, r, g, b);
         }

         drawCardOutline(x - 2.5, y, z - 4.5, false, r, g, b);
         drawCardOutline(x - 6.5, y, z + 2.5, true, r, g, b);
         drawCardOutline(x + 1.5, y, z - 4.5, false, r, g, b);
         drawCardOutline(x + 4.0, y, z + 2.5, true, r, g, b);
         if (render.getPlayers() != null) {
            GameClientState client = controller.getClient();

            for(int playerIndex = 0; playerIndex < 2; ++playerIndex) {
               PlayerClientOpponentState player = render.getPlayers()[playerIndex];
               PlayerClientOpponentState opp = render.getPlayers()[(playerIndex + 1) % 2];
               if (player != null) {
                  int rotation = playerIndex * 180;
                  double xOffset = playerIndex == 0 ? 1.5 : -2.5;
                  double x1;
                  double z1;
                  if (player.getPlayerName() != null) {
                     x1 = x + xOffset;
                     z1 = y - 1.97;
                     double z0 = z - 4.5;
                     GL11.glPushMatrix();
                     RenderCard.startQuads();
                     RenderCard.handleRotation(x1, z1, z0, rotation, 1.0F);
                     f.func_78264_a(false);
                     RenderCard.drawUnlocalizedString(f, x1 - 5.0, z1, z0 - 0.5 - (double)((float)f.func_78256_a(player.getPlayerName()) / 2.0F / 16.0F), player.getPlayerName(), render.getGamePhase().after(GamePhase.PreMatch) && render.getCurrentTurn() == playerIndex ? '\udddd' : 16777215, false, 90, 4.0F, "");
                     RenderCard.endQuads();
                     GL11.glPopMatrix();
                  }

                  GL11.glPushMatrix();
                  Minecraft.func_71410_x().field_71446_o.func_110577_a(player.getCoin().getHeads());
                  RenderCard.startQuads();
                  x1 = x + (double)playerIndex == 0.0 ? 2.75 : -3.75;
                  z1 = z - 1.25;
                  GL11.glTranslated(0.0, 0.0, rotation > 0 ? -0.75 : 0.0);
                  RenderCard.handleRotation(x1, y, z1, rotation, 1.0F);
                  RenderCard.wr().func_181668_a(7, DefaultVertexFormats.field_181707_g);
                  RenderCard.wr().func_181662_b(x1, y, z1).func_187315_a(1.0, 1.0).func_181675_d();
                  RenderCard.wr().func_181662_b(x1 + 0.75, y, z1).func_187315_a(1.0, 0.0).func_181675_d();
                  RenderCard.wr().func_181662_b(x1 + 0.75, y, z1 - 0.75).func_187315_a(0.0, 0.0).func_181675_d();
                  RenderCard.wr().func_181662_b(x1, y, z1 - 0.75).func_187315_a(0.0, 1.0).func_181675_d();
                  RenderCard.te().func_78381_a();
                  RenderCard.endQuads();
                  GL11.glPopMatrix();
                  PokemonCardState activeCard = player.getActiveCard();
                  double x0;
                  double y0;
                  double z0;
                  if (activeCard != null) {
                     x0 = x + xOffset;
                     y0 = y + 0.1;
                     z0 = z - 4.5;
                     if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.ASLEEP;
                     })) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(x0 + 1.75, y0, z0 + 0.25);
                        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                        GL11.glTranslated(-x0, -y0, -z0);
                     } else if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.PARALYZED;
                     })) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(x0 + 0.25, y0, z0 - 1.75);
                        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                        GL11.glTranslated(-x0, -y0, -z0);
                     } else if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.CONFUSED;
                     })) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(x0 + 2.0, y0, z0 - 1.5);
                        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                        GL11.glTranslated(-x0, -y0, -z0);
                     }

                     Minecraft mc = Minecraft.func_71410_x();
                     if (client != null && client.getMe() != null && client.getMe().getPlayerName().equals(player.getPlayerName()) && client.getMe().getAvailableActions().isCanAttack() && client.isMyTurn()) {
                        drawCardOutline(x0, y0, z0, false, 0, 1, 0);
                     }

                     RenderCard.drawCardOrBase(x0, y0, z0, activeCard, rotation, f, partialTicks, player, opp);
                     drawDamageCounter(activeCard.getStatus().getDamage(), x0, y0, z0, rotation, f);
                     drawAttachments(playerIndex, activeCard, x0, y0, z0, rotation, partialTicks, f, player, opp);
                     if (activeCard.getData().getID() != ImmutableCard.FACE_DOWN_ID && (mc.field_71462_r == null || !(mc.field_71462_r instanceof GuiTCG))) {
                        drawPokemonModelOnBoard(x0, y0, z0, mc, partialTicks, activeCard, BoardLocation.Active, 0, playerIndex, controller.getScale());
                     }

                     if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.BURNT;
                     })) {
                        drawStatusCounter(CardCondition.BURNT, x0, y0, z0, rotation);
                     }

                     if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.POISONED;
                     })) {
                        drawStatusCounter(CardCondition.POISONED, x0, y0, z0, rotation);
                     }

                     if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.ASLEEP;
                     })) {
                        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glPopMatrix();
                     } else if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.PARALYZED;
                     })) {
                        GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glPopMatrix();
                     } else if (activeCard.getStatus().getConditions().stream().anyMatch((c) -> {
                        return c.getLeft() == CardCondition.CONFUSED;
                     })) {
                        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glPopMatrix();
                     }
                  }

                  int i;
                  for(i = 0; i < 5; ++i) {
                     int benchIndex = playerIndex == 1 ? 4 - i : i;
                     PokemonCardState benchCard = player.getBenchCards()[benchIndex];
                     if (benchCard != null) {
                        double x0 = x + (playerIndex == 0 ? 5.5 : -6.5);
                        double y0 = y + 0.1;
                        double z0 = z - 0.5 - (double)(i * 2);
                        RenderCard.drawCardOrBase(x0, y0, z0, benchCard, rotation, f, partialTicks, player, opp);
                        drawDamageCounter(benchCard.getStatus().getDamage(), x0, y0, z0, rotation, f);
                        drawAttachments(playerIndex, benchCard, x0, y0, z0, rotation, partialTicks, f, player, opp);
                     }
                  }

                  if (player.getTrainerCard() != null) {
                     RenderCard.drawCard(x + xOffset, y + 0.1, z - 1.5, player.getTrainerCard(), rotation, f, partialTicks, player, opp);
                  }

                  if (player.getPrizeCards() != null) {
                     for(i = 0; i < player.getPrizeCards().length; ++i) {
                        float xPrizeOffset = (playerIndex == 0 ? 1.625F : 7.375F) + 0.45F - (float)(i % 3 + 3) * 1.125F;
                        float zPrizeOffset = 7.0F + (float)(i / 3) * 1.5F;
                        if (player.getPrizeCards()[i] != null) {
                           RenderCard.startQuads();
                           RenderCard.handleRotation(x - (double)xPrizeOffset, y + 0.1, z - (double)zPrizeOffset, 90, 0.75F);
                           RenderCard.drawCardBase(x - (double)xPrizeOffset, y + 0.1, z - (double)zPrizeOffset, (CommonCardState)null, false, player, opp);
                           RenderCard.endQuads();
                        }
                     }
                  }

                  int i;
                  String s;
                  if (player.getDeckSize() >= 0) {
                     x0 = x;
                     y0 = y + 0.1;
                     z0 = z + 2.5;

                     for(i = 0; i < player.getDeckSize(); ++i) {
                        x0 = x + (playerIndex == 0 ? 4.0 : -4.5);
                        y0 = y + 0.02 * (double)i + 0.1;
                        RenderCard.drawCardBase(x0, y0, z0, (CommonCardState)null, true, player, opp);
                     }

                     RenderCard.startQuads();
                     RenderCard.handleRotation(x0, y0, z0, 270, 2.7F);
                     s = Integer.toString(player.getDeckSize());
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.253, y0 + 0.001 - 2.0, z0 + 0.075, s, 0, false, 90, 3.0F, "");
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.243, y0 + 0.003 - 2.0, z0 + 0.065, s, 15658734, false, 90, 3.0F, "");
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.785, y - 2.0, z0 + 0.125, "Deck", 16777215, false, 90, 1.0F, "");
                     RenderCard.endQuads();
                  }

                  if (player.getDiscardPile().size() > 0) {
                     x0 = x;
                     y0 = y + 0.1;
                     z0 = z + 2.5;

                     for(i = 0; i < player.getDiscardPile().size(); ++i) {
                        x0 = x + (playerIndex == 0 ? 6.0 : -6.5);
                        y0 = y + 0.02 * (double)i + 0.1;
                        RenderCard.drawCardBase(x0, y0, z0, (CommonCardState)null, true, player, opp);
                     }

                     if (player.getDiscardPile().size() == 0) {
                        x0 = x + (playerIndex == 0 ? 6.0 : -6.5);
                        y0 = y + 0.0 + 0.1;
                     }

                     RenderCard.startQuads();
                     RenderCard.handleRotation(x0, y0, z0, 270, 2.7F);
                     s = Integer.toString(player.getDiscardPile().size());
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.253, y0 + 0.001 - 2.0, z0 + 0.075, s, 0, false, 90, 3.0F, "");
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.243, y0 + 0.003 - 2.0, z0 + 0.065, s, 15658734, false, 90, 3.0F, "");
                     RenderCard.drawUnlocalizedString(Minecraft.func_71410_x().field_71466_p, x0 - 0.785, y - 2.0, z0 - 0.09, "Discard Pile", 16777215, false, 90, 1.0F, "");
                     RenderCard.endQuads();
                  }
               }
            }
         }

         RenderCard.drawCard(x - 6.5, y + 0.1, z + 2.5, (CommonCardState)null, 90, f, partialTicks, (PlayerCommonState)null, (PlayerCommonState)null);
         RenderCard.drawCard(x + 4.0, y + 0.1, z + 2.5, (CommonCardState)null, 90, f, partialTicks, (PlayerCommonState)null, (PlayerCommonState)null);
         drawCardOutline(x - 2.5, y, z - 4.5 + 3.0, false, r, g, b);
         drawCardOutline(x - 6.5 + 2.0, y, z + 2.5, true, r, g, b);
         drawCardOutline(x + 1.5, y, z - 4.5 + 3.0, false, r, g, b);
         drawCardOutline(x + 4.0 + 2.0, y, z + 2.5, true, r, g, b);
         RenderCard.drawCard(x - 6.5 + 2.0, y + 0.1, z + 2.5, (CommonCardState)null, 90, f, partialTicks, (PlayerCommonState)null, (PlayerCommonState)null);
         RenderCard.drawCard(x + 4.0 + 2.0, y + 0.1, z + 2.5, (CommonCardState)null, 90, f, partialTicks, (PlayerCommonState)null, (PlayerCommonState)null);

         for(i = 0; i < 6; ++i) {
            float zOffset = 7.0F + (float)(i / 3) * 1.5F;
            float xOffset = 1.6875F - (float)(i % 3 + 3) * 1.125F;
            drawPrizeOutline(x - (double)xOffset, y, z - (double)zOffset, r, g, b);
            xOffset = 7.4375F - (float)(i % 3 + 3) * 1.125F;
            drawPrizeOutline(x - (double)xOffset, y, z - (double)zOffset, r, g, b);
         }

         drawCardOutline(x - 0.25, y, z - 7.5, true, r, g, b);
         GL11.glPopMatrix();
      }

   }

   private static void drawDamageCounter(int damage, double x0, double y0, double z0, int rotation, FontRenderer f) {
      if (damage > 0) {
         RenderCard.startQuads();
         RenderCard.handleRotation(x0, y0, z0, rotation, 1.0F);
         RenderCard.drawSprite(x0 + 0.55, y0 + 0.01, z0 + 1.0, 3.0, 0, new ResourceLocation("tcg:gui/cards/damage.png"));
         RenderCard.drawUnlocalizedString(f, x0 + 2.2, y0 - 1.97, z0 - 0.1, Integer.toString(damage), 16777215, false, 90, 3.5F, "");
         RenderCard.endQuads();
      }

   }

   private static void drawStatusCounter(CardCondition cardCondition, double x0, double y0, double z0, int rotation) {
      double offset = 0.0;
      if (cardCondition == CardCondition.BURNT) {
         offset = 0.8;
      }

      RenderCard.startQuads();
      RenderCard.handleRotation(x0, y0, z0, rotation, 1.0F);
      RenderCard.drawSprite(x0 - offset, y0 + 0.005, z0 + 1.0, 3.0, 0, new ResourceLocation("tcg:gui/cards/" + cardCondition.getName().toLowerCase() + ".png"));
      RenderCard.endQuads();
   }

   private static void drawAttachments(int playerIndex, PokemonCardState activeCard, double x0, double y0, double z0, int rotation, float t, FontRenderer f, PlayerClientOpponentState player, PlayerClientOpponentState opp) {
      double attachmentOffsetX = 0.03;
      double attachmentOffsetY = 0.026;
      double attachmentOffsetZ = 0.03;
      int sign = playerIndex == 0 ? 1 : -1;
      int energyCount = 0;

      for(int attachmentIndex = 0; attachmentIndex < activeCard.getAttachments().size(); ++attachmentIndex) {
         CommonCardState attachment = (CommonCardState)activeCard.getAttachments().get(attachmentIndex);
         RenderCard.drawCardOrBase(x0 + (double)sign * attachmentOffsetX * (double)(attachmentIndex + 1), y0 - attachmentOffsetY * (double)(attachmentIndex + 1), z0 - (double)sign * attachmentOffsetZ * (double)(attachmentIndex + 1), attachment, 180, f, t, player, opp);
         if (attachment.getCardType() == CardType.ENERGY) {
            drawEnergySprite(playerIndex, x0, y0, z0, rotation, energyCount++, attachment.getMainEnergy());
            if (attachment.getSecondaryEnergy() != null) {
               drawEnergySprite(playerIndex, x0, y0, z0, rotation, energyCount++, attachment.getSecondaryEnergy());
            }
         } else if (attachment.getAbility() != null && attachment.getAbility().getEffect() != null) {
            List energies = attachment.getAbility().getEffect().getEnergyEquivalence(attachment);
            if (energies != null) {
               Iterator var24 = energies.iterator();

               while(var24.hasNext()) {
                  CommonCardState energy = (CommonCardState)var24.next();
                  drawEnergySprite(playerIndex, x0, y0, z0, rotation, energyCount++, energy.getMainEnergy());
               }
            }
         }
      }

   }

   private static void drawEnergySprite(int playerIndex, double x0, double y0, double z0, int rotation, int energyCount, Energy energy) {
      int sign = playerIndex == 0 ? true : true;
      double energySize = 0.25;
      double xEnergyOffset = -1.3;
      double zEnergyOffset = -0.7 + (double)energyCount * (energySize + 0.02);
      RenderCard.startQuads();
      RenderCard.handleRotation(x0, y0, z0, rotation, 1.0F);
      RenderCard.drawEnergySprite(x0 + xEnergyOffset, y0 + 0.01, z0 + zEnergyOffset, rotation, energy);
      RenderCard.endQuads();
   }

   @SideOnly(Side.CLIENT)
   public static void drawPokemonModelOnBoard(double x, double y, double z, Minecraft mc, float partialTicks, CommonCardState c, BoardLocation location, int sublocation, int playerIndex, float scale) {
      int rotation = playerIndex == 0 ? 270 : 90;
      x += playerIndex == 0 ? 0.6 : 1.5;
      z -= 0.75;

      try {
         EntityPixelmon pixelmon = activePokemons[playerIndex];
         if (pixelmon == null || pixelmon.getBaseStats().nationalPokedexNumber != c.getPokemonID()) {
            pixelmon = (EntityPixelmon)PixelmonEntityList.createEntityByName(EnumSpecies.getFromDex(c.getPokemonID()).name, mc.field_71441_e);
            pixelmon.getLvl().setLevel(0);
            pixelmon.getPokemonData().setGrowth(EnumGrowth.Ordinary);
            pixelmon.canMove = true;
            pixelmon.func_96094_a("");
            activePokemons[playerIndex] = pixelmon;
            pixelmon.setAnimation(AnimationType.IDLE);
         }

         pixelmon.setPixelmonScale((float)((double)scale * ((double)(pixelmon.getBaseStats().getHeight() / 3.0F) / pixelmon.getBaseStats().getBoundsData().getHeight())));
         pixelmon.getAnimationVariables().tick();
         GlStateManager.func_179094_E();
         GL11.glTranslated(x, y, z);
         GL11.glRotated((double)rotation, 0.0, 1.0, 0.0);
         GL11.glTranslated(-x, -y, -z);
         if (c.getOverrideModelColor() != null) {
            GL11.glColor3f(c.getOverrideModelColor().x, c.getOverrideModelColor().y, c.getOverrideModelColor().z);
         }

         try {
            Render entityClassRenderObject = Minecraft.func_71410_x().func_175598_ae().func_78715_a(EntityPixelmon.class);
            RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
            rp.renderPixelmon(pixelmon, x, y, z, partialTicks, true);
         } catch (Exception var17) {
         }

         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GlStateManager.func_179121_F();
      } catch (Exception var18) {
      }

   }
}
