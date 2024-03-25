package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.camera.CameraMode;
import com.pixelmonmod.pixelmon.client.camera.CameraTargetEntity;
import com.pixelmonmod.pixelmon.client.camera.GuiCamera;
import com.pixelmonmod.pixelmon.client.gui.battles.EvoInfo;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionResponse;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EntityBoundsData;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;

public class GuiEvolve extends GuiCamera {
   public static EntityPixelmon currentPokemon;
   boolean createdEntity = false;
   String newPokemon;
   String oldNickname;
   boolean cancelled = false;
   EvoInfo evoInfo = null;
   int ticks = 0;
   int fadeCount = 0;

   public GuiEvolve() {
      super(CameraMode.Evolution);
      if (this.field_146297_k == null) {
         this.field_146297_k = Minecraft.func_71410_x();
      }

      if (ClientProxy.battleManager.evolveList.isEmpty()) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         this.checkForPokemon();
         if (currentPokemon == null) {
            Pixelmon.network.sendToServer(new EvolutionResponse(this.evoInfo.pokemonUUID));
         }

      }
   }

   private void checkForPokemon() {
      if (this.evoInfo == null) {
         this.evoInfo = (EvoInfo)ClientProxy.battleManager.evolveList.get(0);
         ClientProxy.battleManager.evolveList.remove(0);
      }

      UUID pokemonUUID = this.evoInfo.pokemonUUID;
      this.newPokemon = this.evoInfo.evolveInto;
      currentPokemon = GuiHelper.getEntity(pokemonUUID);
      Minecraft mc = Minecraft.func_71410_x();
      if (currentPokemon != null) {
         currentPokemon.field_70714_bg.field_75782_a.clear();
         if (ClientProxy.camera != null && PixelmonConfig.useBattleCamera) {
            ClientProxy.camera.field_70128_L = false;
            mc.func_175607_a(ClientProxy.camera);
            ClientProxy.camera.setTargetRandomPosition(new CameraTargetEntity(currentPokemon));
         }

         this.oldNickname = currentPokemon.getNickname();
         this.calcSizeDifference();
      }
   }

   private void calcSizeDifference() {
      BaseStats bs = EnumSpecies.getFromNameAnyCase(this.newPokemon).getBaseStats();
      BaseStats currentStats = currentPokemon.getBaseStats();
      if (currentStats != null) {
         EntityBoundsData newData = bs.getBoundsData();
         EntityBoundsData currentData = currentPokemon.getBaseStats().getBoundsData();
         currentPokemon.heightDiff = (float)(newData.getHeight() - currentData.getHeight());
         currentPokemon.widthDiff = (float)(newData.getWidth() - currentData.getWidth());
         currentPokemon.lengthDiff = (float)(newData.getWidth() - currentData.getWidth());
      }
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (currentPokemon != null && currentPokemon.evoStage == EvolutionStage.Choice) {
         ++this.ticks;
         if (this.ticks >= 80) {
            Pixelmon.network.sendToServer(new EvolutionResponse(currentPokemon.getPokemonData().getUUID(), true));
            currentPokemon.evolvingVal = 0;
            currentPokemon.evoAnimTicks = 0;
            this.ticks = 0;
         }
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int par3) {
      if (currentPokemon != null) {
         if (currentPokemon.evoStage == EvolutionStage.Choice) {
            int xPos = this.field_146294_l / 2 - 30;
            int yPos = this.field_146295_m / 4 - 15;
            if (mouseX >= xPos && mouseX <= xPos + 60 && mouseY >= yPos && mouseY <= yPos + 17) {
               Pixelmon.network.sendToServer(new EvolutionResponse(currentPokemon.getPokemonData().getUUID(), false));
               currentPokemon.evoStage = null;
               this.cancelled = true;
            }
         } else if (currentPokemon.evoStage == null || this.cancelled) {
            Minecraft minecraft = Minecraft.func_71410_x();
            minecraft.field_71439_g.func_71053_j();
            if (!ClientProxy.battleManager.evolveList.isEmpty()) {
               minecraft.func_147108_a(new GuiEvolve());
            } else if (ServerStorageDisplay.bossDrops != null) {
               minecraft.func_147108_a(new GuiItemDrops());
            } else if (!ClientProxy.battleManager.newAttackList.isEmpty()) {
               minecraft.func_147108_a(new GuiBattle());
            } else if (ClientData.openMegaItemGui >= 0) {
               minecraft.func_147108_a(new GuiMegaItem(ClientData.openMegaItemGui > 0));
            } else {
               Pixelmon.network.sendToServer(new BattleGuiClosed());
            }

            minecraft.func_175607_a(minecraft.field_71439_g);
         }

      }
   }

   public void func_146281_b() {
      super.func_146281_b();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.field_146297_k == null) {
         this.field_146297_k = Minecraft.func_71410_x();
      }

      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.evo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (currentPokemon == null) {
         this.checkForPokemon();
      } else {
         if (currentPokemon.evoStage != EvolutionStage.PreAnimation && currentPokemon.evoStage != EvolutionStage.PostAnimation) {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 120), (double)(this.field_146295_m / 4 - 40), 240.0, 40.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         String s;
         if (currentPokemon.evoStage == EvolutionStage.PreChoice) {
            s = I18n.func_135052_a("gui.guiEvolve.huh", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 4 - 30, 16777215);
         }

         if (currentPokemon.evoStage == EvolutionStage.Choice) {
            this.oldNickname = currentPokemon.getEscapedNickname();
            s = I18n.func_135052_a("gui.guiEvolve.evolve", new Object[]{this.oldNickname});
            this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 4 - 30, 16777215);
            int xPos = this.field_146294_l / 2 - 30;
            int yPos = this.field_146295_m / 4 - 15;
            if (mouseX >= xPos && mouseX <= xPos + 60 && mouseY >= yPos && mouseY <= yPos + 17) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.buttonOver);
            } else {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.button);
            }

            GuiHelper.drawImageQuad((double)xPos, (double)yPos, 60.0, 17.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            s = I18n.func_135052_a("gui.cancel.text", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 4 - 11, 16777215);
         }

         if (this.cancelled) {
            s = I18n.func_135052_a("gui.guiEvolve.cancel", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 4 - 30, 16777215);
         } else if (currentPokemon.evoStage == EvolutionStage.End || currentPokemon.evoStage == null) {
            s = I18n.func_135052_a("gui.guiEvolve.done", new Object[]{this.oldNickname, currentPokemon.getLocalizedName()});
            this.field_146297_k.field_71466_p.func_78276_b(s, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(s) / 2, this.field_146295_m / 4 - 30, 16777215);
         }

         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
      }
   }
}
