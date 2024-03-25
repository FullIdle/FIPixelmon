package com.pixelmonmod.pixelmon.client.gui.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.raids.RaidAction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiRaidStart extends GuiScreen {
   private static final ResourceLocation WATERMARK = new ResourceLocation("pixelmon", "textures/gui/raids/watermark.png");
   private static final ResourceLocation STAR = new ResourceLocation("pixelmon", "textures/gui/raids/star.png");
   private final ArrayList buttons = new ArrayList();
   private final ArrayList slots = new ArrayList();
   private RaidData raid;
   private EntityPixelmon pixelmon;
   private boolean shouldRefresh = false;
   private float fadeout = 0.0F;
   private int seconds = 0;
   private boolean closing = false;

   public GuiRaidStart(RaidData raid) {
      this.field_146297_k = Minecraft.func_71410_x();
      this.setRaid(raid);
   }

   public void setRaid(RaidData raid) {
      this.setRaid(raid, (GuiRaidStart)null);
   }

   public void setRaid(RaidData raid, GuiRaidStart old) {
      this.raid = raid;
      if (old != null) {
         this.pixelmon = old.pixelmon;
      } else {
         Pokemon pokemon = Pixelmon.pokemonFactory.create(raid.getSpecies());
         if (raid.getForm() != null) {
            pokemon.setForm(raid.getForm());
         }

         pokemon.setGrowth(EnumGrowth.Ordinary);
         this.pixelmon = new EntityPixelmon(this.field_146297_k.field_71441_e);
         this.pixelmon.setPokemon(pokemon);
         this.pixelmon.setAnimation(AnimationType.IDLE);
         this.pixelmon.checkAnimation();
         this.pixelmon.initAnimation();
      }

      this.shouldRefresh = true;
   }

   private void initButtons() {
      this.buttons.clear();
      this.slots.clear();
      this.buttons.add((new RaidButton((double)this.field_146294_l / 1.45, (double)this.field_146295_m / 1.15 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, (double)((float)this.field_146294_l / 4.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.solo", new Object[0]), this.raid, (button) -> {
         Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 6));
      })).setEnabled(!this.raid.isStarting()).setVisible(this.raid.isSolo()));
      this.buttons.add((new RaidButton((double)this.field_146294_l / 1.45, (double)this.field_146295_m / 1.15 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, (double)((float)this.field_146294_l / 4.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.start", new Object[0]), this.raid, (button) -> {
         Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 9));
      })).setEnabled(!this.raid.isStarting() && this.raid.isOwner(this.field_146297_k.field_71439_g.func_110124_au())).setVisible(!this.raid.isSolo()));
      this.buttons.add((new RaidButton((double)this.field_146294_l / 1.45, (double)this.field_146295_m / 1.15 - (double)((int)((double)this.field_146295_m * 0.08)), 1.0, (double)((float)this.field_146294_l / 4.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.multi", new Object[0]), this.raid, (button) -> {
         Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 7));
      })).setEnabled(!this.raid.isStarting()).setVisible(this.raid.isSolo()));
      this.buttons.add((new RaidButton((double)this.field_146294_l / 1.45, (double)this.field_146295_m / 1.15 - (double)((int)((double)this.field_146295_m * 0.08)), 1.0, (double)((float)this.field_146294_l / 4.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.players", new Object[]{this.raid.getPlayers().size()}), this.raid, (button) -> {
      })).setEnabled(false).setVisible(!this.raid.isSolo()));
      this.buttons.add((new RaidButton((double)this.field_146294_l / 1.45, (double)this.field_146295_m / 1.15, 1.0, (double)((float)this.field_146294_l / 4.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.quit", new Object[0]), this.raid, (button) -> {
         this.closing = true;
         Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 8));
      })).setEnabled(!this.raid.isStarting()));
      float xGap = (float)this.field_146294_l * 0.075F;
      float yGap = (float)this.field_146295_m * 0.1F;
      ArrayList players = this.raid.getPlayers();

      for(int i = 0; i < 4; ++i) {
         RaidData.RaidPlayer player = players.size() > i ? (RaidData.RaidPlayer)players.get(i) : null;
         this.slots.add((new PokemonSlot(this.raid, (double)((float)this.field_146294_l / 1.1F), (double)((float)this.field_146295_m * 0.08F + yGap * (float)i), 2.0, player, (button) -> {
         })).setEnabled(false));
      }

      Pokemon[] party = ClientStorageManager.party.getAll();

      for(int i = 0; i < 6; ++i) {
         int xPos = i;
         int yPos = 0;
         if (i >= 3) {
            xPos = i - 3;
            yPos = 1;
         }

         this.slots.add((new PokemonSlot(this.raid, (double)((float)this.field_146294_l * 0.755F + xGap * (float)xPos), (double)((float)this.field_146295_m * 0.55F + yGap * (float)yPos), 2.0, party[i], (button) -> {
            if (party[i] != null && party[i].canBattle()) {
               SoundHelper.playButtonPressSound();
               Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), i));
            }

         })).setEnabled(!this.raid.isStarting()).setIndex(i));
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.buttons.isEmpty() || this.shouldRefresh) {
         this.shouldRefresh = false;
         this.initButtons();
      }

      this.func_146276_q_();
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      int starSize;
      if (this.raid.isStarting()) {
         starSize = 5 - this.raid.tick / 20;
         if (this.seconds != starSize) {
            SoundHelper.playSound(SoundEvents.field_193807_ew, 0.8F, 1.5F);
            this.seconds = starSize;
            this.fadeout = 255.0F;
         }

         this.raid.getDenEntity(this.field_146297_k.field_71441_e).map((den) -> {
            Color c2 = den.getColorUIB();
            Color color = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), Math.max(0, (int)this.fadeout));
            GuiHelper.drawScaledCenteredString(String.valueOf(this.seconds), (float)this.field_146294_l * 0.8F, (float)this.field_146295_m * 0.13F, color.getRGB(), (float)this.field_146294_l * 0.25F);
            return true;
         }).orElseGet(() -> {
            Color color = new Color(226, 75, 66, Math.max(0, (int)this.fadeout));
            GuiHelper.drawScaledCenteredString(String.valueOf(this.seconds), (float)this.field_146294_l * 0.8F, (float)this.field_146295_m * 0.13F, color.getRGB(), (float)this.field_146294_l * 0.25F);
            return true;
         });
         --this.fadeout;
      }

      this.field_146297_k.field_71446_o.func_110577_a(STAR);
      starSize = (int)((double)this.field_146294_l * 0.05);

      for(int i = 0; i < this.raid.getStars(); ++i) {
         GuiHelper.drawImage((double)starSize * 0.75 + (double)(i * starSize), (double)starSize * 0.6, (double)starSize, (double)starSize, 1.0F);
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
      List types = this.pixelmon.getBaseStats().getTypeList();

      for(int i = 0; i < types.size(); ++i) {
         EnumType type = (EnumType)types.get(i);
         float x = type.textureX;
         float y = type.textureY;
         GuiHelper.drawImageQuad((double)starSize * 0.75 + (double)(i * starSize), (double)(starSize * 2), (double)starSize, (float)starSize, (double)(x / 1792.0F), (double)(y / 768.0F), (double)((x + 240.0F) / 1792.0F), (double)((y + 240.0F) / 768.0F), 1.0F);
      }

      Iterator var26 = this.buttons.iterator();

      while(var26.hasNext()) {
         RaidButton button = (RaidButton)var26.next();
         button.draw((double)mouseX, (double)mouseY, partialTicks);
      }

      RaidData.RaidPlayer player = this.raid.getPlayer(this.field_146297_k.field_71439_g.func_110124_au());
      Iterator var29 = this.slots.iterator();

      while(var29.hasNext()) {
         PokemonSlot slot = (PokemonSlot)var29.next();
         slot.draw((double)mouseX, (double)mouseY, (double)this.field_146294_l, (double)this.field_146295_m, player != null && player.index == slot.getIndex() || slot.isPlayer(this.field_146297_k.field_71439_g.func_110124_au()), partialTicks);
      }

      boolean special = !this.raid.canAllCatch;
      String desc = I18n.func_135052_a(special ? "raid.lorespecial" : "raid.lore", new Object[]{4, 10});
      String[] split = desc.split("\\\\n");
      int lineCount = 0;
      String[] var11 = split;
      int var12 = split.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         String line = var11[var13];
         GuiHelper.drawScaledString(line, (float)this.field_146294_l * 0.025F, (float)this.field_146295_m * (special ? 0.82F : 0.88F) + (float)this.field_146295_m * 0.03F * (float)lineCount, 16777215, (float)this.field_146295_m * 0.05F);
         ++lineCount;
      }

      GuiHelper.drawScaledCenteredString(I18n.func_135052_a("raid.choose", new Object[0]), (float)this.field_146294_l * 0.855F, (float)this.field_146295_m * 0.46F, -1, (float)this.field_146294_l * 0.03F);
      GlStateManager.func_179094_E();
      GlStateManager.func_179142_g();
      GlStateManager.func_179126_j();
      GlStateManager.func_179109_b((float)this.field_146294_l / 4.0F, (float)this.field_146295_m / 4.0F * 3.0F, 300.0F);
      double referenceW = (double)((float)this.field_146295_m / 3.0F);
      double referenceH = (double)((float)this.field_146295_m / 3.0F);
      double dh = this.pixelmon.getBaseStats().getBoundsData().getHeight() - referenceH;
      double dw = this.pixelmon.getBaseStats().getBoundsData().getWidth() - referenceW;
      double scalar = 1.0;
      if (dh > dw) {
         scalar = referenceH / this.pixelmon.getBaseStats().getBoundsData().getHeight();
      } else {
         scalar = referenceW / this.pixelmon.getBaseStats().getBoundsData().getWidth();
      }

      GlStateManager.func_179139_a(scalar, scalar, scalar);
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      RenderHelper.func_74519_b();

      try {
         RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
         Render entityClassRenderObject = renderManager.func_78715_a(EntityPixelmon.class);
         RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
         rp.renderPixelmon(this.pixelmon, 0.0, 0.0, 0.0, partialTicks, true, 1, new float[]{0.0F, 0.0F, 0.0F, 1.0F});
         renderManager.field_78735_i = 180.0F;
      } catch (Exception var24) {
         var24.printStackTrace();
      }

      GlStateManager.func_179121_F();
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      Iterator var4 = this.buttons.iterator();

      while(var4.hasNext()) {
         RaidButton button = (RaidButton)var4.next();
         button.onClick((double)mouseX, (double)mouseY, mouseButton);
      }

      var4 = this.slots.iterator();

      while(var4.hasNext()) {
         PokemonSlot slot = (PokemonSlot)var4.next();
         slot.onClick((double)mouseX, (double)mouseY, mouseButton);
      }

   }

   public void func_146276_q_() {
      this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, -1596523265, -1866285374);
      int right = this.field_146294_l / 3 * 2;
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)right, 0.0F, 0.0F);
      GlStateManager.func_179114_b(25.0F, 0.0F, 0.0F, 1.0F);
      this.raid.getDenEntity(this.field_146297_k.field_71441_e).map((den) -> {
         GuiHelper.drawGradientRect(-right, 0, 0.0F, 0, this.field_146295_m * 2, den.getColorUIA().getRGB(), den.getColorUIB().getRGB());
         Color c3 = den.getColorUIC();
         GlStateManager.func_179131_c((float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, 0.2F);
         return true;
      }).orElseGet(() -> {
         GuiHelper.drawGradientRect(-right, 0, 0.0F, 0, this.field_146295_m * 2, -37796, -5752007);
         GlStateManager.func_179131_c(0.8F, 0.0F, 0.0F, 0.2F);
         return true;
      });
      GlStateManager.func_179121_F();
      this.field_146297_k.field_71446_o.func_110577_a(WATERMARK);
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179094_E();
      GlStateManager.func_179114_b(25.0F, 0.0F, 0.0F, 1.0F);
      GuiHelper.drawImage((double)((float)this.field_146294_l / 6.0F), (double)((float)(-this.field_146295_m) / 4.0F), (double)((float)this.field_146294_l / 2.5F), (double)((float)this.field_146294_l / 2.5F) * 1.65591398, 2.0F);
      GlStateManager.func_179121_F();
   }

   public void func_73876_c() {
      this.pixelmon.getAnimationVariables().tick();
   }

   public void func_175273_b(Minecraft mc, int nw, int nh) {
      super.func_175273_b(mc, nw, nh);
      this.initButtons();
   }

   public void func_146281_b() {
      super.func_146281_b();
      if (!this.closing) {
         if (!this.raid.isStarting()) {
            this.closing = true;
            Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 8));
         } else {
            this.field_146297_k.func_152344_a(() -> {
               this.field_146297_k.func_147108_a(this);
            });
         }
      }

   }

   public void func_146282_l() throws IOException {
      if (!this.raid.isStarting()) {
         super.func_146282_l();
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
