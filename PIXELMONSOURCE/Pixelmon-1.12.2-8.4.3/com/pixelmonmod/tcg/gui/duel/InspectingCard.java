package com.pixelmonmod.tcg.gui.duel;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerClientOpponentState;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.helper.CardHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class InspectingCard extends CardWithLocation {
   private Minecraft mc = Minecraft.func_71410_x();
   private TCGGuiScreen parent;
   private boolean isLocked;
   private float opacity;
   private boolean isUp = false;
   private String title = null;
   private int xOffset = 300;
   private boolean isPickingAttack;

   public InspectingCard(TCGGuiScreen parent) {
      this.parent = parent;
   }

   public void set(CommonCardState card, boolean isMine, BoardLocation location, int locationSubIndex, boolean isLocked) {
      if (card.getData().getID() != ImmutableCard.FACE_DOWN_ID) {
         this.set(card, isMine, location, locationSubIndex, isLocked, (String)null, false);
      }
   }

   public void set(CommonCardState card, boolean isMine, BoardLocation location, int locationSubIndex, boolean isLocked, String title) {
      this.set(card, isMine, location, locationSubIndex, isLocked, title, false);
   }

   public void set(CommonCardState card, boolean isMine, BoardLocation location, int locationSubIndex, boolean isLocked, String title, boolean isPickingAttack) {
      if (isMine != this.isMine || location != this.location || locationSubIndex != this.locationSubIndex) {
         this.opacity = 0.0F;
         this.set(card, isMine, location, locationSubIndex);
         this.isLocked = isLocked;
         this.isUp = true;
         this.title = title;
         this.isPickingAttack = isPickingAttack;
         this.xOffset = isPickingAttack ? 0 : 300;
      }

   }

   public void clear() {
      this.location = null;
      this.isLocked = false;
      this.isUp = false;
      this.isPickingAttack = false;
   }

   public BoardLocation getLocation() {
      return this.location;
   }

   public void update(boolean isButtonPressed) {
      if (!this.isLocked && !isButtonPressed) {
         this.isUp = false;
         this.clear();
      }

      if (this.isUp && this.opacity < 1.0F) {
         this.opacity = (float)((double)this.opacity + 0.3);
         if (this.opacity >= 1.0F) {
            this.opacity = 1.0F;
         }
      } else if (!this.isLocked && !this.isUp && this.opacity > 0.0F) {
         this.opacity = (float)((double)this.opacity - 0.2);
         if (this.opacity <= 0.0F) {
            this.opacity = 0.0F;
            this.card = null;
         }
      }

   }

   public void draw(PlayerClientMyState me, PlayerClientOpponentState opp, FontRenderer f, float zLevel) {
      ScaledResolution res = new ScaledResolution(this.mc);
      if (this.card != null) {
         float guiScaling = (float)this.mc.field_71440_d / 240.0F / (float)res.func_78325_e();
         int pushToSide = (int)((float)this.xOffset * guiScaling);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, this.opacity);
         int cardX = res.func_78326_a() + pushToSide;
         int cardY = res.func_78328_b();
         if (this.title != null) {
            int width = f.func_78256_a(this.title);
            f.func_175065_a(this.title, (float)((res.func_78326_a() + pushToSide) / 2 - width / 2), (float)(res.func_78328_b() / 2 - 95), 16777215, true);
         }

         CardHelper.drawCard(this.card, this.mc, cardX, cardY, zLevel, this.opacity, 0.0, me, opp);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   void drawTooltip(int mouseX, int mouseY, FontRenderer f, float zLevel) {
      ScaledResolution res = new ScaledResolution(this.mc);
      if (this.card != null) {
         float guiScaling = (float)this.mc.field_71440_d / 240.0F / (float)res.func_78325_e();
         int pushToSide = (int)(300.0F * guiScaling);
         this.parent.drawEffectTooltip(this.card, mouseX, mouseY, f, pushToSide / 2, 0);
      }

   }

   public int getxOffset() {
      return this.xOffset;
   }

   boolean isPickingAttack() {
      return this.isPickingAttack;
   }
}
