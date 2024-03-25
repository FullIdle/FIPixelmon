package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.ServerTrades;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class GuiTrading extends GuiScreen {
   public boolean ready = false;
   private int selected = -1;
   private BlockPos tradeMachine;

   public GuiTrading(int x, int y, int z) {
      this.tradeMachine = new BlockPos(x, y, z);
   }

   public void func_146281_b() {
      Pixelmon.network.sendToServer(ServerTrades.getDeRegisterPacket(this.tradeMachine));
      ClientTradingManager.reset();
   }

   public void func_73863_a(int var2, int var3, float var1) {
      ScaledResolution var5 = new ScaledResolution(this.field_146297_k);
      var5.func_78326_a();
      var5.func_78328_b();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - 256) / 2, (this.field_146295_m - 206) / 2, 0, 0, 256, 204);
      int i = -1;
      Pokemon[] party = ClientStorageManager.party.getAll();
      Pokemon[] var7 = party;
      int var8 = party.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Pokemon p = var7[var9];
         ++i;
         if (p != null) {
            I18n.func_135052_a("pixelmon." + p.getSpecies().name.toLowerCase() + ".name", new Object[0]);
            GuiHelper.bindPokemonSprite(p, this.field_146297_k);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 93 + 25 * i), (double)(this.field_146295_m / 2 + 68), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            if (!p.getHeldItem().func_190926_b()) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 97 + 25 * i + 18), (double)(this.field_146295_m / 2 + 68 + 18), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            }
         }
      }

      this.drawPlayer(this.field_146297_k.field_71439_g, 0, 0);
      this.drawPlayer(ClientTradingManager.tradePartner, 270, 90);
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(0.5F, 0.5F, 0.0F);
      Pokemon playerPokemon = null;
      if (this.selected != -1) {
         playerPokemon = party[this.selected];
      }

      this.drawStats(ClientTradingManager.selectedStats, playerPokemon, 0);
      this.drawStats(ClientTradingManager.tradeTargetStats, ClientTradingManager.tradeTarget, 270);
      GlStateManager.func_179121_F();
      this.drawPokemonDetails(playerPokemon, -117, -212, -180, -75);
      this.drawPokemonDetails(ClientTradingManager.tradeTarget, 18, 58, 90, 15);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.ready", new Object[0]), (this.field_146294_l + 130) / 2, (this.field_146295_m + 157) / 2, 16777215);
      this.drawButtonReady(var2, var3);
      if (!this.hasOtherHatchedPokemon(this.selected) || playerPokemon != null && playerPokemon.isEgg() && !ClientTradingManager.targetPartyIsMoreThanOnePokemon) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.invalid", new Object[0]), (this.field_146294_l - 30) / 2, (this.field_146295_m + 38) / 2, 16213760);
      } else if (ClientTradingManager.player1Ready && ClientTradingManager.player2Ready) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.trade", new Object[0]), (this.field_146294_l - 30) / 2, (this.field_146295_m + 38) / 2, 16777215);
         this.drawButtonTrade(var2, var3);
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.notready", new Object[0]), (this.field_146294_l - 45) / 2, (this.field_146295_m + 38) / 2, 16777215);
      }

      this.drawPokemonSelection(var2, var3);
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(0.5F, 0.5F, 0.0F);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.want", new Object[]{I18n.func_135052_a(this.field_146297_k.field_71439_g.func_145748_c_().func_150254_d(), new Object[0])}), this.field_146294_l - 235, this.field_146295_m - 178, 16777215);
      if (ClientTradingManager.tradePartner != null) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.want", new Object[]{I18n.func_135052_a(ClientTradingManager.tradePartner.func_70005_c_(), new Object[0])}), this.field_146294_l + 35, this.field_146295_m - 178, 16777215);
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.nuf", new Object[0]), this.field_146294_l + 35, this.field_146295_m - 178, 16777215);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      if (ClientTradingManager.tradePartner != null && !ClientTradingManager.player2Ready) {
         GlStateManager.func_179124_c(1.0F, 0.0F, 0.0F);
         this.func_73729_b((this.field_146294_l + 45) / 2, (this.field_146295_m + 85) / 2, 61, 242, 90, 14);
      } else if (!ClientTradingManager.player2Ready) {
         GlStateManager.func_179124_c(1.0F, 0.0F, 0.0F);
         this.func_73729_b((this.field_146294_l + 65) / 2, (this.field_146295_m + 85) / 2, 153, 242, 72, 14);
      } else if (ClientTradingManager.player2Ready) {
         GlStateManager.func_179124_c(0.0F, 1.0F, 0.0F);
         this.func_73729_b((this.field_146294_l + 75) / 2, (this.field_146295_m + 85) / 2, 1, 242, 58, 14);
      }

      if (!ClientTradingManager.player1Ready) {
         GlStateManager.func_179124_c(1.0F, 0.0F, 0.0F);
         this.func_73729_b((this.field_146294_l - 225) / 2, (this.field_146295_m + 85) / 2, 61, 242, 90, 14);
      } else if (ClientTradingManager.player1Ready) {
         GlStateManager.func_179124_c(0.0F, 1.0F, 0.0F);
         this.func_73729_b((this.field_146294_l - 195) / 2, (this.field_146295_m + 85) / 2, 1, 242, 58, 14);
      }

      GlStateManager.func_179121_F();
      if (this.selected != -1) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179124_c(0.0F, 1.0F, 0.0F);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
         this.func_73729_b((this.field_146294_l - 190 + this.selected * 50) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         GlStateManager.func_179121_F();
      }

      this.drawButtonClose(var2, var3);
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(0.5F, 0.5F, 0.0F);
      this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("gui.trading.select", new Object[0]), (this.field_146294_l / 2 - 118) * 2, (this.field_146295_m / 2 + 77) * 2, 50, 16777215);
      GlStateManager.func_179121_F();
   }

   private void drawPlayer(EntityPlayer player, int playerOffset, int questionOffset) {
      if (player != null) {
         GuiHelper.drawEntity(player, (this.field_146294_l - 210 + playerOffset) / 2, (this.field_146295_m - 82) / 2, 20.0F, 0.0F, 0.0F);
      } else {
         GlStateManager.func_179094_E();
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179152_a(1.5F, 1.5F, 0.0F);
         this.func_73729_b(this.field_146294_l - 75 + questionOffset, this.field_146295_m - 265, 227, 242, 10, 14);
         GlStateManager.func_179121_F();
      }

   }

   private void drawStats(PixelmonStatsData stats, Pokemon pokemon, int xOffset) {
      String[] statNames = new String[]{"hp", "attack", "defense", "spattack", "spdefense", "speed"};
      int[] statHeights = new int[6];
      int textPosition = this.field_146294_l - 183 + xOffset;

      int statPosition;
      for(statPosition = 0; statPosition < statNames.length; ++statPosition) {
         String statLang = "nbt." + statNames[statPosition] + ".name";
         int statHeight = this.field_146295_m - 156 + 16 * statPosition;
         statHeights[statPosition] = statHeight;
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a(statLang, new Object[0]), textPosition, statHeight, 16777215);
      }

      statPosition = this.field_146294_l - 54 + xOffset;
      boolean isEgg = pokemon == null ? false : pokemon.isEgg();
      if (!isEgg && stats != null) {
         int[] statValues;
         if (pokemon != null && pokemon.getStats() != null) {
            statValues = new int[]{pokemon.getStats().hp, pokemon.getStats().attack, pokemon.getStats().defence, pokemon.getStats().specialAttack, pokemon.getStats().specialDefence, pokemon.getStats().speed};
         } else {
            statValues = new int[]{stats.HP, stats.Attack, stats.Defence, stats.SpecialAttack, stats.SpecialDefence, stats.Speed};
         }

         for(int i = 0; i < statValues.length; ++i) {
            this.func_73731_b(this.field_146297_k.field_71466_p, String.valueOf(statValues[i]), statPosition, statHeights[i], 16777215);
         }
      } else {
         String questionMarks = "???";
         int[] var10 = statHeights;
         int var11 = statHeights.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            int statHeight = var10[var12];
            this.func_73731_b(this.field_146297_k.field_71466_p, questionMarks, statPosition, statHeight, 16777215);
         }
      }

   }

   private void drawPokemonDetails(Pokemon p, int spriteOffset, int textOffset, int descriptionOffset, int emptyOffset) {
      if (p != null) {
         GuiHelper.bindPokemonSprite(p, this.field_146297_k);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + spriteOffset), (double)(this.field_146295_m / 2 - 33), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         if (!p.getHeldItem().func_190926_b()) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.heldItem);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + spriteOffset + 18), (double)(this.field_146295_m / 2 - 33 + 18), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         if (p.isShiny() && !p.isEgg()) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.shiny);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + spriteOffset - 3), (double)(this.field_146295_m / 2 - 16), 10.0, 10.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         GlStateManager.func_179094_E();
         GlStateManager.func_179152_a(0.5F, 0.5F, 0.0F);
         String levelText;
         String nameText;
         String descriptionLang;
         if (p.isEgg()) {
            levelText = "???";
            nameText = I18n.func_135052_a("pixelmon.egg.name", new Object[0]);
            descriptionLang = "gui.trader.eggDescription";
         } else {
            levelText = " " + p.getLevel();
            nameText = p.getNickname();
            descriptionLang = "pixelmon." + p.getBaseStats().getPokemonName().toLowerCase() + ".description";
         }

         this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + levelText, this.field_146294_l + textOffset, this.field_146295_m - 62, 16777215);
         this.func_73732_a(this.field_146297_k.field_71466_p, nameText, this.field_146294_l + textOffset, this.field_146295_m - 10, 16777215);
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a(descriptionLang, new Object[0]), this.field_146294_l + descriptionOffset, this.field_146295_m - 55, 150, 16777215);
         GlStateManager.func_179121_F();
      } else {
         GlStateManager.func_179094_E();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179152_a(1.5F, 1.5F, 0.0F);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
         this.func_73729_b(this.field_146294_l / 3 + emptyOffset, this.field_146295_m / 3 - 20, 227, 242, 10, 14);
         GlStateManager.func_179121_F();
      }

   }

   public int drawPokemonSelection(int par1, int par2) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      int index = -1;
      if (par1 >= (this.field_146294_l - 190) / 2 && par1 <= (this.field_146294_l - 140) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(0) != null) {
         this.func_73729_b((this.field_146294_l - 190) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 0;
      }

      if (par1 >= (this.field_146294_l - 140) / 2 && par1 <= (this.field_146294_l - 90) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(1) != null) {
         this.func_73729_b((this.field_146294_l - 140) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 1;
      }

      if (par1 >= (this.field_146294_l - 90) / 2 && par1 <= (this.field_146294_l - 40) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(2) != null) {
         this.func_73729_b((this.field_146294_l - 90) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 2;
      }

      if (par1 >= (this.field_146294_l - 40) / 2 && par1 <= (this.field_146294_l + 10) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(3) != null) {
         this.func_73729_b((this.field_146294_l - 40) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 3;
      }

      if (par1 >= (this.field_146294_l + 10) / 2 && par1 <= (this.field_146294_l + 60) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(4) != null) {
         this.func_73729_b((this.field_146294_l + 10) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 4;
      }

      if (par1 >= (this.field_146294_l + 60) / 2 && par1 <= (this.field_146294_l + 110) / 2 && par2 >= (this.field_146295_m + 140) / 2 && par2 <= (this.field_146295_m + 190) / 2 && ClientStorageManager.party.get(5) != null) {
         this.func_73729_b((this.field_146294_l + 60) / 2, (this.field_146295_m + 140) / 2, 1, 206, 26, 24);
         index = 5;
      }

      return index;
   }

   private boolean hasOtherHatchedPokemon(int except) {
      for(int slot = 0; slot < 6; ++slot) {
         if (slot != except) {
            Pokemon pokemon = ClientStorageManager.party.get(slot);
            if (pokemon != null && !pokemon.isEgg()) {
               return true;
            }
         }
      }

      if (ClientTradingManager.tradeTarget != null && ClientTradingManager.tradeTarget.isEgg()) {
         return false;
      } else {
         return true;
      }
   }

   public boolean drawButtonTrade(int par1, int par2) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      if (par1 >= (this.field_146294_l - 72) / 2 && par1 <= (this.field_146294_l + 70) / 2 && par2 >= (this.field_146295_m + 26) / 2 && par2 <= (this.field_146295_m + 62) / 2) {
         this.func_73729_b((this.field_146294_l - 72) / 2, (this.field_146295_m + 26) / 2, 28, 205, 72, 19);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.trade", new Object[0]), (this.field_146294_l - 30) / 2, (this.field_146295_m + 38) / 2, 16777120);
         return true;
      } else {
         return false;
      }
   }

   public boolean drawButtonClose(int par1, int par2) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      if (par1 >= (this.field_146294_l + 213) / 2 && par1 <= (this.field_146294_l + 248) / 2 && par2 <= (this.field_146295_m + 199) / 2 && par2 >= (this.field_146295_m + 170) / 2) {
         this.func_73729_b((this.field_146294_l + 214) / 2, (this.field_146295_m + 170) / 2, 67, 225, 17, 15);
         return true;
      } else {
         return false;
      }
   }

   public boolean drawButtonReady(int par1, int par2) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.tradeGui);
      if (par1 >= (this.field_146294_l + 123) / 2 && par1 <= (this.field_146294_l + 197) / 2 && par2 <= (this.field_146295_m + 179) / 2 && par2 >= (this.field_146295_m + 148) / 2) {
         this.func_73729_b((this.field_146294_l + 122) / 2, (this.field_146295_m + 148) / 2, 28, 225, 38, 16);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.trading.ready", new Object[0]), (this.field_146294_l + 130) / 2, (this.field_146295_m + 157) / 2, 16777120);
         return true;
      } else {
         return false;
      }
   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      super.func_73864_a(par1, par2, par3);
      if (this.drawButtonReady(par1, par2) && this.selected >= 0 && ClientTradingManager.tradeTarget != null) {
         this.field_146297_k.field_71441_e.func_184133_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71439_g.func_180425_c(), SoundEvents.field_187750_dc, SoundCategory.AMBIENT, 0.7F, 1.0F);
         ClientTradingManager.player1Ready = !ClientTradingManager.player1Ready;
         Pixelmon.network.sendToServer(new ServerTrades(ClientTradingManager.player1Ready, this.tradeMachine));
      }

      if (this.drawButtonTrade(par1, par2) && ClientTradingManager.player1Ready && ClientTradingManager.player2Ready) {
         Pokemon[] party = ClientStorageManager.party.getAll();
         if (this.selected >= 0 && this.selected >= 0 && party[this.selected] != null && (!party[this.selected].isEgg() || ClientTradingManager.targetPartyIsMoreThanOnePokemon) && this.hasOtherHatchedPokemon(this.selected)) {
            this.field_146297_k.field_71441_e.func_184133_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71439_g.func_180425_c(), SoundEvents.field_187750_dc, SoundCategory.AMBIENT, 0.7F, 1.0F);
            Pixelmon.network.sendToServer(ServerTrades.getTradePacket(this.tradeMachine));
         }
      }

      if (this.drawPokemonSelection(par1, par2) != -1) {
         this.field_146297_k.field_71441_e.func_184133_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71439_g.func_180425_c(), SoundEvents.field_187750_dc, SoundCategory.AMBIENT, 0.7F, 1.0F);
         this.selected = this.drawPokemonSelection(par1, par2);
         Pixelmon.network.sendToServer(new ServerTrades(this.selected, this.tradeMachine));
         ClientTradingManager.player1Ready = false;
         ClientTradingManager.player2Ready = false;
      }

      if (this.drawButtonClose(par1, par2)) {
         this.field_146297_k.field_71441_e.func_184133_a(this.field_146297_k.field_71439_g, this.field_146297_k.field_71439_g.func_180425_c(), SoundEvents.field_187750_dc, SoundCategory.AMBIENT, 0.7F, 1.0F);
         this.field_146297_k.field_71439_g.func_71053_j();
         if (!ClientProxy.battleManager.evolveList.isEmpty()) {
            this.field_146297_k.func_147108_a(new GuiEvolve());
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
