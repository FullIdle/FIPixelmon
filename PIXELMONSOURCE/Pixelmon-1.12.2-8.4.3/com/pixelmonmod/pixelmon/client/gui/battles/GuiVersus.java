package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiTeamSelect;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GuiVersus extends GuiScreen {
   private static final ResourceLocation TOP_LEFT = new ResourceLocation("pixelmon:textures/gui/acceptDeny/Opponent1.png");
   private static final ResourceLocation MIDDLE = new ResourceLocation("pixelmon:textures/gui/acceptDeny/Opponent2.png");
   private static final ResourceLocation BOTTOM_RIGHT = new ResourceLocation("pixelmon:textures/gui/acceptDeny/Opponent3.png");
   private static final ResourceLocation VS = new ResourceLocation("pixelmon:textures/gui/acceptDeny/Opponent8.png");
   private static final ResourceLocation PLAYER_1_FRAME = new ResourceLocation("pixelmon:textures/gui/acceptDeny/player1Frame.png");
   private static final ResourceLocation PLAYER_2_FRAME = new ResourceLocation("pixelmon:textures/gui/acceptDeny/player2Frame.png");
   private static final ResourceLocation PLAYER_1_NAME = new ResourceLocation("pixelmon:textures/gui/acceptDeny/player1Name.png");
   private static final ResourceLocation PLAYER_2_NAME = new ResourceLocation("pixelmon:textures/gui/acceptDeny/player2Name.png");
   private static final ResourceLocation POKE_BALL_HOLDER = new ResourceLocation("pixelmon:textures/gui/acceptDeny/pokeballHolder.png");
   protected int leftX;
   protected int topY;
   protected int ticks = 5;
   protected int offset1;
   protected int offset2;
   protected int playerPartyX;
   protected int playerPartyY;
   protected int opponentPartyX;
   protected int opponentPartyY;
   protected boolean isNPC;
   private ResourceLocation npcSkin;
   protected static final int GUI_WIDTH = 280;
   protected static final int GUI_HEIGHT = 182;
   public static final int PARTY_SEPARATOR = 12;

   protected GuiVersus() {
      this.field_146297_k = Minecraft.func_71410_x();
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.updateAnchors();
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_73876_c() {
      try {
         super.func_73876_c();
      } catch (NullPointerException var2) {
      }

      if (this.ticks > 0) {
         --this.ticks;
      }

   }

   private void updateAnchors() {
      this.leftX = (this.field_146294_l - 280) / 2;
      this.topY = (this.field_146295_m - 182) / 2;
      this.offset1 = -this.ticks * 50;
      this.offset2 = -this.offset1;
      this.playerPartyX = this.leftX + 84;
      this.playerPartyY = this.topY + 27;
      this.opponentPartyX = this.leftX + 120;
      this.opponentPartyY = this.topY + 121;
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      this.updateAnchors();
      this.drawRectangle();
      this.drawEntity(PLAYER_1_NAME, PLAYER_1_FRAME, this.field_146297_k.field_71439_g, this.field_146297_k.field_71439_g.getDisplayNameString(), this.offset1, 76, 9, 19, 11, 16, 9, 82, 27, 92, 15, false);
      EntityLivingBase opponent = this.getOpponent();
      if (opponent != null) {
         String opponentName;
         if (this.isNPC) {
            opponentName = GuiTeamSelect.teamSelectPacket.npcName;
         } else {
            opponentName = opponent.func_145748_c_().func_150254_d();
         }

         this.drawEntity(PLAYER_2_NAME, PLAYER_2_FRAME, opponent, opponentName, this.offset2, 56, 103, 202, 76, 199, 74, 118, 121, 188 - this.field_146297_k.field_71466_p.func_78256_a(opponentName), 108, this.isNPC);
         GlStateManager.func_179084_k();
      }
   }

   public boolean func_73868_f() {
      return false;
   }

   protected abstract EntityLivingBase getOpponent();

   private void drawRectangle() {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179147_l();
      this.field_146297_k.field_71446_o.func_110577_a(TOP_LEFT);
      GuiHelper.drawImageQuad((double)(this.leftX + this.offset1), (double)this.topY, 280.0, 182.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71446_o.func_110577_a(MIDDLE);
      GuiHelper.drawImageQuad((double)(this.leftX + this.offset2), (double)this.topY, 280.0, 182.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71446_o.func_110577_a(BOTTOM_RIGHT);
      GuiHelper.drawImageQuad((double)this.leftX, (double)this.topY, 280.0, 182.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      if (this.ticks == 0) {
         this.field_146297_k.field_71446_o.func_110577_a(VS);
         GuiHelper.drawImageQuad((double)this.leftX, (double)(this.topY + 2), 280.0, 226.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

   }

   protected void drawEntity(ResourceLocation name, ResourceLocation frame, EntityLivingBase entity, String entityName, int tickOffset, int nameBoxX, int nameBoxY, int headX, int headY, int frameX, int frameY, int holderX, int holderY, int nameX, int nameY, boolean isCurrentNPC) {
      this.field_146297_k.field_71446_o.func_110577_a(name);
      GuiHelper.drawImageQuad((double)(this.leftX + nameBoxX + tickOffset), (double)(this.topY + nameBoxY), 145.0, 17.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.drawEntity(entity, this.leftX + headX + tickOffset, this.topY + headY, isCurrentNPC);
      this.field_146297_k.field_71446_o.func_110577_a(frame);
      GuiHelper.drawImageQuad((double)(this.leftX + frameX + tickOffset), (double)(this.topY + frameY), 65.0, 65.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71446_o.func_110577_a(POKE_BALL_HOLDER);
      GuiHelper.drawImageQuad((double)(this.leftX + holderX + tickOffset), (double)(this.topY + holderY), 80.0, 17.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71466_p.func_78276_b(entityName, this.leftX + nameX + tickOffset, this.topY + nameY, 16777215);
   }

   protected void drawPokeBalls(int[] pokeBalls, int startX, int startY, int tickOffset) {
      int pos = 0;
      int[] var6 = pokeBalls;
      int var7 = pokeBalls.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         int pid = var6[var8];
         if (pid != -999) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            if (pid < 0) {
               pid *= -1;
               --pid;
               GlStateManager.func_179131_c(0.4F, 0.4F, 0.4F, 1.0F);
            }

            Item pball = EnumPokeballs.getFromIndex(pid).getItem();
            this.field_146296_j.func_175042_a(new ItemStack(pball), startX + pos * 12 + tickOffset, startY);
         }

         ++pos;
      }

   }

   protected void drawOpponentPokeBalls(int numPokeBalls) {
      int[] opponentPokeBalls = new int[numPokeBalls];
      Arrays.fill(opponentPokeBalls, 0);
      this.drawPokeBalls(opponentPokeBalls, this.opponentPartyX, this.opponentPartyY, this.offset2);
   }

   protected void drawEntity(EntityLivingBase entity, int x, int y, boolean isCurrentNPC) {
      ResourceLocation skin = null;
      if (isCurrentNPC && entity instanceof EntityNPC) {
         EntityNPC npc = (EntityNPC)entity;
         if (!npc.bindTexture() && this.npcSkin == null) {
            this.npcSkin = new ResourceLocation(npc.getTexture());
         }

         skin = this.npcSkin;
      } else {
         skin = ((AbstractClientPlayer)entity).func_110306_p();
         this.field_146297_k.field_71446_o.func_110577_a(skin);
      }

      if (skin != null) {
         this.field_146297_k.field_71446_o.func_110577_a(skin);
      }

      GuiHelper.drawImageQuad((double)x, (double)y, 60.0, 60.0F, 0.125, 0.125, 0.25, 0.25, this.field_73735_i);
   }
}
